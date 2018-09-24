package com.mapr.springframework.data.maprdb.repository.query;

import com.mapr.springframework.data.maprdb.core.MapROperations;
import org.ojai.store.Query;
import org.springframework.data.repository.query.RepositoryQuery;

public abstract class AbstractMapRQuery implements RepositoryQuery {

    protected final MapROperations operations;
    protected final MapRQueryMethod method;
    protected final Class<?> domainClass;

    public AbstractMapRQuery(MapRQueryMethod method, Class<?> domainClass, MapROperations operations) {

        this.method = method;
        this.operations = operations;
        this.domainClass = domainClass;
    }

    @Override
    public Object execute(Object[] parameters) {

        if(isDeleteQuery()) {
            operations.execute(convertToQuery(parameters), domainClass).forEach(operations::remove);
            return null;
        }

        return operations.execute(convertToQuery(parameters), domainClass);
    }

    protected abstract Query convertToQuery(Object[] parameters);

    protected abstract boolean isDeleteQuery();

}
