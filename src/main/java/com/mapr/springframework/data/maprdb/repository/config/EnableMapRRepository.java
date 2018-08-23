package com.mapr.springframework.data.maprdb.repository.config;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.mapr.springframework.data.maprdb.repository.support.MapRRepositoryFactoryBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.data.repository.query.QueryLookupStrategy.Key;


@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import(MapRRepositoriesRegistrar.class)
public @interface EnableMapRRepository {

    String[] value() default {};

    String[] basePackages() default {};

    Class<?>[] basePackageClasses() default {};

    ComponentScan.Filter[] includeFilters() default {};

    ComponentScan.Filter[] excludeFilters() default {};

    String repositoryImplementationPostfix() default "Impl";

    Class<?> repositoryFactoryBeanClass() default MapRRepositoryFactoryBean.class;

    String namedQueriesLocation() default "";

    Key queryLookupStrategy() default Key.CREATE_IF_NOT_FOUND;

}
