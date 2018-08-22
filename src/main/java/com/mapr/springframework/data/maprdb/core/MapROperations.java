package com.mapr.springframework.data.maprdb.core;

import java.util.Optional;

public interface MapROperations {

    <T> Optional<T> find(Object id, Class<T> entityClass);

    <T> Iterable<T> findAll(Class<T> entityClass);

    <T> Iterable<T> find(final Iterable<? extends Object> ids, final Class<T> entityClass);

}
