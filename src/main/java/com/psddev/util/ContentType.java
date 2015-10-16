package com.psddev.util;

import com.psddev.dari.util.ObjectUtils;
import com.psddev.dari.util.SparseSet;
import com.psddev.dari.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Represents HTTP Content-Type header field values and provides methods for
 * working with them.
 */
public enum ContentType {

    /*
     * List of all the top level types
     *
     * application
     * audio
     * image
     * message
     * model
     * multipart
     * text
     * video
     */

    JPEG("image", "jpeg", "jpg", "pjpeg"),
    PNG ("image", "png", "png", "x-png"),
    GIF ("image", "gif", "gif"),
    TIFF("image", "tiff", "tiff"),
    BMP ("image", "bmp", "bmp", "x-xbitmap"),

    MOV ("video", "quicktime", "mov", "mov"),
    MP4 ("video", "mp4", "mp4"),

    MP3 ("audio", "mp3", "mp3"),
    M4A ("audio", "x-m4a", "m4a"),
    WMA ("audio", "x-ms-wma", "wma");

    private static final Map<String, Map<String, ContentType>> VARIANTS_TO_CONTENT_TYPES; static {
        Map<String, Map<String, ContentType>> map = new HashMap<>();

        for (ContentType contentType : ContentType.values()) {

            String type = contentType.getType();

            Map<String, ContentType> subTypes = map.get(type);
            if (subTypes == null) {
                subTypes = new HashMap<>();
                map.put(type, subTypes);
            }

            for (String variant : contentType.getVariants()) {
                subTypes.put(variant, contentType);
            }
        }

        VARIANTS_TO_CONTENT_TYPES = map;
    }

    private String type;
    private String subType;
    private String fileExtension;
    private Set<String> variants;

    private ContentType(String type, String subType, String fileExtension, String... variants) {
        Set<String> variantsSet = new HashSet<>(Arrays.asList(variants));
        variantsSet.add(subType);
        variantsSet.add(fileExtension);

        this.type = type;
        this.subType = subType;
        this.fileExtension = fileExtension;
        this.variants = Collections.unmodifiableSet(variantsSet);
    }

    public String getType() {
        return type;
    }

    public String getSubType() {
        return subType;
    }

    public String getFileExtension() {
        return fileExtension;
    }

    /** Returns an unmodifiable Set of the different browser variants for
     *  this image content type. */
    public Set<String> getVariants() {
        return variants;
    }

    /**
     * Gets the string representation of the content type as it would appear
     * in the HTTP Content-Type header.
     *
     * @return the content type.
     */
    public String getContentType() {
       return getType() + "/" + getSubType();
    }

    // TODO: Use this method to consolidate the logic of the other methods below
    public static ContentType fromString(String contentTypeString) {

        if (contentTypeString == null) {
            return null;
        }

        ContentType contentType = null;

        String type;
        String subType;

        int slashAt = contentTypeString.indexOf('/');

        if (slashAt >= 0) {
            type = contentTypeString.substring(0, slashAt);
            subType = contentTypeString.substring(slashAt + 1);

            Map<String, ContentType> variantsToContentTypes = VARIANTS_TO_CONTENT_TYPES.get(type);

            if (variantsToContentTypes != null) {
                contentType = variantsToContentTypes.get(subType);
            }
        }

        return contentType;
    }

    /** Adds any variants of the given type and subTypes to the result.
     *
     *  Ex. for type = "image"
     * [jpg] --&gt; [jpeg, jpg, pjpeg]
     *
     * */
    public static Set<String> getVariantsForSubTypes(String type, String... subTypes) {

        Set<String> variants = new HashSet<>();
        if (subTypes != null) {
            for (String subType : subTypes) {

                Map<String, ContentType> variantsToContentTypes = VARIANTS_TO_CONTENT_TYPES.get(type);
                ContentType contentType;

                if (variantsToContentTypes != null && (contentType = variantsToContentTypes.get(subType)) != null) {
                    variants.addAll(contentType.getVariants());
                } else {
                    variants.add(subType);
                }
            }
        }
        return variants;
    }

    /** Returns the normalized or "official" content type for a given variant.
     *
     *  Ex. image/pjpeg --&gt; image/jpeg
     *  Ex. image/x-xbitmap --&gt; image/bmp
     *
     * */
    public static String getNormalizedContentType(String contentTypeString) {
        if (contentTypeString == null) {
            return null;
        }

        ContentType contentType = null;

        String type = null;
        String subType;

        int slashAt = contentTypeString.indexOf('/');

        if (slashAt >= 0) {
            type = contentTypeString.substring(0, slashAt);
            subType = contentTypeString.substring(slashAt + 1);

            Map<String, ContentType> variantsToContentTypes = VARIANTS_TO_CONTENT_TYPES.get(type);

            if (variantsToContentTypes != null) {
                contentType = variantsToContentTypes.get(subType);
            }
        }

        if (contentType != null) {
            return type + "/" + contentType.getSubType();

        } else {
            return contentTypeString;
        }
    }

