package com.mapr.springframework.data.maprdb.repository.query;

import com.mapr.springframework.data.maprdb.core.MapROperations;
import org.ojai.store.QueryCondition;
import org.springframework.data.repository.query.RepositoryQuery;

public abstract class AbstractMapRQuery implements RepositoryQuery {

    protected final MapROperations operations;
    protected final MapRQueryMethod method;
    protected final Class<?> domainClass;

    public AbstractMapRQuery(MapRQueryMethod method, MapROperations operations) {

        this.method = method;
        this.operations = operations;
        domainClass = method.getReturnedObjectType();
    }

    @Override
    public Object execute(Object[] parameters) {
        return operations.execute(convertToQueryConditions(parameters), domainClass);
    }

    protected abstract QueryCondition convertToQueryConditions(Object[] parameters);

}
