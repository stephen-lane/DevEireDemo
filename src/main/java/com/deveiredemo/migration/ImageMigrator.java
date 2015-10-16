package com.deveiredemo.migration;

import com.psddev.dari.db.Query;
import com.psddev.dari.db.State;

import com.deveiredemo.model.BeforeSavePermalinkTrigger;
import com.deveiredemo.model.Image;
import com.deveiredemo.model.Tag;

public class ImageMigrator extends AbstractMigrator {

    @Override
    public String getName() {
        return "Image Migrator";
    }

    @Override
    public String getDescription() {
        return "Migrates images from legacy system without disturbing the"
                + " image binary; just references it. Need to make sure the"
                + " storage settings from the legacy system is configured in"
                + " this system before running. This depends on the Tag"
                + " Migrator to be run first.";
    }

    @Override
    public void doMigrate() {

        Query<Object> foreignQuery = getMainForeignQuery("com.deveiredemo.Image");
        for (Object foreignImage : foreignQuery.iterable(0)) {

            State foreignState = State.getInstance(foreignImage);

            Image image = findOrCreate(Image.class, foreignState);

            State state = image.getState();

            if (overwriteExisting() || state.isNew()) {

                // field mappings
                state.putByPath("name", foreignState.getByPath("name"));
                state.putByPath("title", foreignState.getByPath("headline"));
                state.putByPath("captionOld", foreignState.getByPath("caption"));
                state.putByPath("caption", objectToRichText(foreignState.getByPath("description")));
                state.putByPath("credit", foreignState.getByPath("credit"));
                state.putByPath("altText", foreignState.getByPath("altText"));
                state.putByPath("file", foreignState.getByPath("file"));

                state.putByPath("tags", convertForeignReferenceList(Tag.class, foreignState.getByPath("tags")));

                state.putByPath("includeOnPhotoFall", foreignState.getByPath("showOnPhotoFall"));
                state.putByPath("includeOnTagPage", foreignState.getByPath("showOnTournament"));

                // Don't auto generate permalinks.
                BeforeSavePermalinkTrigger.skipPermalinkTrigger(image);

                // copy permalinks from foreign object.
                setPermalinks(state, foreignState);

                // add it to the save queue.
                save(image);
            }
        }
    }
}
