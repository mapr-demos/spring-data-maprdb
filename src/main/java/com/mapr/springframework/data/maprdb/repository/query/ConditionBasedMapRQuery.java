package com.mapr.springframework.data.maprdb.repository.query;

import com.mapr.db.MapRDB;
import com.mapr.springframework.data.maprdb.core.MapROperations;
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
    protected QueryCondition convertToQueryConditions(Object[] parameters) {
        Iterator parametersIterator = Arrays.asList(parameters).iterator();

        QueryCondition condition = MapRDB.newCondition();

        for(PartTree.OrPart orPart : tree)
            condition.or().condition(convertOrPartToQueryCondition(orPart, parametersIterator)).close().build();

        return condition;
    }

    @Override
    public QueryMethod getQueryMethod() {
        return null;
    }

    protected QueryCondition convertOrPartToQueryCondition(PartTree.OrPart orPart, Iterator itr) {

        QueryCondition condition = MapRDB.newCondition();

        for(Part p : orPart)
            condition = condition.and().condition(convertPartToQueryCondition(p, itr)).close().build();

        return condition;
    }

    protected QueryCondition convertPartToQueryCondition(Part part, Iterator itr) {

        String name = part.getProperty().getSegment();
        QueryCondition condition;

        Object parameters;
        switch(part.getType()) {
            case SIMPLE_PROPERTY:
                parameters = itr.next();
                if(!(parameters instanceof Collection<?>))
                    condition = MapRDB.newCondition().is(name, QueryCondition.Op.EQUAL, parameters.toString()).build();
                else
                    throw new UnsupportedOperationException(part.getType().toString() + " method with Example is not supported yet");
                break;
            case NEGATING_SIMPLE_PROPERTY:
                parameters = itr.next();
                if(!(parameters instanceof Collection<?>))
                    condition = MapRDB.newCondition().is(name, QueryCondition.Op.NOT_EQUAL, parameters.toString()).build();
                else
                    throw new UnsupportedOperationException(part.getType().toString() + " method with Example is not supported yet");
                break;
            case LIKE:
                condition = MapRDB.newCondition().like(name, itr.next().toString()).build();
                break;
            case NOT_LIKE:
                condition = MapRDB.newCondition().notLike(name, itr.next().toString()).build();
                break;
            case IN:
                condition =  MapRDB.newCondition().in(name, new ArrayList<>((Collection<?>) itr.next())).build();
                break;
            case NOT_IN:
                condition = MapRDB.newCondition().notIn(name, new ArrayList<>((Collection<?>) itr.next())).build();
                break;
            case EXISTS:
                condition = MapRDB.newCondition().exists(name).build();
                break;
            case LESS_THAN:
                condition = MapRDB.newCondition().is(name, QueryCondition.Op.LESS, Double.parseDouble(itr.next().toString())).build();
                break;
            case LESS_THAN_EQUAL:
                condition = MapRDB.newCondition().is(name, QueryCondition.Op.LESS_OR_EQUAL, Double.parseDouble(itr.next().toString())).build();
                break;
            case GREATER_THAN:
                condition = MapRDB.newCondition().is(name, QueryCondition.Op.GREATER, Double.parseDouble(itr.next().toString())).build();
                break;
            case GREATER_THAN_EQUAL:
                condition = MapRDB.newCondition().is(name, QueryCondition.Op.GREATER_OR_EQUAL, Double.parseDouble(itr.next().toString())).build();
                break;
            case TRUE:
                condition = MapRDB.newCondition().is(name, QueryCondition.Op.EQUAL, true).build();
                break;
            case FALSE:
                condition = MapRDB.newCondition().is(name, QueryCondition.Op.EQUAL, false).build();
                break;
            default:
                throw new UnsupportedOperationException(part.getType().toString() + " method with Example is not supported yet");
        }

        return condition;
    }

}
