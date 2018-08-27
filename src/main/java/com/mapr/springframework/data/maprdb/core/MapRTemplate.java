package com.mapr.springframework.data.maprdb.core;

import com.mapr.db.MapRDB;
import com.mapr.db.Table;
import com.mapr.springframework.data.maprdb.core.mapping.Document;
import org.ojai.DocumentStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class MapRTemplate implements MapROperations {

    private final String databaseName;
    private final static Logger LOGGER = LoggerFactory.getLogger(MapRTemplate.class);


    public MapRTemplate(final String databaseName) {
        this.databaseName = databaseName;
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
    public <T> Table getTable(Class<T> entityClass) {
        return getTable(getTablePath(entityClass));
    }

    @Override
    public Table getTable(final String tableName) {
        return MapRDB.getTable(getPath(tableName));
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
    public <T> Optional<T> findById(Object id, Class<T> entityClass) {
        return findById(id, entityClass, getTablePath(entityClass));
    }

    @Override
    public <T> Optional<T> findById(Object id, Class<T> entityClass, final String tableName) {
        return Optional.of(getTable(tableName).findById(id.toString()).toJavaBean(entityClass));
    }

    @Override
    public <T> List<T> findAll(Class<T> entityClass) {
        return findAll(entityClass, getTablePath(entityClass));
    }

    @Override
    public <T> List<T> findAll(Class<T> entityClass, final String tableName) {
        return convertDocumentStreamToIterable(getTable(tableName).find(), entityClass);
    }

    @Override
    public <T> T insert(T objectToSave) {
        return insert(objectToSave, getTablePath(objectToSave.getClass()));
    }

    @Override
    public <T> T insert(T objectToSave, final String tableName) {
        Table table = getTable(tableName);

        org.ojai.Document document = MapRDB.newDocument(objectToSave);

        table.insert(document);

        return (T) document.toJavaBean(objectToSave.getClass());
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
        Table table = getTable(tableName);

        org.ojai.Document document = MapRDB.newDocument(objectToSave);

        table.insert(document);

        return (T) document.toJavaBean(objectToSave.getClass());
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
        getTable(tableName).delete(MapRDB.newDocument(object));
    }

    @Override
    public <T> void removeById(Object id, Class<T> entityClass) {
        removeById(id, entityClass, getTablePath(entityClass));
    }

    @Override
    public <T> void removeById(Object id, Class<T> entityClass, final String tableName) {
        getTable(tableName).delete(id.toString());
//        remove(findById(id, entityClass, tableName), tableName);
    }

    @Override
    public <T> long count(Class<T> entityClass) {
        LOGGER.warn("Count method use iterations over all records, so it is not recommended using it");

        DocumentStream rs = getTable(entityClass).find("_id");
        Iterator<org.ojai.Document> itrs = rs.iterator();

        long totalRow = 0;
        while (itrs.hasNext()) {
            itrs.next();
            totalRow++;
        }

        rs.close();

        return totalRow;
    }

    private <T> List<T> convertDocumentStreamToIterable(DocumentStream documentStream, Class<T> entityClass) {
        List<T> resultCollection = new LinkedList<>();

        documentStream.forEach(d -> resultCollection.add(d.toJavaBean(entityClass)));

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
