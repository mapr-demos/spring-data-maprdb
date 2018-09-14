package com.mapr.springframework.data.maprdb.repository.query;

import com.mapr.springframework.data.maprdb.core.MapROperations;
import org.ojai.store.Query;
import org.springframework.data.repository.query.QueryMethod;

public class StringBasedMapRQuery extends AbstractMapRQuery{

    protected String query;

    public StringBasedMapRQuery(MapRQueryMethod method, MapROperations operations) {
        this(method.getAnnotatedQuery(), method, operations);
    }

    public StringBasedMapRQuery(String query, MapRQueryMethod method, MapROperations operations) {
        super(method, operations);
        this.query = query;
    }

    @Override
    protected Query convertToQuery(Object[] parameters) {
        return operations.getConnection().newQuery(query).build();
    }

    @Override
    public QueryMethod getQueryMethod() {
        return null;
    }
}
