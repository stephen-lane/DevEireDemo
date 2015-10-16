package com.psddev.util.sitemap;

import com.psddev.dari.db.Database;
import com.psddev.dari.db.Query;
import com.psddev.dari.db.SqlDatabase;
import com.psddev.dari.db.State;
import com.psddev.dari.util.AsyncQueue;
import com.psddev.dari.util.CollectionUtils;
import com.psddev.dari.util.ObjectUtils;
import com.psddev.dari.util.Settings;
import com.psddev.dari.util.StringUtils;
import com.psddev.dari.util.TypeDefinition;

import com.psddev.util.StaxonUtils;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

final class SiteMapUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(SiteMapUtils.class);

    private SiteMapUtils() {
    }

    public static SiteMap getSiteMap(String siteMapUrl, Integer siteMapIndex) {

        Query<SiteMap> siteMapQuery = Query.from(SiteMap.class).where("siteMapUrl = ?", siteMapUrl);

        if (siteMapIndex != null) {
            siteMapQuery.and("siteMapIndex = ?", siteMapIndex);
        } else {
            siteMapQuery.and("siteMapIndex = missing");
        }

        return siteMapQuery.first();
    }

    public static void generateSiteMaps(SiteMapConfig config, SiteMapLastRun lastRun) {
        generateSiteMaps(config, lastRun, 0, config.getMaxAttempts());
    }

    /**
     * Generates and saves all site maps for the application, removing
     * outdated ones where necessary.
     *
     * @param config the sitemap.xml generator configuration.
     */
    private static void generateSiteMaps(SiteMapConfig config, SiteMapLastRun lastRun, int attempts, int maxAttempts) {

        //run
        lastRun.setStartDate(DateTime.now());
        lastRun.setRunning(true);
        lastRun.saveImmediately();

        Map<String, ? extends Collection<String>> configCrossSiteUrls = config.getSiteMapCrossSiteUrls();
        if (configCrossSiteUrls == null) {
            configCrossSiteUrls = Collections.emptyMap();
        }

        Integer maxEntriesCount = ObjectUtils.firstNonNull(config.getMaximumSiteMapEntries(), SiteMapGenerator.DEFAULT_MAXIMUM_SITE_MAP_ENTRIES);
        Integer maxEntriesFileSize = ObjectUtils.firstNonNull(config.getMaximumSiteMapFileSize(), SiteMapGenerator.DEFAULT_MAXIMUM_SITE_MAP_FILE_SIZE);

        Map<String, SiteMapQueue> siteConsumerQueueMap = new HashMap<>();
        Map<String, String> allCrossSiteUrlsToPrimarySiteUrl = new HashMap<>();

        // Create a queue, consumer, and latch for each Site URL.
        for (Map.Entry<String, ? extends Collection<String>> crossSiteEntry : configCrossSiteUrls.entrySet()) {

            String siteUrl = getSiteUrlOrNull(crossSiteEntry.getKey());
            Collection<String> crossSiteUrls = crossSiteEntry.getValue();
            if (siteUrl != null && !ObjectUtils.isBlank(crossSiteUrls)) {

                AsyncQueue<SiteMapEntry> entryQueue = new AsyncQueue<>();
                CountDownLatch siteCountDownLatch = new CountDownLatch(1);

                SiteMapEntryConsumer siteEntryConsumer = new SiteMapEntryConsumer(entryQueue, maxEntriesCount, maxEntriesFileSize, siteUrl, crossSiteUrls, siteCountDownLatch);
                siteEntryConsumer.submit();

                SiteMapQueue consumerQueue = new SiteMapQueue(entryQueue, siteEntryConsumer, siteCountDownLatch);
                siteConsumerQueueMap.put(siteUrl, consumerQueue);

                for (String crossSiteUrl : crossSiteUrls) {
                    allCrossSiteUrlsToPrimarySiteUrl.put(crossSiteUrl, siteUrl);
                }
            }
        }

        Query<SiteMapItem> query = Query.from(SiteMapItem.class)
                .master()
                .noCache();

        query.getOptions().put(
                SqlDatabase.USE_JDBC_FETCH_SIZE_QUERY_OPTION,
                ObjectUtils.to(Boolean.class, Settings.get(SiteMapGenerator.QUERY_USE_JDBC_FETCH_SIZE_OPTION)));

        Integer fetchSize = ObjectUtils.firstNonNull(
                ObjectUtils.to(Integer.class, Settings.get(SiteMapGenerator.QUERY_FETCH_SIZE_OPTION)),
                SiteMapGenerator.QUERY_FETCH_SIZE_DEFAULT);

        // Stores exceptionKeys PER run, so repeat exceptions don't get logged, and stacktraces can be printed when exceptions do happen.
        Set<String> exceptionKeys = new HashSet<>();

        boolean isSuccessful = false;
        long itemCount = 0L;

        try {

            for (SiteMapItem item : query.iterable(fetchSize)) {
                try {

                    // find all SiteMapEntries for this siteUrl / crossSiteUrls
                    List<SiteMapEntry> siteMapEntries = item.getSiteMapEntries();
                    if (!ObjectUtils.isBlank(siteMapEntries)) {

                        for (SiteMapEntry siteMapEntry : siteMapEntries) {

                            // Start validation of the siteMap entry.

                            // can't be null
                            if (siteMapEntry == null) {
                                continue;
                            }

                            // permalink can't be null or empty
                            String permalink = siteMapEntry.getPermalink();
                            if (StringUtils.isBlank(permalink)) {
                                continue;
                            }

                            // must be a well formed URL and have a valid base site url
                            String siteUrl = SiteMapUtils.getSiteUrlOrNull(permalink);
                            if (siteUrl == null) {
                                continue;
                            }

                            // must have a valid primary site url associated with this url.
                            String primarySiteUrl = allCrossSiteUrlsToPrimarySiteUrl.get(siteUrl);
                            if (primarySiteUrl == null) {
                                continue;
                            }

                            // must have a valid queue for the given primary site url.
                            SiteMapQueue siteConsumerQueue = siteConsumerQueueMap.get(primarySiteUrl);
                            if (siteConsumerQueue == null) {
                                continue;
                            }

                            siteConsumerQueue.getQueue().add(siteMapEntry);
                            itemCount++;
                        }
                    }

                } catch (Exception e) { // Exception from an individual item

                    StringBuilder builder = new StringBuilder();
                    builder.append(e.getClass().getName());

                    StackTraceElement[] stackTrace = e.getStackTrace();
                    if (stackTrace.length > 0) {
                        builder.append(stackTrace[0].toString());
                    }

                    String exceptionIdentifier = builder.toString();
                    if (!exceptionKeys.contains(exceptionIdentifier)) {
                        exceptionKeys.add(exceptionIdentifier);

                        String id = "";
                        if (item != null) {
                            State state = item.getState();
                            if (state != null) {
                                id = state.getId().toString();
                            }
                        }

                        LOGGER.warn(String.format("SiteMap Task couldn't process Item with id [%s]", id), e);
                    }
                }
            }

            isSuccessful = true;

        } catch (Exception e) { // Overall Query exception
            LOGGER.warn(e.getMessage(), e);

        } finally {

            int totalSiteMapCount = 0;
            for (Map.Entry<String, SiteMapQueue> siteConsumerQueueEntry : siteConsumerQueueMap.entrySet()) {

                String siteUrl = siteConsumerQueueEntry.getKey();
                SiteMapQueue siteConsumerQueue = siteConsumerQueueEntry.getValue();
                if (!StringUtils.isBlank(siteUrl) && siteConsumerQueue != null) {

                    siteConsumerQueue.getQueue().close();

                    // Make sure the consumer is finished max wait 5 minutes;
                    try {
                        siteConsumerQueue.getLatch().await(5, TimeUnit.MINUTES);
                    } catch (InterruptedException e) {
                        LOGGER.warn(e.getMessage(), e);
                    }

                    if (isSuccessful) {
                        int siteMapCount = saveSiteMaps(siteConsumerQueue.getConsumer().getSiteMaps(), siteUrl, siteConsumerQueue.getConsumer().getCrossSiteUrls());
                        totalSiteMapCount = totalSiteMapCount + siteMapCount;
                        LOGGER.info(String.format("Generated [%s] site maps for [%s] domain.", siteMapCount, siteUrl));

                    }
                }
            }

            if (!isSuccessful) {
                if (++attempts < maxAttempts) {
                    LOGGER.info(String.format("Retrying site map generation [%s] of [%s] attempts.", attempts, maxAttempts));
                    generateSiteMaps(config, lastRun, attempts, maxAttempts);
                } else {
                    LOGGER.warn("Site Map Generation failed");
                }
            }

            lastRun.endRun(isSuccessful, totalSiteMapCount, itemCount);
        }
    }

    private static int saveSiteMaps(List<SiteMap> siteMaps, String siteUrl, Collection<String> crossSiteUrls) {

        int siteMapCount = 0;
        Database database = Database.Static.getDefault();

        try {

            database.beginWrites();

            // Delete Sitemaps from existing site
            Query.from(SiteMap.class).where("siteMapUrl = ?", siteUrl).deleteAll();

            if (siteMaps.size() > 1) {

                // Create an Index Site Map for this siteUrl
                SiteMap siteMapIndex = new SiteMap();
                siteMapIndex.setSiteMapUrl(siteUrl);
                siteMapIndex.setCrossSiteUrls(new HashSet<>(crossSiteUrls));

                // Set indexSiteMaps and set indexSiteMapXML data.
                siteMapIndex.setSiteMaps(siteMaps);

                Map<String, Object> siteMapIndexMap = getSiteMapIndexMap(siteMaps);

                siteMapIndex.setXml(getSiteMapXml(siteMapIndexMap, false));

                for (SiteMap siteMap : siteMaps) {
                    siteMap.save();
                    siteMapCount++;
                }

                // save index sitemap
                siteMapIndex.setLastModified(new Date());
                siteMapIndex.save();
                siteMapCount++;

            } else if (siteMaps.size() == 1) {

                SiteMap siteMap = siteMaps.get(0);
                siteMap.setSiteMapIndex(null);
                siteMap.save();
                siteMapCount++;
            }

            database.commitWrites();

        } catch (Exception e) {
            LOGGER.warn(e.getMessage(), e);

        } finally {
            database.endWrites();
        }

        return siteMapCount;
    }

    private static Map<String, Object> getSiteMapIndexMap(List<SiteMap> siteMapObjects) {

        Map<String, Object> siteMapIndexRoot = new LinkedHashMap<>();
        Map<String, Object> siteMapIndex = new LinkedHashMap<>();

        siteMapIndex.put("@" + Constants.SITE_MAP_XMLNS, Constants.SITE_MAP_XMLNS_URL);
        siteMapIndex.put("@" + Constants.SITE_MAP_XSI, Constants.SITE_MAP_XSI_URL);
        siteMapIndex.put("@" + Constants.SITE_MAP_XSI_LOC, Constants.SITE_MAP_INDEX_XSI_LOC_URL);

        List<Map<String, Object>> siteMaps = new ArrayList<>();

        for (SiteMap siteMapObject : siteMapObjects) {

            Map<String, Object> siteMap = new LinkedHashMap<>();

            String siteUrl = siteMapObject.getSiteMapUrl();
            Date lastModified = siteMapObject.getLastModified();

            siteMap.put(Constants.SITE_MAP_INDEX_LOC_ELEMENT, StringUtils.ensureEnd(siteUrl, "/") + "sitemap" + siteMapObject.getSiteMapIndex() + ".xml");
            if (lastModified != null) {
                siteMap.put(Constants.SITE_MAP_INDEX_LASTMOD_ELEMENT, new MediumDateFieldTransformer().transform(lastModified));
            }

            siteMaps.add(siteMap);
        }

        siteMapIndex.put(Constants.SITE_MAP_INDEX_SITEMAP_ELEMENT, siteMaps);

        siteMapIndexRoot.put(Constants.SITE_MAP_INDEX_ELEMENT, siteMapIndex);

        return siteMapIndexRoot;
    }

    public static String getSiteUrlOrNull(String permalink) {
        URL url;
        try {
            url = new URL(permalink);

            String protocol = url.getProtocol();
            String host = url.getHost();
            int port = url.getPort();

            return protocol + "://" + host + (port >= 0 ? ":" + port : "");

        } catch (MalformedURLException e) {
            // do nothing, skip
        }

        return null;
    }

    static String getSiteMapXml(Map<String, Object> siteMap, boolean formatted) {

        return StaxonUtils.jsonToXmlOrNull(ObjectUtils.toJson(siteMap), formatted);
    }

    static Map<String, Object> toXmlJsonMap(Object object) {

        Map<String, Object> map = new LinkedHashMap<>();

        TypeDefinition<?> typeDef = TypeDefinition.getInstance(object.getClass());

        List<String> missingRequired = null;

        for (Field field : typeDef.getAllFields()) {

            XmlField xmlField = field.getAnnotation(XmlField.class);

            if (xmlField != null) {

                String path = xmlField.path();
                boolean required = xmlField.required();
                Class<? extends FieldTransformer> fieldTransformerClass = xmlField.transformer();

                FieldTransformer fieldTransformer = null;
                if (fieldTransformerClass != null) {
                    fieldTransformer = TypeDefinition.getInstance(fieldTransformerClass).newInstance();
                }

                Object value = null;
                try {
                    if (fieldTransformer == null) {
                        fieldTransformer = new DefaultFieldTransformer();
                    }

                    value = fieldTransformer.transform(field.get(object));

                } catch (IllegalArgumentException | IllegalAccessException e) {
                    LOGGER.warn(e.getMessage(), e);
                }

                if (value != null) {
                    CollectionUtils.putByPath(map, path, value);

                } else if (required) {
                    if (missingRequired == null) {
                        missingRequired = new ArrayList<>();
                    }
                    missingRequired.add(path);
                }
            }
        }

        if (ObjectUtils.isBlank(missingRequired)) {
            return map;

        } else {
            LOGGER.warn("Skipping site map element [" + map + "] of type ["
                    + object.getClass().getName() + "] because "
                    + missingRequired + " is required!");
            return null;
        }
    }

    public static final int XML_DECLARATION_SIZE = 38;

    private static final int XML_ELEMENT_SIZE_OVERHEAD = 5;
    private static final int XML_EMPTY_ELEMENT_SIZE_OVERHEAD = 4;
    private static final int XML_ATTRIBUTE_SIZE_OVERHEAD = 3;

    /**
     * Calculates the size in bytes of that resulting XML. It does not include
     * the size of the XML declaration but it can be added manually using the
     * constant value {@link #XML_DECLARATION_SIZE}.
     *
     * @param jsonMap map compliant with being transformed into XML.
     * @return the size of the resulting XML.
     */
    public static Integer getXmlLengthForJsonMap(Map<String, Object> jsonMap) {
        return getXmlLengthForJsonObject(null, jsonMap);
    }

    private static Integer getXmlLengthForJsonObject(String previousKey, Object object) {

        if (object instanceof Map) {

            @SuppressWarnings("unchecked")
            Map<String, Object> map = (Map<String, Object>) object;

            int length = 0;

            boolean hasElements = false;

            for (Map.Entry<String, Object> entry : map.entrySet()) {

                String key = entry.getKey();
                Object value = entry.getValue();

                if (key.startsWith("@")) {

                    if (value != null) {
                        length += (key.length() + value.toString().length() + XML_ATTRIBUTE_SIZE_OVERHEAD);
                    }

                } else {
                    Iterable<?> items;

                    if (value instanceof Iterable) {
                        items = (Iterable<?>) value;

                    } else {
                        items = Collections.singleton(value);
                    }

                    for (Object item : items) {
                        hasElements = true;

                        if (item != null) {
                            length += (key.length() * 2 + XML_ELEMENT_SIZE_OVERHEAD);
                            length += getXmlLengthForJsonObject(key, item);

                        } else {
                            length += (key.length() + XML_EMPTY_ELEMENT_SIZE_OVERHEAD);
                        }
                    }
                }
            }

            if (!hasElements && previousKey != null) {
                length -= (previousKey.length() + 1);
            }

            return length;

        } else {
            if (object != null) {
                return object.toString().length();
            } else {
                return 0;
            }
        }
    }

    static final LoadingCache<String, SiteMapConfig> SITE_MAP_CONFIG_INSTANCES = CacheBuilder
            .newBuilder()
            .build(new CacheLoader<String, SiteMapConfig>() {

                @Override
                public SiteMapConfig load(String name) {
                    String settingsName = SiteMapConfig.SETTING_PREFIX + "/" + name;
                    SiteMapConfig instance = null;
                    if (Settings.get(settingsName) != null) {
                        instance = Settings.newInstance(SiteMapConfig.class, settingsName);
                    }
                    return instance;
                }
            });
}
