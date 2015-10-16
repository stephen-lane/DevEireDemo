package com.deveiredemo.migration;

import com.psddev.dari.db.Query;
import com.psddev.dari.db.State;

import com.deveiredemo.model.Author;

public class AuthorMigrator extends AbstractMigrator {

    @Override
    public String getName() {
        return "Author Migrator";
    }

    @Override
    public String getDescription() {
        return "Migrates authors from the legacy DB.";
    }

    @Override
    protected void doMigrate() {

        Query<Object> foreignQuery = getMainForeignQuery("com.deveiredemo.Author");
        for (Object foreignImage : foreignQuery.iterable(0)) {

            State foreignState = State.getInstance(foreignImage);

            Author author = findOrCreate(Author.class, foreignState);

            State state = author.getState();

            if (overwriteExisting() || state.isNew()) {

                // field mappings
                state.putByPath("name", foreignState.getByPath("name"));
                state.putByPath("email", foreignState.getByPath("email"));

                // add it to the save queue.
                save(author);
            }
        }
    }
}
