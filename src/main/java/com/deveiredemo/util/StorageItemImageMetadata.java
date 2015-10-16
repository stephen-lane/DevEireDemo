package com.deveiredemo.util;

import com.psddev.dari.db.Location;
import com.psddev.dari.util.CollectionUtils;
import com.psddev.dari.util.ObjectUtils;
import com.psddev.dari.util.StorageItem;

import org.joda.time.DateTime;
import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Parses out the IPTC/EXIF/GPS information from a an image StorageItem's
 * metadata and exposes it in convenient to use APIs
 */
public class StorageItemImageMetadata {

    /* FILE NAME */
    public static final String ORIGINAL_FILENAME_KEY = "originalFilename";

    /* DIMENSIONS */
    public static final String WIDTH_KEY = "width"; // 1600
    public static final String HEIGHT_KEY = "height"; // 1200

    /* HTTP HEADERS */
    public static final String HTTP_HEADER_CACHE_CONTROL_KEY = "http.headers/Cache-Control"; // [ "public, max-age=31536000" ]
    public static final String HTTP_HEADER_CONTENT_LENGTH_KEY = "http.headers/Content-Length"; // [ "99360" ]
    public static final String HTTP_HEADER_CONTENT_TYPE_KEY = "http.headers/Content-Type"; // [ "image/jpeg" ]

