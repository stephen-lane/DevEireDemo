package com.deveiredemo.view;

import com.psddev.cms.db.Content;
import com.psddev.cms.view.AbstractViewCreator;
import com.psddev.cms.view.ViewRequest;
import com.psddev.dari.db.Query;
import com.psddev.dari.util.PaginatedResult;
import com.psddev.dari.util.StringUtils;

import com.deveiredemo.model.Constants;
import com.deveiredemo.model.Gallery;
import com.deveiredemo.model.GalleryFallPage;
import com.deveiredemo.view.base.BspMainView;
import com.deveiredemo.view.base.BspOneColumnLayoutView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public interface GalleryFallPageView extends BspOneColumnLayoutView {

    public static final String PAGE_PARAMETER = "p";

    static class FromGalleryFallPage extends AbstractViewCreator<GalleryFallPage> implements GalleryFallPageView {

        @Override
        public Options getOptions() {
            return () -> null;
        }

        @Override
        public BspMainView getMain() {
            return () -> createComponents(request, 1);
        }
    }

    static List<Object> createComponents(ViewRequest request, int page) {

        List<Object> views = new ArrayList<>();

        final int limit = 4;

        if (page < 1 || page > 100) {
            return views;
        }

        long offset = (page - 1) * limit;

        Query<Gallery> query = Query.from(Gallery.class)
                .where("* matches *")
                .sortDescending(Content.PUBLISH_DATE_FIELD);

        PaginatedResult<Gallery> result = query.select(offset, limit);

        Iterator<Class<? extends GalleryGridView.FromGallery>> viewClasses = Arrays.asList(
                GalleryGridView.FromGalleryWithLayout1.class,
                GalleryGridView.FromGalleryWithLayout2.class,
                GalleryGridView.FromGalleryWithLayout3.class,
                GalleryGridView.FromGalleryWithLayout4.class)
                .iterator();

        for (Gallery gallery : result.getItems()) {

            GalleryGridView view = request.createView(viewClasses.next(), gallery);
            if (view != null) {
                views.add(view);
            }
        }

        if (result.hasNext()) {
            views.add(new NextContentView() {
                @Override
                public String getUrl() {
                    return StringUtils.addQueryParameters(Constants.AJAX_ENDPOINT_GALLERY_FALL,
                            PAGE_PARAMETER, page + 1);
                }

                @Override
                public String getLinkText() {
                    return "Load More";
                }
            });
        }

        return views;
    }
}
