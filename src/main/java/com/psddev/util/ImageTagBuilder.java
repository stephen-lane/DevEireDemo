package com.psddev.util;

import com.psddev.cms.db.CropOption;
import com.psddev.cms.db.ImageCrop;
import com.psddev.cms.db.ImageHotSpot;
import com.psddev.cms.db.ImageTextOverlay;
import com.psddev.cms.db.ResizeOption;
import com.psddev.cms.db.StandardImageSize;
import com.psddev.cms.tool.CmsTool;
import com.psddev.dari.db.Application;
import com.psddev.dari.util.CollectionUtils;
import com.psddev.dari.util.ImageEditor;
import com.psddev.dari.util.ImageResizeStorageItemListener;
import com.psddev.dari.util.ObjectMap;
import com.psddev.dari.util.ObjectUtils;
import com.psddev.dari.util.Settings;
import com.psddev.dari.util.StorageItem;
import com.psddev.dari.util.StringUtils;
import com.psddev.dari.util.TypeReference;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.UUID;

/**
 * <p>Static utility class for building HTML 'img' tags and URLs edited by
 * an {@link com.psddev.dari.util.ImageEditor}. This class is functionally equivalent to calling
 * the JSTL &lt;cms:img&gt; tag in your JSP code.  Example usage:</p>
 * <pre>
 * StorageItem myStorageItem;
 *
 * String imageTagHtml = new ImageTagBuilder(myStorageItem)
 *      .setWidth(300)
 *      .setHeight(200)
 *      .addAttribute("class", "thumbnail")
 *      .addAttribute("alt", "My image")
 *      .toHtml();
 * </pre>
 * You can also grab just the image URL instead of the entire HTML output
 * by calling:
 * <pre>
 * String imageUrl = new ImageTagBuilder(myStorageItem)
 *      .setWidth(300)
 *      .setHeight(200)
 *      .toUrl()
 * </pre>
 */
public final class ImageTagBuilder {

    private static final String ORIGINAL_WIDTH_METADATA_PATH = "image/originalWidth";
    private static final String ORIGINAL_HEIGHT_METADATA_PATH = "image/originalHeight";

    private static final String HOTSPOTS_CLASS_NAME = "com.psddev.image.HotSpots";

    private static final Set<String> VOID_ELEMENTS = new HashSet<>(Arrays.asList(
            "area", "base", "br", "col", "embed", "hr", "img", "input",
            "keygen", "link", "meta", "param", "source", "track", "wbr"));

    private StorageItem item;

    private ImageEditor editor;

    private StandardImageSize standardImageSize;

    private Integer width;
    private Integer height;
    private CropOption cropOption;
    private ResizeOption resizeOption;

    private String tagName;
    private String srcAttribute;
    private boolean hideDimensions;
    private boolean overlay;
    private boolean disableHotSpotCrop;
    private boolean edits = true;

    private final Map<String, String> attributes = new LinkedHashMap<>();

    public ImageTagBuilder(StorageItem item) {
        this.item = item;
    }

    public StorageItem getItem() {
        return this.item;
    }

    /**
     * Sets the name of the {@linkplain ImageEditor image editor}
     * to use.
     */
    public ImageTagBuilder editor(ImageEditor editor) {
        this.editor = editor;
        return this;
    }

    public ImageEditor getEditor() {
        return editor;
    }

    /**
     * Sets the internal name of the {@linkplain StandardImageSize
     * image size} to use.
     */
    public ImageTagBuilder standardImageSize(StandardImageSize standardImageSize) {
        this.standardImageSize = standardImageSize;
        return this;
    }

    /**
     * Sets the internal name of the {@linkplain StandardImageSize
     * image size} to use.
     */
    public ImageTagBuilder standardImageSize(String internalName) {
        this.standardImageSize = StandardImageSize.findAll().stream()
                .filter((sis) -> sis.getInternalName().equals(internalName))
                .findFirst().orElse(null);
        return this;
    }

    public StandardImageSize getStandardImageSize() {
        return standardImageSize;
    }

    /**
     * Sets the width. Note that this will override the width provided
     * by the image size set with {@link #standardImageSize(String)}.
     */
    public ImageTagBuilder width(Integer width) {
        this.width = width;
        return this;
    }

