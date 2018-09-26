package com.mapr.springframework.data.maprdb.core;

import com.mapr.db.MapRDB;
import com.mapr.db.Table;

import com.mapr.springframework.data.maprdb.core.mapping.Document;
import com.mapr.springframework.data.maprdb.core.mapping.MapRJsonConverter;
import org.ojai.DocumentStream;
import org.ojai.store.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class MapRTemplate implements MapROperations {

    private final static Logger LOGGER = LoggerFactory.getLogger(MapRTemplate.class);

    private final String databaseName;
    private Connection connection;
    private MapRJsonConverter converter;

    public MapRTemplate(final String databaseName) {
        this(databaseName, DriverManager.getConnection("ojai:mapr:"));
    }

    protected MapRTemplate(final String databaseName, Connection connection) {
        converter = new MapRJsonConverter();
        this.databaseName = databaseName;
        this.connection = connection;
    }

    @Override
    public Connection getConnection() {
        return connection;
    }

    @Override
    public <T> Table createTable(Class<T> entityClass) {
        return createTable(getTablePath(entityClass));
    }

    @Override
    public Table createTable(final String tableName) {
        return MapRDB.createTable(getPath(tableName));
    }

    @Override
    public <T> void dropTable(Class<T> entityClass) {
        dropTable(getTablePath(entityClass));
    }

    @Override
    public void dropTable(final String tableName) {
        MapRDB.deleteTable(getPath(tableName));
    }

    @Override
    public <T> boolean tableExists(Class<T> entityClass) {
        return tableExists(getTablePath(entityClass));
    }

    @Override
    public boolean tableExists(final String tableName) {
        return MapRDB.tableExists(getPath(tableName));
    }

    @Override
    public <T> DocumentStore getStore(Class<T> entityClass) {
        return getStore(getTablePath(entityClass));
    }

    @Override
    public DocumentStore getStore(String storeName) {
        return connection.getStore(getPath(storeName));
    }

    @Override
    public <T> Optional<T> findById(Object id, Class<T> entityClass) {
        return findById(id, entityClass, getTablePath(entityClass));
    }

    @Override
    public <T> Optional<T> findById(Object id, Class<T> entityClass, final String tableName) {
        org.ojai.Document document = getStore(entityClass).findById(id.toString());
        return Optional.ofNullable(document != null ? converter.toObject(document.toString(), entityClass) : null);
    }

    @Override
    public <T> List<T> findAll(Class<T> entityClass) {
        return findAll(entityClass, getTablePath(entityClass));
    }

    @Override
    public <T> List<T> findAll(Class<T> entityClass, final String tableName) {
        return execute(connection.newQuery().build(), entityClass, tableName);
    }

    @Override
    public <T> T insert(T objectToSave) {
        return insert(objectToSave, getTablePath(objectToSave.getClass()));
    }

    @Override
    public <T> T insert(T objectToSave, final String tableName) {
        DocumentStore store = getStore(tableName);

        org.ojai.Document document = connection.newDocument(converter.toJson(objectToSave));

        store.insert(document);

        return (T) converter.toObject(document.toString(), objectToSave.getClass());
    }

    @Override
    public <T> List<T> insert(Iterable<T> objectsToSave) {
        return StreamSupport.stream(objectsToSave.spliterator(), false).map(this::insert).collect(Collectors.toList());
    }

    @Override
    public <T> T save(T objectToSave) {
        return save(objectToSave, getTablePath(objectToSave.getClass()));
    }

    @Override
    public <T> T save(T objectToSave, final String tableName) {
        DocumentStore store = getStore(tableName);

        org.ojai.Document document = connection.newDocument(converter.toJson(objectToSave));

        store.insertOrReplace(document);
        store.flush();

        return (T) converter.toObject(document.toString(), objectToSave.getClass());
    }

    @Override
    public <T> List<T> save(Iterable<T> objectsToSave) {
        return StreamSupport.stream(objectsToSave.spliterator(), false).map(this::save).collect(Collectors.toList());
    }

    @Override
    public void remove(Object object) {
        remove(object, getTablePath(object.getClass()));
    }

    @Override
    public void remove(Object object, final String tableName) {
        DocumentStore store = getStore(tableName);
        store.delete(connection.newDocument(converter.toJson(object)));
        store.flush();
    }

    @Override
    public <T> void removeById(Object id, Class<T> entityClass) {
        removeById(id, entityClass, getTablePath(entityClass));
    }

    @Override
    public <T> void removeById(Object id, Class<T> entityClass, final String tableName) {
        DocumentStore store = getStore(tableName);
        store.delete(id.toString());
        store.flush();
    }

    @Override
    public <T> long count(Class<T> entityClass) {
        LOGGER.warn("Count method use iterations over all records, so it is not recommended using it");

        Query query = connection.newQuery().select("_id").build();
        DocumentStream rs = getStore(entityClass).find(query);
        Iterator<org.ojai.Document> itrs = rs.iterator();

        long totalRow = 0;
        while (itrs.hasNext()) {
            itrs.next();
            totalRow++;
        }

        rs.close();

        return totalRow;
    }

    @Override
    public <T> List<T> execute(QueryCondition queryCondition, Class<T> entityClass) {
        return execute(connection.newQuery().where(queryCondition).build(), entityClass);
    }

    @Override
    public <T> List<T> execute(Query query, Class<T> entityClass) {
        return execute(query, entityClass, getTablePath(entityClass));
    }

    private  <T> List<T> execute(Query query, Class<T> entityClass, String tableName) {
        return convertDocumentStreamToIterable(getStore(tableName).find(query), entityClass);
    }

    private <T> List<T> convertDocumentStreamToIterable(DocumentStream documentStream, Class<T> entityClass) {
        List<T> resultCollection = new LinkedList<>();

        documentStream.forEach(d -> resultCollection.add(converter.toObject(d.toString(), entityClass)));

        documentStream.close();

        return resultCollection;
    }

    private String getPath(String className) {
        if(databaseName.startsWith("/"))
            return String.format("%s%s", databaseName, className);
        else
            return String.format("/%s%s", databaseName, className);
    }

    private <T> String getTablePath(Class<T> entityClass) {
        String className = entityClass.getAnnotation(Document.class).value();

        if(className.isEmpty())
            className = entityClass.getSimpleName().toLowerCase();

        if(className.startsWith("/"))
            return className;
        else
            return String.format("/%s", className);
    }

}
