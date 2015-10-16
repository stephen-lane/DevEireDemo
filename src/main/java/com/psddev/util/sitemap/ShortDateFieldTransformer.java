package com.psddev.util.sitemap;

import org.joda.time.DateTime;

import java.util.Date;

class ShortDateFieldTransformer implements FieldTransformer {

    @Override
    public Object transform(Object object) {
        if (object instanceof Date) {
            return new DateTime(object).toString("yyyy-MM-dd");
        } else {
            return null;
        }
    }
}