    /* EXIF */
    public static final String EXIF_APERTURE_VALUE_KEY = "Exif/Aperture Value"; // F2.8
    public static final String EXIF_ARTIST_KEY = "Exif/Artist"; // Rick Rycroft
    public static final String EXIF_BITS_PER_SAMPLE_KEY = "Exif/Bits Per Sample"; // 8 8 8 bits/component/pixel
    public static final String EXIF_COLOR_SPACE_KEY = "Exif/Color Space"; // Undefined
    public static final String EXIF_COMPONENTS_CONFIGURATION_KEY = "Exif/Components Configuration"; // YCbCr
    public static final String EXIF_COMPRESSION_KEY = "Exif/Compression"; // JPEG (old-style)
    public static final String EXIF_COPYRIGHT = "Exif/Copyright"; // 2015 Getty Images
    public static final String EXIF_CUSTOM_RENDERER_KEY = "Exif/Custom Rendered"; // Normal process
    public static final String EXIF_DATE_TIME_DIGITIZED_KEY = "Exif/Date//Time Digitized"; // 2014:01:22 12:12:44
    public static final String EXIF_DATE_TIME_KEY = "Exif/Date//Time"; // 2014:01:22 12:15:12
    public static final String EXIF_DATE_TIME_ORIGINAL_KEY = "Exif/Date//Time Original"; // 2014:01:22 12:12:44
    public static final String EXIF_EXPOSURE_BIAS_VALUE_KEY = "Exif/Exposure Bias Value"; // 0 EV
    public static final String EXIF_EXPOSURE_MODE_KEY = "Exif/Exposure Mode"; // Manual exposure
    public static final String EXIF_EXPOSURE_PROGRAM_KEY = "Exif/Exposure Program"; // Manual control
    public static final String EXIF_EXPOSURE_TIME_KEY = "Exif/Exposure Time"; // 1/6400 sec
    public static final String EXIF_F_NUMBER_KEY = "Exif/F-Number"; // F2.8
    public static final String EXIF_FLASH_KEY = "Exif/Flash"; // Flash did not fire, auto
    public static final String EXIF_FLASHPIX_VERSION_KEY = "Exif/FlashPix Version"; // 1.00
    public static final String EXIF_FOCAL_LENGTH_KEY = "Exif/Focal Length"; // 400.0 mm
    public static final String EXIF_FOCAL_PLANE_RESOLUTION_UNIT_KEY = "Exif/Focal Plane Resolution Unit"; // Inches
    public static final String EXIF_FOCAL_PLANE_X_RESOLUTION_KEY = "Exif/Focal Plane X Resolution"; // 731/2592000 inches
    public static final String EXIF_FOCAL_PLANE_Y_RESOLUTION_KEY = "Exif/Focal Plane Y Resolution"; // 490/1728000 inches
    public static final String EXIF_IMAGE_HEIGHT_KEY = "Exif/Exif Image Height"; // 2146 pixels
    public static final String EXIF_IMAGE_WIDTH_KEY = "Exif/Exif Image Width"; // 3014 pixels
    public static final String EXIF_ISO_SPEED_RATINGS_KEY = "Exif/ISO Speed Ratings"; // 200
    public static final String EXIF_MAKE_KEY = "Exif/Make"; // Canon
    public static final String EXIF_MAX_APERTURE_VALUE_KEY = "Exif/Max Aperture Value"; // F2.8
    public static final String EXIF_METERING_MODE_KEY = "Exif/Metering Mode"; // Multi-segment
    public static final String EXIF_MODEL_KEY = "Exif/Model"; // Canon EOS-1D X
    public static final String EXIF_ORIENTATION_KEY = "Exif/Orientation"; // Top, left side (Horizontal / normal)
    public static final String EXIF_PHOTOMETRIC_INTERPRETATION_KEY = "Exif/Photometric Interpretation"; // RGB
    public static final String EXIF_PRIMARY_CHROMATICITIES_KEY = "Exif/Primary Chromaticities"; // 64/100 33/100 21/100 71/100 15/100 6/100
    public static final String EXIF_RESOLUTION_UNIT_KEY = "Exif/Resolution Unit"; // Inch
    public static final String EXIF_SAMPLES_PER_PIXEL_KEY = "Exif/Samples Per Pixel"; // 3 samples/pixel
    public static final String EXIF_SCENE_CAPTURE_TYPE_KEY = "Exif/Scene Capture Type"; // Standard
    public static final String EXIF_SHUTTER_SPEED_VALUE_KEY = "Exif/Shutter Speed Value"; // 1/6316 sec
    public static final String EXIF_SOFTWARE_KEY = "Exif/Software"; // Adobe Photoshop CS5.1 Macintosh
    public static final String EXIF_SUB_SEC_TIME_DIGITIZED_KEY = "Exif/Sub-Sec Time Digitized"; // 70
    public static final String EXIF_SUB_SEC_TIME_KEY = "Exif/Sub-Sec Time"; // 70
    public static final String EXIF_SUB_SEC_TIME_ORIGINAL_KEY = "Exif/Sub-Sec Time Original"; // 70
    public static final String EXIF_SUBJECT_DISTANCE_KEY = "Exif/Subject Distance"; // 22.2 metres
    public static final String EXIF_THUMBNAIL_DATE_KEY = "Exif/Thumbnail Data"; // [5270 bytes of thumbnail data]
    public static final String EXIF_THUMBNAIL_IMAGE_HEIGHT_KEY = "Exif/Thumbnail Image Height"; // 2146 pixels
    public static final String EXIF_THUMBNAIL_IMAGE_WIDTH_KEY = "Exif/Thumbnail Image Width"; // 3014 pixels
    public static final String EXIF_THUMBNAIL_LENGTH_KEY = "Exif/Thumbnail Length"; // 5270 bytes
    public static final String EXIF_THUMBNAIL_OFFSET_KEY = "Exif/Thumbnail Offset"; // 1714 bytes
    public static final String EXIF_USER_COMMENT_KEY = "Exif/User Comment"; // ???
    public static final String EXIF_VERSION_KEY = "Exif/Exif Version"; // 2.30
    public static final String EXIF_WHITE_BALANCE_KEY = "Exif/White Balance"; // Auto white balance
    public static final String EXIF_WHITE_POINT_KEY = "Exif/White Point"; // 313/1000 329/1000
    public static final String EXIF_X_RESOLUTION_KEY = "Exif/X Resolution"; // 72 dots per inch
    public static final String EXIF_Y_RESOLUTION_KEY = "Exif/Y Resolution"; // 72 dots per inch
    public static final String EXIF_YCBCR_COEFFICIENTS_KEY = "Exif/YCbCr Coefficients"; // 299/1000 587/1000 114/1000
    public static final String EXIF_YCBCR_POSITIONING_KEY = "Exif/YCbCr Positioning"; // Datum point

