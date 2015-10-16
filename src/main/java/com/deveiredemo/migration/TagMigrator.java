package com.deveiredemo.migration;

import com.psddev.dari.db.Query;
import com.psddev.dari.db.State;
import com.psddev.dari.util.ObjectUtils;

import com.deveiredemo.model.Tag;

public class TagMigrator extends AbstractMigrator {

    @Override
    public String getName() {
        return "Tag Migrator";
    }

    @Override
    public String getDescription() {
        return "Migrates tags from the legacy system.";
    }

    @Override
    public void doMigrate() {
        migrateTagChildren(null);
    }

    private void migrateTagChildren(Tag parent) {

        Query<Object> query = getMainForeignQuery("com.deveiredemo.Tag");

        if (parent != null) {
            query.where("parent = ?", parent.as(MigratedContent.Data.class).getForeignId());
        } else {
            query.where("parent = missing");
        }

        for (Object foreignObject : query.iterable(0)) {

            State foreignState = State.getInstance(foreignObject);

            Tag tag = findOrCreate(Tag.class, foreignState);

            if (overwriteExisting() || tag.getState().isNew()) {

                tag.setName(ObjectUtils.to(String.class, foreignState.getByPath("name")));
                tag.setParent(parent);

                saveImmediately(tag);
            }

            migrateTagChildren(tag);
        }
    }
}
