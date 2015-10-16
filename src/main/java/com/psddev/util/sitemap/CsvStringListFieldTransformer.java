package com.psddev.util.sitemap;

import com.psddev.dari.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

class CsvStringListFieldTransformer implements FieldTransformer {

    @Override
    public Object transform(Object object) {

        if (object instanceof Iterable) {

            List<String> items = new ArrayList<>();
            for (Object item : (Iterable<?>) object) {
                items.add(FieldTransformer.cleanString(item.toString()));
            }
            return StringUtils.join(items, ", ");

        } else {
            return null;
        }
    }
}
