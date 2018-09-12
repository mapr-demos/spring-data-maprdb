package com.mapr.springframework.data.maprdb.repository.query;

import com.mapr.springframework.data.maprdb.core.MapROperations;
import org.ojai.store.Query;
import org.ojai.store.QueryCondition;
import org.springframework.data.repository.query.QueryMethod;
import org.springframework.data.repository.query.parser.Part;
import org.springframework.data.repository.query.parser.PartTree;

import java.util.*;

public class ConditionBasedMapRQuery extends AbstractMapRQuery {
    private final PartTree tree;

    public ConditionBasedMapRQuery(MapRQueryMethod method, MapROperations operations) {
        super(method, operations);
        tree = new PartTree(method.getName(), domainClass);
    }

    @Override
    protected Query convertToQuery(Object[] parameters) {
        Iterator parametersIterator = Arrays.asList(parameters).iterator();

        QueryCondition condition = operations.getConnection().newCondition();

        for(PartTree.OrPart orPart : tree)
            condition.or().condition(convertOrPartToQueryCondition(orPart, parametersIterator)).close().build();

        return operations.getConnection().newQuery().where(condition).build();
    }

    @Override
    public QueryMethod getQueryMethod() {
        return null;
    }

    protected QueryCondition convertOrPartToQueryCondition(PartTree.OrPart orPart, Iterator itr) {

        QueryCondition condition = operations.getConnection().newCondition();

        for(Part p : orPart)
            condition = condition.and().condition(convertPartToQueryCondition(p, itr)).close().build();

        return condition;
    }

    protected QueryCondition convertPartToQueryCondition(Part part, Iterator itr) {

        String name = part.getProperty().getSegment();
        QueryCondition condition = operations.getConnection().newCondition();

        Object parameters;
        switch(part.getType()) {
            case SIMPLE_PROPERTY:
                parameters = itr.next();
                if(!(parameters instanceof Collection<?>))
                    condition = condition.is(name, QueryCondition.Op.EQUAL, parameters.toString()).build();
                else
                    throw new UnsupportedOperationException(part.getType().toString() + " method with Example is not supported yet");
                break;
            case NEGATING_SIMPLE_PROPERTY:
                parameters = itr.next();
                if(!(parameters instanceof Collection<?>))
                    condition = condition.is(name, QueryCondition.Op.NOT_EQUAL, parameters.toString()).build();
                else
                    throw new UnsupportedOperationException(part.getType().toString() + " method with Example is not supported yet");
                break;
            case LIKE:
                condition = condition.like(name, itr.next().toString()).build();
                break;
            case NOT_LIKE:
                condition = condition.notLike(name, itr.next().toString()).build();
                break;
            case IN:
                condition = condition.in(name, new ArrayList<>((Collection<?>) itr.next())).build();
                break;
            case NOT_IN:
                condition = condition.notIn(name, new ArrayList<>((Collection<?>) itr.next())).build();
                break;
            case EXISTS:
                condition = condition.exists(name).build();
                break;
            case LESS_THAN:
                //TODO fix object convert
                condition = condition.is(name, QueryCondition.Op.LESS, Double.parseDouble(itr.next().toString())).build();
                break;
            case LESS_THAN_EQUAL:
                //TODO fix object convert
                condition = condition.is(name, QueryCondition.Op.LESS_OR_EQUAL, Double.parseDouble(itr.next().toString())).build();
                break;
            case GREATER_THAN:
                //TODO fix object convert
                condition = condition.is(name, QueryCondition.Op.GREATER, Double.parseDouble(itr.next().toString())).build();
                break;
            case GREATER_THAN_EQUAL:
                //TODO fix object convert
                condition = condition.is(name, QueryCondition.Op.GREATER_OR_EQUAL, Double.parseDouble(itr.next().toString())).build();
                break;
            case TRUE:
                condition = condition.is(name, QueryCondition.Op.EQUAL, true).build();
                break;
            case FALSE:
                condition = condition.is(name, QueryCondition.Op.EQUAL, false).build();
                break;
            default:
                throw new UnsupportedOperationException(part.getType().toString() + " method with Example is not supported yet");
        }

        return condition;
    }

}
