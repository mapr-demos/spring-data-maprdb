package com.mapr.springframework.data.maprdb.core.mapping;

import org.springframework.data.annotation.ReadOnlyProperty;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD })
@ReadOnlyProperty
public @interface MapRId {

}