    /**
     * Converts a set of content types into a String formatted to be
     * placed in a file input's accept attribute, and includes all
     * variants of the given content types.
     *
     * <p>
     * Ex. ["image/jpeg", "image/png"] --&gt; "image/jpeg,image/jpg,image/pjpeg,image/png"
     *
     */
    public static String contentTypesToFileInputAcceptAttribute(Set<String> contentTypes) {
        if (ObjectUtils.isBlank(contentTypes)) {
            return null;

        } else {
            Set<String> expandedContentTypes = new HashSet<>();

            String type;
            String subType;

            for (String contentType : contentTypes) {
                int slashAt = contentType.indexOf('/');

                if (slashAt >= 0) {
                    type = contentType.substring(0, slashAt);
                    subType = contentType.substring(slashAt + 1);

                    Set<String> variants = getVariantsForSubTypes(type, subType);

                    for (String variant : variants) {
                        expandedContentTypes.add(type + "/" + variant);
                    }

                } else {
                    expandedContentTypes.add(contentType);
                }
            }

            return StringUtils.join(new ArrayList<>(expandedContentTypes), ",");
        }
    }

    /** Converts a set of content types into a sparse set that includes
     *  all variants of the given content types.
     *
     *  <p>
     *  Ex. ["image/jpeg", "image/png"] --&gt; "+image/jpeg +image/jpg +image/pjpeg +image/png"
     *
     *  Ex. ["image/"] --&gt; "+image/"
     *
     *   */
    public static SparseSet contentTypesToSparseSet(Set<String> contentTypes) {
        if (ObjectUtils.isBlank(contentTypes)) {
            return new SparseSet("+/");

        } else {

            SparseSet set = new SparseSet();

            String type;
            String subType;

            for (String contentType : contentTypes) {

                int slashAt = contentType.indexOf('/');

                if (slashAt >= 0) {
                    type = contentType.substring(0, slashAt);
                    subType = contentType.substring(slashAt + 1);

                    Set<String> variants = getVariantsForSubTypes(type, subType);

                    for (String variant : variants) {
                        set.add(type + "/" + variant);
                    }

                } else {
                    set.add(contentType);
                }
            }

            return set;
        }
    }

    public static String contentTypesToPrettyString(Set<String> contentTypes) {

        List<String> subTypes = new ArrayList<>();

        if (contentTypes != null) {
            for (String contentType : contentTypes) {

                int slashAt = contentType.indexOf('/');
                if (slashAt >= 0) {
                    String subType = contentType.substring(slashAt + 1);
                    if (!subType.isEmpty()) {
                        subTypes.add(subType);
                    }
                }
            }
        }

        if (!subTypes.isEmpty()) {

            if (subTypes.size() == 1) {
                return subTypes.get(0);

            } else if (subTypes.size() == 2) {
                return subTypes.get(0) + " or " + subTypes.get(1);

            } else {
                StringBuilder builder = new StringBuilder();

                Object[] subTypesArr = subTypes.toArray();

                for (int i = 0; i < subTypesArr.length - 2; i++) {
                    builder.append(subTypesArr[i]).append(", ");
                }

                builder.append(subTypesArr[subTypesArr.length - 2]);
                builder.append(" or ");
                builder.append(subTypesArr[subTypesArr.length - 1]);

                return builder.toString();
            }

        } else {
            return "N/A";
        }
    }

    public static String contentTypesToPrettyString(String types) {
        if (ObjectUtils.isBlank(types)) {
            return null;
        }

        String[] typesArr = StringUtils.split(types.trim().toUpperCase(), "[,\\s]+");
        if (typesArr.length > 0) {
            if (typesArr.length == 1) {
                return typesArr[0];

            } else if (typesArr.length == 2) {
                return typesArr[0] + " or " + typesArr[1];

            } else {
                StringBuilder builder = new StringBuilder();
                for (int i = 0; i < typesArr.length - 2; i++) {
                    builder.append(typesArr[i]).append(", ");
                }
                builder.append(typesArr[typesArr.length - 2]);
                builder.append(" or ");
                builder.append(typesArr[typesArr.length - 1]);
                return builder.toString();
            }
        } else {
            return null;
        }
    }
}
