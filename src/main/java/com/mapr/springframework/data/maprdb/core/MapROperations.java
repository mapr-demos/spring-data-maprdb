package com.mapr.springframework.data.maprdb.core;

import com.mapr.db.Table;
import org.ojai.store.Connection;
import org.ojai.store.DocumentStore;
import org.ojai.store.Query;
import org.ojai.store.QueryCondition;

import java.util.List;
import java.util.Optional;

public interface MapROperations {

    Connection getConnection();

    <T> Table createTable(Class<T> entityClass);

    Table createTable(final String tableName);

    <T> void dropTable(Class<T> entityClass);

    void dropTable(final String tableName);

    <T> boolean tableExists(Class<T> entityClass);

    boolean tableExists(final String tableName);

    <T> DocumentStore getStore(Class<T> entityClass);

    DocumentStore getStore(final String storeName);

    <T> Optional<T> findById(Object id, Class<T> entityClass);

    <T> Optional<T> findById(Object id, Class<T> entityClass, final String tableName);

    <T> List<T> findAll(Class<T> entityClass);

    <T> List<T> findAll(Class<T> entityClass, final String tableName);

    <T> T insert(T objectToSave);

    <T> T insert(T objectToSave, final String tableName);

    <T> List<T> insert(Iterable<T> objectsToSave);

    <T> T save(T objectToSave);

    <T> T save(T objectToSave, final String tableName);

    <T> List<T> save(Iterable<T>  objectsToSave);

    void remove(Object object);

    void remove(Object object, final String tableName);

    <T> void removeById(Object id, Class<T> entityClass);

    <T> void removeById(Object id, Class<T> entityClass, final String tableName);

    <T> void remove(Iterable<T> objectsToDelete);

    <T> void removeAll(Class<T> entityClass);

    <T> long count(Class<T> entityClass);

    <T> List<T> execute(QueryCondition queryCondition, Class<T> entityClass);

    <T> List<T> execute(Query query, Class<T> entityClass);

}
