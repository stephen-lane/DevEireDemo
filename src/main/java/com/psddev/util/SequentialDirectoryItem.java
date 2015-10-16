package com.psddev.util;

import com.psddev.cms.db.Directory;
import com.psddev.cms.db.Site;
import com.psddev.cms.db.ToolUi;
import com.psddev.dari.db.Modification;
import com.psddev.dari.db.ObjectIndex;
import com.psddev.dari.db.Recordable;
import com.psddev.dari.db.State;
import com.psddev.dari.util.ObjectUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * Prevents unique constraint validation exceptions on permalinks when creating
 * content by automatically appending an auto-incremented number to the end
 * of the generated URL to ensure uniqueness on publish.
 *
 * Implementing classes may override {@link #shouldIncrementDirectoryItem()} to
 * determine if the {@link com.psddev.dari.db.Record#onDuplicate(com.psddev.dari.db.ObjectIndex)}
 * logic should actually run. The default implementation returns true, and
 * therefore always runs.
 */
public interface SequentialDirectoryItem extends Directory.Item, Recordable {

    /**
     * Creates the permalink appropriate for the given {@code site}.
     *
     * @param site the site the permalink is created in.
     * @return the permalink path for this object.
     */
    String createSequentialPermalink(Site site);

    /**
     * @return true if the directory item should be incremented on a duplicate
     *      error, false otherwise. Default implementation returns true.
     */
    default boolean shouldIncrementDirectoryItem() {
        return true;
    }

    /**
     * Creates the permalink appropriate for the given {@code site} and appends
     * the directory item index to the end.
     *
     * @param site the site the permalink is created in.
     * @return the permalink path for this object.
     */
    default String createPermalink(Site site) {

        String permalink = createSequentialPermalink(site);
        if (permalink != null) {

            // TODO: Need to handle /* and /** paths
            Integer itemIndex = as(Data.class).getDirectoryItemIndex();
            if (itemIndex != null) {
                permalink += itemIndex;
            }
        }

        return permalink;
    }

    /**
     * Modification of SequentialDirectoryItem that stores the auto-increment
     * directory item index for the original object.
     */
    public static final class Data extends Modification<SequentialDirectoryItem> {

        @Indexed
        @ToolUi.Hidden
        private Integer directoryItemIndex;

        /**
         * @return the auto-increment directory item index .
         */
        public Integer getDirectoryItemIndex() {
            return directoryItemIndex;
        }

        private transient Set<Directory.Path> originalDuplicatePaths = null;

        @Override
        protected final boolean onDuplicate(ObjectIndex index) {

            if (getOriginalObject().shouldIncrementDirectoryItem()) {

                if (index != null) {
                    String field = index.getField();

                    if (Directory.PATHS_FIELD.equals(field)) {

                        Set<Directory.Path> duplicatePaths = as(Directory.Data.class).getPaths();
                        if (ObjectUtils.isBlank(duplicatePaths)) {
                            return false;
                        }
                        duplicatePaths = new HashSet<>(duplicatePaths);

                        if (directoryItemIndex == null) {
                            directoryItemIndex = 1;

                        } else {
                            directoryItemIndex++;
                        }

                        if (originalDuplicatePaths == null) {
                            originalDuplicatePaths = new HashSet<>(duplicatePaths);
                        }

                        Set<Directory.Path> originalPaths = new HashSet<>();
                        for (Directory.Path duplicatePath : duplicatePaths) {
                            Object originalObject = Directory.Static.findByPath(duplicatePath.getSite(), duplicatePath.getPath());
                            if (originalObject != null) {

                                Set<Directory.Path> originalObjectPaths = State.getInstance(originalObject).as(Directory.Data.class).getPaths();
                                if (originalObjectPaths != null) {
                                    originalPaths.addAll(originalObjectPaths);
                                }
                            }
                        }

                        boolean incrementedDirectoryItems = false;

                        List<Directory.Path> newPaths = new ArrayList<>();

                        for (Directory.Path duplicatePath : duplicatePaths) {

                            if (originalPaths.contains(duplicatePath)) {

                                String duplicatePathString = duplicatePath.getPath();

                                for (Directory.Path originalDuplicatePath : originalDuplicatePaths) {

                                    String originalDuplicatePathString = originalDuplicatePath.getPath();

                                    if (duplicatePathString.startsWith(originalDuplicatePathString)) {

                                        String pathDiff = duplicatePathString.substring(originalDuplicatePathString.length());

                                        boolean isDerivativePath = pathDiff.isEmpty() || ObjectUtils.to(Integer.class, pathDiff) != null;
                                        if (isDerivativePath) {
                                            newPaths.add(new Directory.Path(
                                                    originalDuplicatePath.getSite(),
                                                    originalDuplicatePath.getPath() + directoryItemIndex,
                                                    originalDuplicatePath.getType()));
                                            incrementedDirectoryItems = true;
                                        }
                                    }
                                }

                            } else {
                                newPaths.add(duplicatePath);
                            }
                        }

                        if (incrementedDirectoryItems) {

                            as(Directory.Data.class).clearPaths();
                            for (Directory.Path newPath : newPaths) {
                                as(Directory.Data.class).addPath(newPath.getSite(), newPath.getPath(), newPath.getType());
                            }

                            return true;
                        }
                    }
                }

            }

            return false;
        }
    }
}
