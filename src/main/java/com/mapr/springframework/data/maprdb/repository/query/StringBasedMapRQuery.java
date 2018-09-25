package com.mapr.springframework.data.maprdb.repository.query;

import com.mapr.springframework.data.maprdb.core.MapROperations;
import org.ojai.store.Query;
import org.springframework.data.repository.query.QueryMethod;

public class StringBasedMapRQuery extends AbstractMapRQuery{

    protected String query;

    public StringBasedMapRQuery(MapRQueryMethod method, Class<?> domainClass, MapROperations operations) {
        this(method.getAnnotatedQuery(), method, domainClass, operations);
    }

    public StringBasedMapRQuery(String query, MapRQueryMethod method, Class<?> domainClass, MapROperations operations) {
        super(method, domainClass, operations);
        this.query = query;
    }

    @Override
    protected Query convertToQuery(Object[] parameters) {
        return operations.getConnection().newQuery(query).build();
    }

    @Override
    protected boolean isCountQuery() {
        return method.getQueryAnnotation().exists();
    }

    @Override
    protected boolean isDeleteQuery() {
        return method.getQueryAnnotation().delete();
    }

    @Override
    protected boolean isExistsQuery() {
        return method.getQueryAnnotation().count();
    }

    @Override
    public QueryMethod getQueryMethod() {
        return null;
    }

}
