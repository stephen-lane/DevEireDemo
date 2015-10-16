package com.deveiredemo.migration;

import com.psddev.dari.db.Query;
import com.psddev.dari.db.State;
import com.psddev.dari.util.ObjectUtils;

import com.deveiredemo.model.HtmlEmbedVideo;
import com.deveiredemo.model.Tag;
import com.deveiredemo.model.YouTubeVideo;

public class VideoMigrator extends AbstractMigrator {

    @Override
    public String getName() {
        return "Video Migrator";
    }

    @Override
    public String getDescription() {
        return "Migrate video objects from legacy DB splitting them into 2 types: "
                + "YouTube Video & HTML Embed Video";
    }

    @Override
    protected void doMigrate() {

        Query<Object> foreignQuery = getMainForeignQuery("com.deveiredemo.Video");
        for (Object foreignObject : foreignQuery.iterable(0)) {

            State foreignState = State.getInstance(foreignObject);

            if (!ObjectUtils.isBlank(foreignState.getByPath("videoId"))) {

                YouTubeVideo youTubeVideo = findOrCreate(YouTubeVideo.class, foreignState);
                State state = youTubeVideo.getState();

                if (overwriteExisting() || state.isNew()) {

                    // field mappings
                    state.putByPath("videoIdentifier", foreignState.getByPath("videoId"));
                    state.putByPath("title", foreignState.getByPath("name"));
                    state.putByPath("thumbnail", foreignState.getByPath("thumbnail"));
                    state.putByPath("description", objectToRichText(foreignState.getByPath("description")));

                    // copy tags
                    state.putByPath("tags", convertForeignReferenceList(Tag.class, foreignState.getByPath("tags")));

                    // copy permalinks from foreign object.
                    setPermalinks(state, foreignState);

                    // add to save queue
                    save(youTubeVideo);
                }
            } else {

                HtmlEmbedVideo htmlEmbedVideo = findOrCreate(HtmlEmbedVideo.class, foreignState);
                State state = htmlEmbedVideo.getState();

                if (overwriteExisting() || state.isNew()) {

                    // field mappings
                    state.putByPath("name", foreignState.getByPath("name"));
                    state.putByPath("title", foreignState.getByPath("headline"));
                    state.putByPath("description", foreignState.getByPath("caption"));
                    state.putByPath("embedCode", foreignState.getByPath("embeddedVideo"));
                    state.putByPath("thumbnail", foreignState.getByPath("thumbnail"));

                    // copy tags
                    state.putByPath("tags", convertForeignReferenceList(Tag.class, foreignState.getByPath("tags")));

                    // copy permalinks from foreign object.
                    setPermalinks(state, foreignState);

                    // add to save queue
                    save(htmlEmbedVideo);
                }
            }
        }
    }
}
