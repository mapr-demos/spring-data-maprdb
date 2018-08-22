package com.mapr.springframework.data.maprdb.core.mapping;

import org.springframework.core.annotation.AliasFor;
import org.springframework.data.annotation.Persistent;

import java.lang.annotation.*;

@Persistent
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
public @interface Document {

    @AliasFor("collection")
    String value() default "";

    @AliasFor("value")
    String collection() default "";

    String language() default "";

}