package com.mapr.springframework.data.maprdb.repository;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD, ElementType.ANNOTATION_TYPE })
@Documented
@Query(count = true)
public @interface CountQuery {

    @AliasFor(annotation = Query.class)
    String value() default "";

}
