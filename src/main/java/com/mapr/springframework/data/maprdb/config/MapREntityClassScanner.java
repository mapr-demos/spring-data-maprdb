package com.mapr.springframework.data.maprdb.config;

import com.mapr.springframework.data.maprdb.core.mapping.Document;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;

public class MapREntityClassScanner {

    @SuppressWarnings("unchecked")
    private static final Class<? extends Annotation>[] ENTITY_ANNOTATIONS = new Class[] { Document.class};

    @SuppressWarnings("unchecked")
    private static final Class<? extends Annotation>[] ADDITIONAL_ANNOTATIONS = new Class[] { TypeAlias.class };

    public static Set<Class<?>> scanForEntities(final String... basePackages) throws ClassNotFoundException {
        final Set<Class<?>> entities = new HashSet<>();
        for (final String basePackage : basePackages) {
            entities.addAll(scanForEntities(basePackage));
        }
        return entities;
    }

    public static Set<Class<?>> scanForEntities(final String basePackage) throws ClassNotFoundException {
        final Set<Class<?>> entities = new HashSet<>();
        if (StringUtils.hasText(basePackage)) {
            final ClassPathScanningCandidateComponentProvider componentProvider = new ClassPathScanningCandidateComponentProvider(
                    false);
            for (final Class<? extends Annotation> annotationType : ENTITY_ANNOTATIONS) {
                componentProvider.addIncludeFilter(new AnnotationTypeFilter(annotationType));
            }
            for (final Class<? extends Annotation> annotationType : ADDITIONAL_ANNOTATIONS) {
                componentProvider.addIncludeFilter(new AnnotationTypeFilter(annotationType));
            }
            for (final BeanDefinition definition : componentProvider.findCandidateComponents(basePackage)) {
                entities.add(ClassUtils.forName(definition.getBeanClassName(), null));
            }
        }
        return entities;
    }

}
