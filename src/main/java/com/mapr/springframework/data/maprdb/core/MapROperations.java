package com.mapr.springframework.data.maprdb.core;

import com.mapr.db.Table;

import java.util.Collection;
import java.util.Optional;

public interface MapROperations {

    <T> Table createTable(Class<T> entityClass);

    Table createTable(final String tableName);

    <T> Table getTable(Class<T> entityClass);

    Table getTable(final String tableName);

    <T> void dropTable(Class<T> entityClass);

    void dropTable(final String tableName);

    <T> boolean tableExists(Class<T> entityClass);

    boolean tableExists(final String tableName);

    <T> Optional<T> findById(Object id, Class<T> entityClass);

    <T> Optional<T> findById(Object id, Class<T> entityClass, final String tableName);

    <T> Collection<T> findAll(Class<T> entityClass);

    <T> Collection<T> findAll(Class<T> entityClass, final String tableName);

    <T> T insert(T objectToSave);

    <T> T insert(T objectToSave, final String tableName);

    <T> Collection<T> insertAll(Collection<? extends T> objectsToSave);

    <T> T save(T objectToSave);

    <T> T save(T objectToSave, final String tableName);

    <T> Collection<T> save(Collection<? extends T> objectsToSave);

    void remove(Object object);

    void remove(Object object, final String tableName);

    <T> void remove(Object id, Class<T> entityClass);

    <T> void remove(Object id, Class<T> entityClass, final String tableName);

    <T> long count(Class<T> entityClass);

}
