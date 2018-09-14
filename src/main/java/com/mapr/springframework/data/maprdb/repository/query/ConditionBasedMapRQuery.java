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

        for(PartTree.OrPart orPart : tree) {
            QueryCondition cond = convertOrPartToQueryCondition(orPart, parametersIterator);
            if(condition.isEmpty())
                condition = condition.or().condition(cond);
            else
                condition = condition.condition(cond);
        }

        return operations.getConnection().newQuery().where(condition.close().build()).build();
    }

    @Override
    public QueryMethod getQueryMethod() {
        return null;
    }

    protected QueryCondition convertOrPartToQueryCondition(PartTree.OrPart orPart, Iterator itr) {

        QueryCondition condition = operations.getConnection().newCondition();

        for(Part p : orPart) {
            QueryCondition cond = convertPartToQueryCondition(p, itr);
            if(condition.isEmpty())
                condition = condition.and().condition(cond);
            else
                condition = condition.condition(cond);
        }

        return condition.close().build();
    }

    protected QueryCondition convertPartToQueryCondition(Part part, Iterator itr) {

        String name = part.getProperty().getSegment();
        QueryCondition condition = operations.getConnection().newCondition();

        Object parameters;
        switch(part.getType()) {
            case SIMPLE_PROPERTY:
                parameters = itr.next();
                if(!(parameters instanceof Collection<?>))
                    condition = condition.is(name, QueryCondition.Op.EQUAL, parameters.toString());
                else
                    throw new UnsupportedOperationException(part.getType().toString() + " method with Example is not supported yet");
                break;
            case NEGATING_SIMPLE_PROPERTY:
                parameters = itr.next();
                if(!(parameters instanceof Collection<?>))
                    condition = condition.is(name, QueryCondition.Op.NOT_EQUAL, parameters.toString());
                else
                    throw new UnsupportedOperationException(part.getType().toString() + " method with Example is not supported yet");
                break;
            case LIKE:
                condition = condition.like(name, itr.next().toString());
                break;
            case NOT_LIKE:
                condition = condition.notLike(name, itr.next().toString());
                break;
            case IN:
                condition = condition.in(name, new ArrayList<>((Collection<?>) itr.next()));
                break;
            case NOT_IN:
                condition = condition.notIn(name, new ArrayList<>((Collection<?>) itr.next()));
                break;
            case EXISTS:
                condition = condition.exists(name);
                break;
            case LESS_THAN:
                //TODO fix object convert
                condition = condition.is(name, QueryCondition.Op.LESS, Double.parseDouble(itr.next().toString()));
                break;
            case LESS_THAN_EQUAL:
                //TODO fix object convert
                condition = condition.is(name, QueryCondition.Op.LESS_OR_EQUAL, Double.parseDouble(itr.next().toString()));
                break;
            case GREATER_THAN:
                //TODO fix object convert
                condition = condition.is(name, QueryCondition.Op.GREATER, Double.parseDouble(itr.next().toString()));
                break;
            case GREATER_THAN_EQUAL:
                //TODO fix object convert
                condition = condition.is(name, QueryCondition.Op.GREATER_OR_EQUAL, Double.parseDouble(itr.next().toString()));
                break;
            case TRUE:
                condition = condition.is(name, QueryCondition.Op.EQUAL, true);
                break;
            case FALSE:
                condition = condition.is(name, QueryCondition.Op.EQUAL, false);
                break;
            default:
                throw new UnsupportedOperationException(part.getType().toString() + " method with Example is not supported yet");
        }

        return condition.build();
    }

}
