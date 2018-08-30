package com.mapr.springframework.data.maprdb.core.query;

import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.query.QueryMethod;

import java.lang.reflect.Method;

public class MapRQueryMethod extends QueryMethod {

    public MapRQueryMethod(Method method, RepositoryMetadata metadata, ProjectionFactory factory) {
        super(method, metadata, factory);
    }
}
