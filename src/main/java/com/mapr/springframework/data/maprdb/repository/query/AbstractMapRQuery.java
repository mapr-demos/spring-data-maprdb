package com.mapr.springframework.data.maprdb.repository.query;

import com.mapr.springframework.data.maprdb.core.MapROperations;
import org.springframework.data.repository.query.RepositoryQuery;

public abstract class AbstractMapRQuery implements RepositoryQuery {

    protected final MapROperations operations;
    protected final MapRQueryMethod method;
    protected final Class<?> domainClass;

    public AbstractMapRQuery(MapRQueryMethod method, MapROperations operations) {

        this.method = method;
        this.operations = operations;
        domainClass = null;
    }

    @Override
    public Object execute(Object[] parameters) {
        return null;
    }

}
