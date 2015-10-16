package com.deveiredemo.migration;

import com.psddev.dari.db.Query;
import com.psddev.dari.db.State;

import com.deveiredemo.model.Gallery;
import com.deveiredemo.model.GallerySlide;
import com.deveiredemo.model.Image;
import com.deveiredemo.model.Tag;

import java.util.ArrayList;
import java.util.List;

public class GalleryMigrator extends AbstractMigrator {

    @Override
    public String getName() {
        return "Gallery Migrator";
    }

    @Override
    public String getDescription() {
        return "Migrates galleries from the legacy system.";
    }

    @Override
    protected void doMigrate() {

        Query<Object> foreignQuery = getMainForeignQuery("com.deveiredemo.Gallery");

        for (Object foreignObject : foreignQuery.iterable(0)) {

            State foreignState = State.getInstance(foreignObject);

            Gallery gallery = findOrCreate(Gallery.class, foreignState);

            State state = gallery.getState();

            if (overwriteExisting() || state.isNew()) {

                state.putByPath("name", foreignState.getByPath("name"));
                state.putByPath("headline", foreignState.getByPath("headline"));
                state.putByPath("description", objectToRichText(foreignState.getByPath("description")));

                state.putByPath("tags", convertForeignReferenceList(Tag.class, foreignState.getByPath("tags")));

                // copy embedded slides
                List<GallerySlide> slides = new ArrayList<>();

                Object foreignSlides = foreignState.getByPath("slides");
                if (foreignSlides instanceof Iterable) {

                    for (Object foreignSlide : (Iterable<?>) foreignSlides) {

                        State foreignSlideState = State.getInstance(foreignSlide);

                        Object foreignSlideImage = foreignSlideState.getByPath("image");
                        State foreignSlideImageState = State.getInstance(foreignSlideImage);

                        if (foreignSlideImageState != null) {

                            Image image = find(Image.class, foreignSlideImageState);
                            if (image != null) {

                                GallerySlide slide = new GallerySlide();

                                State slideState = State.getInstance(slide);

                                slideState.putByPath("image", image);
                                slideState.putByPath("captionOverride", foreignSlideState.getByPath("caption"));

                                slides.add(slide);
                            }
                        }
                    }
                }

                state.putByPath("slides", slides);

                // copy permalinks from foreign object.
                setPermalinks(state, foreignState);

                // add it to the save queue.
                save(gallery);
            }
        }
    }
}