    public Integer getWidth() {
        return width;
    }

    /**
     * Sets the height. Note that this will override the height provided
     * by the image size set with {@link #standardImageSize(String)}.
     */
    public ImageTagBuilder height(Integer height) {
        this.height = height;
        return this;
    }

    public Integer getHeight() {
        return height;
    }

    /**
     * Sets the crop option. Note that this will override the crop option
     * provided by the image size set with {@link #standardImageSize(String)}.
     */
    public ImageTagBuilder cropOption(CropOption cropOption) {
        this.cropOption = cropOption;
        return this;
    }

    public CropOption getCropOption() {
        return cropOption;
    }

    /**
     * Sets the resize option. Note that this will override the resize option
     * provided by the image size set with {@link #standardImageSize(String)}.
     */
    public ImageTagBuilder resizeOption(ResizeOption resizeOption) {
        this.resizeOption = resizeOption;
        return this;
    }

    public ResizeOption getResizeOption() {
        return resizeOption;
    }

    public ImageTagBuilder tagName(String tagName) {
        this.tagName = tagName;
        return this;
    }

    public String getTagName() {
        return tagName;
    }

    /**
     * Overrides the default attribute (src) used to place the image URL. This
     * is usually used in the conjunction with lazy loading scripts that copy
     * the image URL from this attribute to the "src" attribute at some point
     * after the page has loaded.
     */
    public ImageTagBuilder srcAttribute(String srcAttribute) {
        this.srcAttribute = srcAttribute;
        return this;
    }

    public String getSrcAttribute() {
        return srcAttribute;
    }

    /**
     * Set to true if the resulting image dimensions should be removed
     * from the final tag output.
     */
    public ImageTagBuilder hideDimensions() {
        this.hideDimensions = true;
        return this;
    }

    /**
     * Set whether the resulting image dimensions should be removed
     * from the final tag output.
     */
    public ImageTagBuilder hideDimensions(boolean hideDimensions) {
        this.hideDimensions = hideDimensions;
        return this;
    }

    public boolean isHideDimensions() {
        return hideDimensions;
    }

    public ImageTagBuilder overlay(boolean overlay) {
        this.overlay = overlay;
        return this;
    }

    public boolean isOverlay() {
        return overlay;
    }

    public ImageTagBuilder disableHotSpotCrop(boolean disableHotSpotCrop) {
        this.disableHotSpotCrop = disableHotSpotCrop;
        return this;
    }

    public boolean isDisableHotSpotCrop() {
        return disableHotSpotCrop;
    }

    public ImageTagBuilder edits(boolean edits) {
        this.edits = edits;
        return this;
    }

    public boolean isEdits() {
        return edits;
    }

    /**
     * Adds an attribute to be placed on the tag.
     */
    public ImageTagBuilder attribute(String name, Object value) {
        this.attributes.put(name, value != null ? value.toString() : null);
        return this;
    }

    /**
     * Adds all the attributes to be placed on the tag.
     */
    public ImageTagBuilder attributes(Map<String, ?> attributes) {
        if (attributes != null) {
            for (Map.Entry<String, ?> entry : attributes.entrySet()) {
                attribute(entry.getKey(), entry.getValue());
            }
        }
        return this;
    }

