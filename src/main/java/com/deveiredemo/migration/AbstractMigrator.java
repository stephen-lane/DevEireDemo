package com.deveiredemo.migration;

import com.psddev.cms.db.Content;
import com.psddev.cms.db.Directory;
import com.psddev.cms.db.ToolUser;
import com.psddev.dari.db.Database;
import com.psddev.dari.db.Query;
import com.psddev.dari.db.Recordable;
import com.psddev.dari.db.ReferentialText;
import com.psddev.dari.db.State;
import com.psddev.dari.util.AsyncQueue;
import com.psddev.dari.util.ObjectUtils;
import com.psddev.dari.util.StringUtils;
import com.psddev.dari.util.TypeDefinition;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public abstract class AbstractMigrator implements Migrator {

    private transient Database foreignDatabase;
    private transient AsyncQueue<Recordable> saveQueue;
    private transient AsyncQueue<Recordable> saveUnsafelyQueue;
    private transient AsyncQueue<Recordable> deleteQueue;
    private transient boolean overwriteExisting;
    private transient String additionalForeignQueryCriteria;

    protected transient Logger logger = LoggerFactory.getLogger(getClass());

    protected abstract void doMigrate();

    @Override
    public final void migrate(Database foreignDatabase,
                              boolean overwriteExisting,
                              String additionalForeignQueryCriteria,
                              AsyncQueue<Recordable> saveQueue,
                              AsyncQueue<Recordable> saveUnsafelyQueue,
                              AsyncQueue<Recordable> deleteQueue,
                              Logger logger) {

        this.foreignDatabase = foreignDatabase;
        this.overwriteExisting = overwriteExisting;
        this.additionalForeignQueryCriteria = additionalForeignQueryCriteria;
        this.saveQueue = saveQueue;
        this.saveUnsafelyQueue = saveUnsafelyQueue;
        this.deleteQueue = deleteQueue;
        this.logger = logger;

        doMigrate();
    }

    protected final boolean overwriteExisting() {
        return overwriteExisting;
    }

    protected void saveImmediately(Recordable item) {
        if (item != null) {
            try {
                item.getState().saveImmediately();
            } catch (Exception e) {
                logger.error("Failed to save item [" + item.getState().getId() + " - " + item.getState().getLabel() + "]" + e.getMessage());
            }
        }
    }

    protected void save(Recordable item) {
        if (item != null) {
            if (saveQueue != null) {
                saveQueue.add(item);
            } else {
                item.getState().save();
            }
        }
    }

    protected void saveUnsafely(Recordable item) {
        if (item != null) {
            if (saveUnsafelyQueue != null) {
                saveUnsafelyQueue.add(item);
            } else {
                item.getState().saveUnsafely();
            }
        }
    }

    protected void delete(Recordable item) {
        if (item != null) {
            if (deleteQueue != null) {
                deleteQueue.add(item);
            } else {
                item.getState().delete();
            }
        }
    }

    protected double getDefaultQueryTimeout() {
        return 10.0;
    }

    protected Query<Object> getMainForeignQuery(String foreignGroup) {
        Query<Object> foreignQuery = getForeignQuery(foreignGroup);

        if (!StringUtils.isBlank(additionalForeignQueryCriteria)) {
            foreignQuery.and(additionalForeignQueryCriteria);
        }

        return foreignQuery;
    }

    protected Query<Object> getForeignQuery(String foreignGroup) {
        return Query.fromGroup(foreignGroup)
                .using(foreignDatabase)
                .noCache()
                .master()
                .timeout(getDefaultQueryTimeout());
    }

    protected <T extends MigratedContent> T find(Class<T> returnType, State foreignState) {

        return Query.from(returnType)
                .noCache()
                .master()
                .timeout(getDefaultQueryTimeout())
                .where(MigratedContent.FOREIGN_ID_FIELD + " = ?", foreignState.getId())
                .first();
    }

    protected <T extends MigratedContent> T findOrCreate(Class<T> returnType, State foreignState) {

        T object = find(returnType, foreignState);

        if (object == null) {
            object = TypeDefinition.getInstance(returnType).newInstance();

            object.as(MigratedContent.Data.class).setForeignId(foreignState.getId());
            //object.as(MigratedContent.Data.class).setForeignState(foreignState.getSimpleValues());

            Content.ObjectModification contentMod = object.as(Content.ObjectModification.class);

            contentMod.setPublishUser(findForeignUser(foreignState.getByPath(Content.PUBLISH_USER_FIELD)));
            contentMod.setPublishDate(getDate(foreignState, Content.PUBLISH_DATE_FIELD));

            contentMod.setUpdateUser(findForeignUser(foreignState.getByPath(Content.UPDATE_USER_FIELD)));
            contentMod.setUpdateDate(getDate(foreignState, Content.UPDATE_DATE_FIELD));
        }

        return object;
    }

    protected ToolUser findForeignUser(Object foreignObject) {

        if (foreignObject instanceof Recordable || foreignObject instanceof State) {

            State foreignState = State.getInstance(foreignObject);
            return Query.from(ToolUser.class)
                    .where("email = ?", foreignState.getByPath("email"))
                    .first();
        }

        return null;
    }

    /*
    protected <T> T getForeignReferenceByUniqueKey(Class<T> returnType, State foreignState, String uniqueKeyPath) {
        return Query.from(returnType)
                .where(uniqueKeyPath + " = ?", foreignState.getByPath(uniqueKeyPath))
                .first();
    }
    */

    protected List<Directory.Path> getForeignPaths(State foreignState) {

        List<Directory.Path> paths = new ArrayList<>();

        Map<String, Directory.PathType> pathTypes = foreignState.as(Directory.ObjectModification.class).getPathTypes();

        for (Map.Entry<String, Directory.PathType> entry : pathTypes.entrySet()) {

            String rawPath = entry.getKey();
            Directory.PathType pathType = entry.getValue();

            int slashIndex = rawPath.indexOf('/');
            if (slashIndex >= 0) {

                UUID directoryId = ObjectUtils.to(UUID.class, rawPath.substring(0, slashIndex));
                String path = rawPath.substring(slashIndex);

                Object foreignDirectory = getForeignQuery(Directory.class.getName()).where("_id = ?", directoryId).first();

                if (foreignDirectory != null) {
                    State foreignDirectoryState = State.getInstance(foreignDirectory);

                    String pathPrefix = ObjectUtils.to(String.class, foreignDirectoryState.getByPath("path"));

                    if (pathPrefix != null) {
                        String newPath = StringUtils.removeEnd(pathPrefix, "/") + "/" + StringUtils.removeStart(path, "/");

                        paths.add(new Directory.Path(null, newPath, pathType));
                    }
                }
            }
        }

        return paths;
    }

    protected void setPermalinks(State state, State foreignState) {

        // copy permalinks from foreign object.
        List<Directory.Path> foreignPaths = getForeignPaths(foreignState);
        if (!foreignPaths.isEmpty()) {

            Directory.Data dirData = state.as(Directory.Data.class);

            dirData.clearPaths();

            for (Directory.Path path : foreignPaths) {
                dirData.addPath(path.getSite(), path.getPath(), path.getType());
            }
        }
    }

    protected Date getDate(State foreignState, String path) {
        return ObjectUtils.to(Date.class, foreignState.getByPath(path));
    }

    protected <T extends MigratedContent> List<T> convertForeignReferenceList(Class<T> returnType, Object foreignList) {

        List<T> list = new ArrayList<>();

        if (foreignList instanceof List) {
            for (Object foreignListItem : (List<?>) foreignList) {

                State foreignListItemState = State.getInstance(foreignListItem);

                if (foreignListItemState != null) {
                    T item = find(returnType, foreignListItemState);
                    if (item != null) {
                        list.add(item);
                    }
                }
            }
        }

        return !list.isEmpty() ? list : null;
    }

    protected String objectToRichText(Object object) {

        if (object instanceof ReferentialText) {
            return referentialTextToRichText((ReferentialText) object);

        } else if (object instanceof List) {
            ReferentialText refText = new ReferentialText();
            ((List<?>) object).stream().filter((item) -> item instanceof String).forEach(refText::add);
            return referentialTextToRichText(refText);

        } else if (object instanceof String) {
            return (String) object;

        } else {
            return null;
        }
    }

    private String referentialTextToRichText(ReferentialText refText) {

        StringBuilder builder = new StringBuilder();

        refText.forEach((item) -> {
            if (item instanceof String) {

                String text = (String) item;

                if (!StringUtils.isBlank(text) && !"<br />".equals(text)) {
                    builder.append(text);
                }
            }
        });

        return builder.length() > 0 ? builder.toString() : null;
    }
}