    /* IPTC */
    public static final String IPTC_BYLINE_KEY = "Iptc/By-line"; // Rick Rycroft
    public static final String IPTC_BYLINE_TITLE_KEY = "Iptc/By-line Title"; // STF
    public static final String IPTC_CAPTION_ABSTRACT_KEY = "Iptc/Caption//Abstract"; // Dominika Cibulkova of Slovakia celebrates...
    public static final String IPTC_CATEGORY_KEY = "Iptc/Category"; // S
    public static final String IPTC_CITY_KEY = "Iptc/City"; // Melbourne
    public static final String IPTC_COPYRIGHT_NOTICE_KEY = "Iptc/Copyright Notice"; // 2015 Getty Images
    public static final String IPTC_COUNTRY_LOCATION_KEY = "Iptc/Country//Primary Location"; // AUS
    public static final String IPTC_CREDIT_KEY = "Iptc/Credit"; // ASSOCIATED PRESS
    public static final String IPTC_DATE_CREATED_KEY = "Iptc/Date Created"; // Wed Jan 22 00:00:00 EST 2014
    public static final String IPTC_DIRECTORY_VERSION_KEY = "Iptc/Directory Version"; // 12848
    public static final String IPTC_HEADLINE_KEY = "Iptc/Headline"; // Dominika Cibulkova of Slovakia celebrates...
    public static final String IPTC_OBJECT_NAME_KEY = "Iptc/Object Name"; // APTOPIX Australian Open Tennis
    public static final String IPTC_ORIGINAL_TRANSMISSION_REFERENCE_KEY = "Iptc/Original Transmission Reference"; // XMEL130
    public static final String IPTC_SUPPLEMENTAL_CATEGORIES_KEY = "Iptc/Supplemental Category(s)"; // TEN
    public static final String IPTC_TIME_CREATED_KEY = "Iptc/Time Created"; // 121244+0000
    public static final String IPTC_URGENCY_KEY = "Iptc/Urgency"; // 52
    public static final String IPTC_WRITER_EDITOR_KEY = "Iptc/Writer//Editor"; // MDB KAJ

    /* GPS */
    public static final String GPS_LATITUDE_KEY = "GPS/GPS Latitude"; // 39"5'13.199987
    public static final String GPS_LATITUDE_REF_KEY = "GPS/GPS Latitude Ref"; // N
    public static final String GPS_LONGITUDE_KEY = "GPS/GPS Longitude"; // 76"45'21.600037
    public static final String GPS_LONGITUDE_REF_KEY = "GPS/GPS Longitude Ref"; // W

    /* JPEG */
    public static final String JPEG_IMAGE_HEIGHT_KEY = "Jpeg/Image Height"; // 1200 pixels
    public static final String JPEG_IMAGE_WIDTH_KEY = "Jpeg/Image Width"; // 1600 pixels

    /* EXIF_ORIENTATION_KEY values */
    public static final Map<String, Integer> EXIF_IMAGE_ORIENTATIONS; static {
        Map<String, Integer> map = new HashMap<>();
        map.put("Top, left side (Horizontal / normal)",                     1);
        map.put("Top, right side (Mirror horizontal)",                      2);
        map.put("Bottom, right side (Rotate 180)",                          3);
        map.put("Bottom, left side (Mirror vertical)",                      4);
        map.put("Left side, top (Mirror horizontal and rotate 270 CW)",     5);
        map.put("Right side, top (Rotate 90 CW)",                           6);
        map.put("Right side, bottom (Mirror horizontal and rotate 90 CW)",  7);
        map.put("Left side, bottom (Rotate 270 CW)",                        8);
        EXIF_IMAGE_ORIENTATIONS = map;
    }

    private static final transient String[] EXIF_DATE_TIME_KEYS = {
            EXIF_DATE_TIME_DIGITIZED_KEY,
            EXIF_DATE_TIME_ORIGINAL_KEY,
            EXIF_DATE_TIME_KEY
    };