    /**
     *
     * @return the HTML for an img tag constructed by this Builder.
     */
    public String toHtml() {
        String html = convertAttributesToHtml(tagName, toAttributes());

        if (isOverlay()) {

            StorageItem item = this.item;

            Map<String, ImageCrop> crops = null;
            if (item != null) {
                crops = findImageCrops(item);
            }

            if (item != null && crops != null && standardImageSize != null) {
                ImageCrop crop = crops.get(standardImageSize.getId().toString());

                if (crop != null) {
                    List<ImageTextOverlay> textOverlays = crop.getTextOverlays();
                    boolean hasOverlays = false;

                    for (ImageTextOverlay textOverlay : textOverlays) {
                        if (!ObjectUtils.isBlank(textOverlay.getText())) {
                            hasOverlays = true;
                            break;
                        }
                    }

                    if (hasOverlays) {
                        StringBuilder overlay = new StringBuilder();
                        CmsTool cms = Application.Static.getInstance(CmsTool.class);
                        String defaultCss = cms.getDefaultTextOverlayCss();
                        String id = "i" + UUID.randomUUID().toString().replace("-", "");

                        overlay.append("<style type=\"text/css\">");

                        if (!ObjectUtils.isBlank(defaultCss)) {
                            overlay.append("#");
                            overlay.append(id);
                            overlay.append("{display:inline-block;overflow:hidden;position:relative;");
                            overlay.append(defaultCss);
                            overlay.append("}");
                        }

                        for (CmsTool.CssClassGroup group : cms.getTextCssClassGroups()) {
                            String groupName = group.getInternalName();
                            for (CmsTool.CssClass cssClass : group.getCssClasses()) {
                                overlay.append("#");
                                overlay.append(id);
                                overlay.append(" .cms-");
                                overlay.append(groupName);
                                overlay.append("-");
                                overlay.append(cssClass.getInternalName());
                                overlay.append("{");
                                overlay.append(cssClass.getCss());
                                overlay.append("}");
                            }
                        }

                        overlay.append("</style>");

                        overlay.append("<span id=\"");
                        overlay.append(id);
                        overlay.append("\">");
                        overlay.append(html);

                        for (ImageTextOverlay textOverlay : textOverlays) {
                            String text = textOverlay.getText();

                            overlay.append("<span style=\"left: ");
                            overlay.append(textOverlay.getX() * 100);
                            overlay.append("%; position: absolute; top: ");
                            overlay.append(textOverlay.getY() * 100);
                            overlay.append("%; font-size: ");
                            overlay.append(textOverlay.getSize() * standardImageSize.getHeight());
                            overlay.append("px; width: ");
                            overlay.append(textOverlay.getWidth() != 0.0 ? textOverlay.getWidth() * 100 : 100.0);
                            overlay.append("%;\">");
                            overlay.append(text);
                            overlay.append("</span>");
                        }

                        overlay.append("</span>");
                        html = overlay.toString();
                    }
                }
            }
        }

        return html;
    }

    /**
     *
     * @return the URL to the image as a String.
     */
    public String toUrl() {
        return toAttributes().get(srcAttribute != null ? srcAttribute : "src");
    }

