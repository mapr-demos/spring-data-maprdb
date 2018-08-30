package com.mapr.springframework.data.maprdb.repository.query;

import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.query.QueryMethod;
import org.springframework.util.ConcurrentReferenceHashMap;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Optional;

public class MapRQueryMethod extends QueryMethod {

    private final Method method;
    private final Map<Class<? extends Annotation>, Optional<Annotation>> annotationCache;

    public MapRQueryMethod(Method method, RepositoryMetadata metadata, ProjectionFactory factory) {
        super(method, metadata, factory);

        this.method = method;
        this.annotationCache = new ConcurrentReferenceHashMap<>();
    }
}