    private static final transient String[] METADATA_TITLE_KEYS = {
            IPTC_HEADLINE_KEY,
            IPTC_OBJECT_NAME_KEY
    };

    private static final transient String[] METADATA_BYLINE_KEYS = {
            IPTC_BYLINE_KEY,
            EXIF_ARTIST_KEY
    };

    /* Date-Time Format: 2014:01:22 12:12:44 */
    private static final String EXIF_DATE_TIME_FORMAT = "yyyy:MM:dd hh:mm:ss";

    /* Time Format: 121244+0000 */
    private static final String IPTC_TIME_CREATED_FORMAT = "hhmmss+SSSS";

    /* Date Format: Wed Jan 22 00:00:00 EST 2014 */
    private static final String IPTC_DATE_CREATED_FORMAT = "EEE MMM dd 00:00:00 zzz yyyy";

    private StorageItem item;
    private Map<String, Object> metadata;

    public StorageItemImageMetadata(StorageItem item) {
        if (item != null) {
            metadata = item.getMetadata();
        }
        if (metadata == null) {
            metadata = new HashMap<>();
            if (item != null) {
                item.setMetadata(metadata);
            }
        }
    }

    public Integer getWidth() {
        return getValueForKey(Integer.class, WIDTH_KEY);
    }

    public Integer getHeight() {
        return getValueForKey(Integer.class, HEIGHT_KEY);
    }

    public String getCaption() {
        return getValueForKey(String.class, IPTC_CAPTION_ABSTRACT_KEY);
    }

    public String getTitle() {
        return getFirstNonBlankValueForKeys(String.class, METADATA_TITLE_KEYS);
    }

    public String getOriginalFileName() {
        return getValueForKey(String.class, ORIGINAL_FILENAME_KEY);
    }

    public String getByline() {
        return getFirstNonBlankValueForKeys(String.class, METADATA_BYLINE_KEYS);
    }

    public String getCredit() {
        return getValueForKey(String.class, IPTC_CREDIT_KEY);
    }

    public Date getDateTaken() {

        DateTime dateTaken = null;

        // check the IPTC metadata fields
        String iptcDate = getValueForKey(String.class, IPTC_DATE_CREATED_KEY);
        if (iptcDate != null) {
            // check the date part
            try {
                dateTaken = DateTimeFormat.forPattern(IPTC_DATE_CREATED_FORMAT).parseDateTime(iptcDate);
            } catch (RuntimeException e) {
                // do nothing
            }
            if (dateTaken != null) {
                // check the time part
                String iptcTime = getValueForKey(String.class, IPTC_TIME_CREATED_KEY);
                if (iptcTime != null) {
                    LocalTime timeTaken = null;
                    try {
                        timeTaken = LocalTime.parse(iptcTime, DateTimeFormat.forPattern(IPTC_TIME_CREATED_FORMAT));
                    } catch (RuntimeException e) {
                        // do nothing
                    }
                    // apply the time to the date
                    if (timeTaken != null) {
                        dateTaken = dateTaken.withFields(timeTaken);
                    }
                }
            }
        }

        // check the exif metadata fields
        if (dateTaken == null) {
            String exifDateTime = getFirstNonBlankValueForKeys(String.class, EXIF_DATE_TIME_KEYS);
            if (exifDateTime != null) {
                try {
                    dateTaken = DateTimeFormat.forPattern(EXIF_DATE_TIME_FORMAT).parseDateTime(exifDateTime);
                } catch (RuntimeException e) {
                    // do nothing
                }
            }
        }

        return dateTaken != null ? dateTaken.toDate() : null;
    }

    public Location getGpsLocation() {
        Double x = getGpsDegrees(getValueForKey(String.class, GPS_LATITUDE_KEY), getValueForKey(String.class, GPS_LATITUDE_REF_KEY));
        Double y = getGpsDegrees(getValueForKey(String.class, GPS_LONGITUDE_KEY), getValueForKey(String.class, GPS_LONGITUDE_REF_KEY));
        return x != null && y != null ? new Location(x, y) : null;
    }

