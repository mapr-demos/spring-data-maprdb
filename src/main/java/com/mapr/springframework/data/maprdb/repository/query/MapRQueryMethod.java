package com.mapr.springframework.data.maprdb.repository.query;

import com.mapr.springframework.data.maprdb.repository.Query;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.query.QueryMethod;
import org.springframework.util.ConcurrentReferenceHashMap;
import org.springframework.util.StringUtils;

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

    public boolean hasAnnotatedQuery() {
        return getQueryAnnotationValue().isPresent();
    }

    public String getAnnotatedQuery() {
        return getQueryAnnotationValue().orElse(null);
    }

    public Query getQueryAnnotation() {
        return AnnotatedElementUtils.findMergedAnnotation(method, Query.class);
    }

    private Optional<String> getQueryAnnotationValue() {
        return Optional.ofNullable(getQueryAnnotation())
                .map(Query::value)
                .filter(StringUtils::hasText);
    }
}
