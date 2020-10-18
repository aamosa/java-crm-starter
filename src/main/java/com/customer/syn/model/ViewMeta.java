package com.customer.syn.model;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static com.customer.syn.model.FormInputType.TEXT;

@Retention(RUNTIME)
@Target(FIELD)
public @interface ViewMeta {
    
    int order() default 99;

    boolean formField() default true;

    FormInputType type() default TEXT;

    String collectionType() default "";
}