    /** Returns all the attributes that will get placed on the img tag. */
    public Map<String, String> toAttributes() {

        // set all the attributes
        Map<String, String> attributes = new LinkedHashMap<>();

        StorageItem item = this.item;
        if (item == null) {
            return attributes;
        }

        ImageEditor editor = this.editor;

        StandardImageSize standardImageSize = this.standardImageSize;

        Integer width = this.width;
        Integer height = this.height;
        CropOption cropOption = this.cropOption;
        ResizeOption resizeOption = this.resizeOption;

        String srcAttr = this.srcAttribute;
        boolean hideDimensions = this.hideDimensions;

        Integer originalWidth = findDimension(item, "width");
        Integer originalHeight = findDimension(item, "height");
        Map<String, ImageCrop> crops = findImageCrops(item);

        // null out all dimensions that are less than or equal to zero
        originalWidth = originalWidth != null && originalWidth <= 0 ? null : originalWidth;
        originalHeight = originalHeight != null && originalHeight <= 0 ? null : originalHeight;
        width = width != null && width <= 0 ? null : width;
        height = height != null && height <= 0 ? null : height;

        Map<String, Object> options = new LinkedHashMap<>();

        Integer cropX = null, cropY = null, cropWidth = null, cropHeight = null;

        // set fields from this standard size if they haven't already been set
        if (standardImageSize != null) {

            Integer standardWidth = standardImageSize.getWidth();
            Integer standardHeight = standardImageSize.getHeight();
            if (standardWidth <= 0) {
                standardWidth = null;
            }
            if (standardHeight <= 0) {
                standardHeight = null;
            }

            Double standardAspectRatio = null;
            if (standardWidth != null && standardHeight != null) {
                standardAspectRatio = (double) standardWidth / (double) standardHeight;
            }

            // if only one of either width or height is set then calculate
            // the other dimension based on the standardImageSize aspect
            // ratio rather than blindly taking the other standardImageSize
            // dimension.
            if (standardAspectRatio != null && (width != null || height != null)) {

                if (width != null && height == null) {
                    height = (int) (width / standardAspectRatio);

                } else if (width == null && height != null) {
                    width = (int) (height * standardAspectRatio);
                }

            } else {
                // get the standard image dimensions
                if (width == null) {
                    width = standardWidth;
                }
                if (height == null) {
                    height = standardHeight;
                }
            }

            // get the crop and resize options
            if (cropOption == null) {
                cropOption = standardImageSize.getCropOption();
            }
            if (resizeOption == null) {
                resizeOption = standardImageSize.getResizeOption();
            }

            // get a potentially smaller image from the StorageItem. This improves
            // resize performance on large images.
            StorageItem alternateItem = findStorageItemForSize(item, width, height);
            if (alternateItem != item) {
                item = alternateItem;
                originalWidth = findDimension(item, "width");
                originalHeight = findDimension(item, "height");
            }

            // get the crop coordinates
            ImageCrop crop;
            if (crops != null && (crop = crops.get(standardImageSize.getId().toString())) != null
                    && originalWidth != null && originalHeight != null) {

                cropX = (int) (crop.getX() * originalWidth);
                cropY = (int) (crop.getY() * originalHeight);
                cropWidth = (int) (crop.getWidth() * originalWidth);

                if (standardAspectRatio != null) {
                    cropHeight = (int) Math.round(cropWidth / standardAspectRatio);

                } else {
                    cropHeight = (int) (crop.getHeight() * originalHeight);
                }
            }
        }

        // if the crop info is unavailable, assume that the image
        // dimensions are the crop dimensions in case the image editor
        // knows how to crop without the x & y coordinates
        if (cropWidth == null) {
            cropWidth = width;
        }
        if (cropHeight == null) {
            cropHeight = height;
        }

        // set the options
        if (cropOption != null) {
            options.put(ImageEditor.CROP_OPTION, cropOption.getImageEditorOption());
        }
        if (resizeOption != null) {
            options.put(ImageEditor.RESIZE_OPTION, resizeOption.getImageEditorOption());
        }

        if (isEdits()) {
            @SuppressWarnings("unchecked")
            Map<String, Object> edits = (Map<String, Object>) item.getMetadata().get("cms.edits");

            if (edits != null) {
                ImageEditor realEditor = editor;
                if (realEditor == null) {
                    realEditor = ImageEditor.Static.getDefault();
                }

                //rotate first
                Set<Map.Entry<String, Object>> entrySet = new TreeMap<>(edits).entrySet();
                for (Map.Entry<String, Object> entry : entrySet) {
                    if (entry.getKey().equals("rotate")) {
                        item = realEditor.edit(item, entry.getKey(), null, entry.getValue());
                    }
                }
                for (Map.Entry<String, Object> entry : entrySet) {
                    if (!entry.getKey().equals("rotate")) {
                        item = realEditor.edit(item, entry.getKey(), null, entry.getValue());
                    }
                }
            }
        }

        // Requires at least the width and height to perform a crop
        if (cropWidth != null && cropHeight != null) {
            if (!disableHotSpotCrop
                    && useHotSpotCrop()
                    && standardImageSize != null
                    && (standardImageSize.getCropOption() == null || standardImageSize.getCropOption().equals(CropOption.AUTOMATIC))
                    && cropX == null
                    && cropY == null) {

                List<Integer> hotSpotCrop = ImageHotSpot.crop(item, cropWidth, cropHeight);
                if (!ObjectUtils.isBlank(hotSpotCrop)
                        && hotSpotCrop.size() == 4) {
                    cropX = hotSpotCrop.get(0);
                    cropY = hotSpotCrop.get(1);
                    cropWidth = hotSpotCrop.get(2);
                    cropHeight = hotSpotCrop.get(3);
                }
            }

            item = ImageEditor.Static.crop(editor, item, options, cropX, cropY, cropWidth, cropHeight);
        }

        // Requires only one of either the width or the height to perform a resize
        if (width != null || height != null) {
            item = ImageEditor.Static.resize(editor, item, options, width, height);
        }

        String url = item.getPublicUrl();
        if (url != null) {
            attributes.put(srcAttr != null ? srcAttr : "src", url);
        }

        Integer newWidth = findDimension(item, "width");
        Integer newHeight = findDimension(item, "height");
        if (newWidth != null && !hideDimensions) {
            attributes.put("width", String.valueOf(newWidth));
        }
        if (newHeight != null && !hideDimensions) {
            attributes.put("height", String.valueOf(newHeight));
        }

        if (this.attributes != null) {
            attributes.putAll(this.attributes);
        }

        if (standardImageSize != null) {
            attributes.put("data-size", standardImageSize.getInternalName());
        }

        return attributes;
    }

