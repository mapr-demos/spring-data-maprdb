package com.mapr.springframework.data.maprdb.repository.query;

import com.mapr.springframework.data.maprdb.core.MapROperations;
import org.ojai.store.Query;
import org.ojai.store.QueryCondition;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.QueryMethod;
import org.springframework.data.repository.query.parser.PartTree;

import java.util.Objects;

public class ConditionBasedMapRQuery extends AbstractMapRQuery {
    private final PartTree tree;

    public ConditionBasedMapRQuery(MapRQueryMethod method, Class<?> domainClass, MapROperations operations) {
        super(method, domainClass, operations);

        tree = new PartTree(method.getName(), domainClass);
    }

    @Override
    protected Query convertToQuery(Object[] parameters) {

        QueryCondition condition = QueryUtils.getQueryCondition(operations.getConnection(), tree, parameters);
        Query query = operations.getConnection().newQuery().where(condition);

        QueryUtils.addSortToQuery(query, tree.getSort());

        if(method.getParameters().hasSortParameter())
            QueryUtils.addSortToQuery(query, (Sort) parameters[method.getParameters().getSortIndex()]);

        if(tree.isLimiting()) {
            if(!isTopLimit()) {
                query.limit(Objects.requireNonNull(tree.getMaxResults()).longValue());
            } else {
                long count = operations.count(domainClass);
                query.offset(count - Objects.requireNonNull(tree.getMaxResults()).longValue());
            }
        }

        if(method.getParameters().hasPageableParameter())
            QueryUtils.addPageableToQuery(query, (Pageable) parameters[method.getParameters().getPageableIndex()]);

        return query.build();
    }

    @Override
    protected boolean isDeleteQuery() {
        return tree.isDelete();
    }

    @Override
    public QueryMethod getQueryMethod() {
        return null;
    }

    private boolean isTopLimit() {
        return method.getName().startsWith("findTop");
    }

}
