package com.psddev.util.sitemap;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** Specifies the target field's path within the raw JSON. */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@interface XmlField {

    /** Returns the target field's path within the raw JSON. */
    String path();

    /** Returns true if this field is required. */
    boolean required() default false;

    /** Returns the class that can transform the raw data into a properly fielded object. */
    Class<? extends FieldTransformer> transformer() default DefaultFieldTransformer.class;
}
