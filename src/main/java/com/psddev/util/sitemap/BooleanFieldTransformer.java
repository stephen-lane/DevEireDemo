package com.psddev.util.sitemap;

class BooleanFieldTransformer implements FieldTransformer {

    @Override
    public Object transform(Object object) {
        if (object instanceof Boolean) {
            return ((boolean) object) ? "yes" : "no";

        } else {
            return null;
        }
    }
}
