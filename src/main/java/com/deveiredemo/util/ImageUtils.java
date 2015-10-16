package com.deveiredemo.util;

import com.psddev.cms.db.StandardImageSize;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility methods surrounding images in Brightspot.
 */
public final class ImageUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(ImageUtils.class);

    private ImageUtils() {
    }

    /**
     * Finds the StandardImageSize for the given internalName. Logs a warning
     * when it cannot find the specified size.
     *
     * @param internalName the internal name of the StandardImageSize.
     * @return the StandardImageSize for the given internalName.
     */
    public static StandardImageSize findStandardSize(String internalName) {
        return findStandardSizeWithAspectRatio(internalName, 0, 0, false, false, false);
    }

    /**
     * Finds the StandardImageSize for the given internalName. Logs a warning
     * when it cannot find the specified size or it doesn't match the expected
     * dimensions.
     *
     * @param internalName the internal name of the StandardImageSize.
     * @param width expected width
     * @param height expected height
     * @param returnNullOnMismatch true if null value should be returned on mismatch, false otherwise.
     * @return the StandardImageSize for the given internalName.
     */
    public static StandardImageSize findStandardSizeWithDimensions(String internalName, int width, int height, boolean returnNullOnMismatch) {
        return findStandardSizeWithAspectRatio(internalName, width, height, false, true, returnNullOnMismatch);
    }

    /**
     * Finds the StandardImageSize for the given internalName. Logs a warning
     * when it cannot find the specified size or it doesn't match the expected
     * dimensions.
     *
     * @param internalName the internal name of the StandardImageSize.
     * @param width width portion of the expected aspect ratio.
     * @param height height portion of the expected aspect ratio.
     * @param returnNullOnMismatch true if null value should be returned on mismatch, false otherwise.
     * @return the StandardImageSize for the given internalName.
     */
    public static StandardImageSize findStandardSizeWithAspectRatio(String internalName, int width, int height, boolean returnNullOnMismatch) {
        return findStandardSizeWithAspectRatio(internalName, width, height, true, false, returnNullOnMismatch);
    }

    private static StandardImageSize findStandardSizeWithAspectRatio(String internalName, int width, int height, boolean matchRatio, boolean matchDimensions, boolean returnNullOnMismatch) {

        for (StandardImageSize sis : StandardImageSize.findAll()) {

            if (sis.getInternalName().equals(internalName)) {

                int sisWidth = sis.getWidth();
                int sisHeight = sis.getHeight();

                if (matchRatio) {

                    double sisRatio = (double) sisWidth / sisHeight;
                    double ratio = (double) width / height;

                    if (sisRatio != ratio) {

                        // TODO: Print a prettier ratio label.
                        String sisRatioLabel = String.valueOf(sisRatio);
                        String ratioLabel = String.valueOf(ratio);

                        LOGGER.warn("Found StandardImageSize [" + internalName + "] with mismatched aspect ratio. Expected ["
                                + ratioLabel + "] but got [" + sisRatioLabel + "] instead. "
                                + "Make sure it has been configured correctly in CMS --> Admin --> Settings.");
                    }

                } else if (matchDimensions) {

                    if (width != sisWidth || height != sisHeight) {

                        LOGGER.warn("Found StandardImageSize [" + internalName + "] with mismatched dimensions. Expected ["
                                + width + "x" + height + "] but got [" + sisWidth + "x" + sisHeight + "] instead. "
                                + "Make sure it has been configured correctly in CMS --> Admin --> Settings.");

                        if (returnNullOnMismatch) {
                            return null;
                        }
                    }
                }

                return sis;
            }
        }

        if (internalName != null) {
            LOGGER.warn("Cannot find StandardImageSize for internalName ["
                    + internalName + "]. Make sure it has been configured in"
                    + " CMS --> Admin --> Settings.");
        }

        return null;
    }

    /**
     * Determines if 2 sets of dimensions have effectively the same aspect ratio.
     *
     * @param width1 dimension 1 width
     * @param height1 dimension 1 height
     * @param width2 dimension 2 width
     * @param height2 dimension 2 height
     * @return true if the dimensions have the same aspect ratio.
     */
    public static boolean isRatioEquivalent(int width1, int height1, int width2, int height2) {
        if (width1 <= 0 || height1 <= 0 || width2 <= 0 || height2 <= 0) {
            throw new IllegalArgumentException("Dimensions must all be positive integers!");
        }
        double ratio1 = (double) width1 / (double) height1;
        double ratio2 = (double) width2 / (double) height2;
        return ratio1 == ratio2;
    }
}
