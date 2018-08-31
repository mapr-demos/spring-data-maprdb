package com.mapr.springframework.data.maprdb.repository;

import org.springframework.data.annotation.QueryAnnotation;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD, ElementType.ANNOTATION_TYPE })
@Documented
@QueryAnnotation
public @interface Query {

    String value() default "";

}
