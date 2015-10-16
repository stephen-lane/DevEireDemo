package com.psddev.util.sitemap;

import java.util.Date;

class DefaultFieldTransformer implements FieldTransformer {

    @Override
    public Object transform(Object object) {
        if (object != null) {

            if (object instanceof Boolean) {
                return new BooleanFieldTransformer().transform(object);

            } else if (object instanceof Date) {
                return new LongDateFieldTransformer().transform(object);

            } else if (object instanceof Iterable) {
                return new ListFieldTransformer().transform(object);

            } else if (object instanceof ComplexElement) {
                return SiteMapUtils.toXmlJsonMap(object);

            } else {
                return FieldTransformer.cleanString(object.toString());
            }
        } else {
            return null;
        }
    }
}
