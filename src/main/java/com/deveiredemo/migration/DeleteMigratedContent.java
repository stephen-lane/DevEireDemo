package com.deveiredemo.migration;

import com.psddev.dari.db.Query;

public class DeleteMigratedContent extends AbstractMigrator {

    @Override
    public String getName() {
        return "Delete Migrated Content";
    }

    @Override
    public String getDescription() {
        return "Deletes all migrated content from the database.";
    }

    @Override
    protected void doMigrate() {

        for (MigratedContent content : Query.from(MigratedContent.class).iterable(0)) {
            if (content.as(MigratedContent.Data.class).getForeignId() != null) {
                delete(content);
            }
        }
    }
}
