package com.mapr.springframework.data.maprdb.core;

import java.util.Optional;

public class MapRTemplate implements MapROperations {
    private final String databaseName;

    public MapRTemplate(String databaseName) {
        this.databaseName = databaseName;
    }

    @Override
    public <T> Optional<T> find(Object id, Class<T> entityClass) {
        return Optional.empty();
    }

    @Override
    public <T> Iterable<T> findAll(Class<T> entityClass) {
        return null;
    }

    @Override
    public <T> Iterable<T> find(Iterable<?> ids, Class<T> entityClass) {
        return null;
    }

    private String getPath(String className) {
        return String.format("/%s/%s", databaseName, className);
    }
}