    public void fixOrientation() {

        // Work some magic to deal with photos with orientation metadata.
        String orientationKey = getValueForKey(String.class, EXIF_ORIENTATION_KEY);
        Integer orientationValue = EXIF_IMAGE_ORIENTATIONS.get(orientationKey);

        if (orientationValue != null) {
            switch (orientationValue) {
                case 1:
                    // do nothing
                    break;
                case 2:
                    CollectionUtils.putByPath(metadata, "cms.edits/flipH", true);
                    break;
                case 3:
                    CollectionUtils.putByPath(metadata, "cms.edits/flipH", true);
                    CollectionUtils.putByPath(metadata, "cms.edits/flipV", true);
                    break;
                case 4:
                    CollectionUtils.putByPath(metadata, "cms.edits/flipV", true);
                    break;
                case 5:
                    CollectionUtils.putByPath(metadata, "cms.edits/flipH", true);
                    CollectionUtils.putByPath(metadata, "cms.edits/rotate", -90);
                    break;
                case 6:
                    CollectionUtils.putByPath(metadata, "cms.edits/rotate", 90);
                    break;
                case 7:
                    CollectionUtils.putByPath(metadata, "cms.edits/flipH", true);
                    CollectionUtils.putByPath(metadata, "cms.edits/rotate", 90);
                    break;
                case 8:
                    CollectionUtils.putByPath(metadata, "cms.edits/rotate", -90);
                    break;
                default:
                    // do nothing
            }
        }
    }

    private Double getGpsDegrees(String dmsString, String dmsRef) {

        if (dmsString == null || dmsRef == null) {
            return null;
        }

        // 39"5'13.199987
        int doubleQuoteIndex = dmsString.indexOf('"');
        int singleQuoteIndex = dmsString.indexOf('\'');

        if (doubleQuoteIndex <= 0 || singleQuoteIndex <= 0
                || singleQuoteIndex < doubleQuoteIndex
                || singleQuoteIndex == dmsString.length() - 1) {
            return null;
        }

        Double degrees = ObjectUtils.to(Double.class, dmsString.substring(0, doubleQuoteIndex));
        Double minutes = ObjectUtils.to(Double.class, dmsString.substring(doubleQuoteIndex + 1, singleQuoteIndex));
        Double seconds = ObjectUtils.to(Double.class, dmsString.substring(singleQuoteIndex + 1));

        if (degrees == null || minutes == null || seconds == null) {
            return null;
        }

        double decimalDegrees = degrees + (minutes / 60) + (seconds / 3600);

        switch (dmsRef) {
            case "N":
            case "E":
                break;
            case "S":
            case "W":
                decimalDegrees *= -1;
                break;
            default:
                return null;
        }

        return decimalDegrees;
    }

    /**
     * Double slashes in the path means that it's part of the key and NOT a
     * separator.
     */
    private <T> T getValueForKey(Class<T> returnType, String path) {

        // handle double slashes
        if (path.contains("//")) {

            Object subMetaData = metadata;
            // https://docs.oracle.com/javase/8/docs/api/java/util/regex/Pattern.html#special
            for (String pathPart : path.split("(?<!/)/(?!/)")) {

                pathPart = pathPart.replaceAll("//", "/");

                if (subMetaData instanceof Map) {
                    subMetaData = ((Map<?, ?>) subMetaData).get(pathPart);
                } else {
                    subMetaData = CollectionUtils.getByPath(subMetaData, pathPart);
                }
            }
            return ObjectUtils.to(returnType, subMetaData);

        } else {
            return ObjectUtils.to(returnType, CollectionUtils.getByPath(metadata, path));
        }
    }

    /**
     * Takes an array of paths and gets the first non-blank result of calling
     * {@link #getValueForKey(Class, String)}.
     */
    private <T> T getFirstNonBlankValueForKeys(Class<T> returnType, String... paths) {

        T value = null;

        for (String path : paths) {
            value = getValueForKey(returnType, path);
            if (!ObjectUtils.isBlank(value)) {
                break;
            }
        }

        return value;
    }
}
