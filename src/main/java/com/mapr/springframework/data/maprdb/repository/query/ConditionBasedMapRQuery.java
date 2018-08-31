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
            condition.or().condition(convertOrPartToQueryCondition(orPart, parametersIterator)).close();

        return condition.build();
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

        switch(part.getType()) {
            case SIMPLE_PROPERTY:
                if(part.getNumberOfArguments() == 1)
                    condition = MapRDB.newCondition().is(name, QueryCondition.Op.EQUAL, itr.next().toString()).build();
                else
                    condition = MapRDB.newCondition().equals(name, new ArrayList<>((Collection<?>) itr.next()));
                break;
            case NEGATING_SIMPLE_PROPERTY:
                if(part.getNumberOfArguments() == 1)
                    condition = MapRDB.newCondition().is(name, QueryCondition.Op.NOT_EQUAL, itr.next().toString()).build();
                else
                    condition = MapRDB.newCondition().notEquals(name, new ArrayList<>((Collection<?>) itr.next()));
                break;
            case LIKE:
                condition = MapRDB.newCondition().like(part.getProperty().getSegment(), itr.next().toString()).build();
                break;
            case NOT_LIKE:
                condition = MapRDB.newCondition().notLike(part.getProperty().getSegment(), itr.next().toString()).build();
                break;
            case IN:
                condition =  MapRDB.newCondition().in(name, new ArrayList<>((Collection<?>) itr.next())).build();
                break;
            case NOT_IN:
                condition = MapRDB.newCondition().notIn(name, new ArrayList<>((Collection<?>) itr.next())).build();
                break;
            case IS_NULL:
                condition = MapRDB.newCondition().exists(name).build();
                break;
            case IS_NOT_NULL:
                condition = MapRDB.newCondition().notExists(name).build();
                break;
            case LESS_THAN:
                condition = MapRDB.newCondition().is(name, QueryCondition.Op.LESS, itr.next().toString()).build();
                break;
            case LESS_THAN_EQUAL:
                condition = MapRDB.newCondition().is(name, QueryCondition.Op.LESS_OR_EQUAL, itr.next().toString()).build();
                break;
            case GREATER_THAN:
                condition = MapRDB.newCondition().is(name, QueryCondition.Op.GREATER, itr.next().toString()).build();
                break;
            case GREATER_THAN_EQUAL:
                condition = MapRDB.newCondition().is(name, QueryCondition.Op.GREATER_OR_EQUAL, itr.next().toString()).build();
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