    private static String convertAttributesToHtml(String tagName, Map<String, String> attributes) {
        StringBuilder builder = new StringBuilder();
        if (!attributes.isEmpty()) {
            if (tagName == null) {
                tagName = "img";
            }
            builder.append("<");
            builder.append(tagName);
            for (Map.Entry<String, String> e : attributes.entrySet()) {
                String key = e.getKey();
                String value = e.getValue();
                if (!(ObjectUtils.isBlank(key) || ObjectUtils.isBlank(value))) {
                    builder.append(" ");
                    builder.append(StringUtils.escapeHtml(key));
                    builder.append("=\"");
                    builder.append(StringUtils.escapeHtml(value));
                    builder.append("\"");
                }
            }

            if (VOID_ELEMENTS.contains(tagName.toLowerCase(Locale.ENGLISH))) {
                if (Settings.get(boolean.class, "dari/selfClosingElements")) {
                    builder.append('/');
                }

                builder.append('>');

            } else {
                builder.append("></");
                builder.append(tagName);
                builder.append(">");
            }
        }
        return builder.toString();
    }

    /**
     * Finds the dimension {@code name} ("width", or "height") for the given
     * StorageItem {@code item}.
     */
    private static Integer findDimension(StorageItem item, String name) {
        if (item == null) {
            return null;
        }
        Integer dimension = null;
        Map<String, Object> metadata = item.getMetadata();
        if (metadata != null) {
            dimension = ObjectUtils.to(Integer.class, metadata.get(name));
            if (dimension == null || dimension == 0) {
                dimension = null;
            }
        }
        return dimension;
    }

    /**
     * Finds the crop information for the StorageItem {@code item}.
     */
    private static Map<String, ImageCrop> findImageCrops(StorageItem item) {
        if (item == null) {
            return null;
        }
        Map<String, ImageCrop> crops = null;
        Map<String, Object> metadata = item.getMetadata();
        if (metadata != null) {
            crops = ObjectUtils.to(new TypeReference<Map<String, ImageCrop>>() { }, metadata.get("cms.crops"));
        }
        if (crops == null) {
            crops = new HashMap<>();
        }
        return crops;
    }

    /**
     * Finds the StorageItem that best matches the provided size. This works
     * in conjunction with the ImageResizeStorageItemListener class to use a
     * presized image that is smaller than the original image in an effort to
     * improve resize performance.
     */
    private static StorageItem findStorageItemForSize(StorageItem item, Integer width, Integer height) {
        if (width == null || height == null) {
            return item;
        }

        StorageItem override = StorageItem.Static.createIn(item.getStorage());
        new ObjectMap(override).putAll(new ObjectMap(item));
        CollectionUtils.putByPath(override.getMetadata(), ORIGINAL_WIDTH_METADATA_PATH, findDimension(item, "width"));
        CollectionUtils.putByPath(override.getMetadata(), ORIGINAL_HEIGHT_METADATA_PATH, findDimension(item, "height"));

        boolean overridden = ImageResizeStorageItemListener.overridePathWithNearestSize(override, width, height);

        return overridden ? override : item;
    }

    private static boolean useHotSpotCrop() {
        return ObjectUtils.getClassByName(HOTSPOTS_CLASS_NAME) != null
                && Settings.getOrDefault(Boolean.class, "cms/image/useHotSpotCrop", Boolean.TRUE);
    }
}
