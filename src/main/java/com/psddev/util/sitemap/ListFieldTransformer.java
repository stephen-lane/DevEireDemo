package com.psddev.util.sitemap;

import java.util.ArrayList;
import java.util.List;

class ListFieldTransformer implements FieldTransformer {

    @Override
    public Object transform(Object object) {

        if (object instanceof Iterable) {

            List<Object> items = new ArrayList<>();

            for (Object objectItem : (Iterable<?>) object) {

                Object item;
                if (objectItem instanceof String) {
                    item = SiteMapUtils.toXmlJsonMap(objectItem);

                } else {
                    item = new DefaultFieldTransformer().transform(objectItem);
                }

                if (item != null) {
                    items.add(item);
                }
            }

            return items;
        } else {
            return null;
        }
    }
}
