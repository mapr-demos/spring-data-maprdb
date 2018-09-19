package com.mapr.springframework.data.maprdb.repository.query;

import org.ojai.store.Connection;
import org.ojai.store.Query;
import org.ojai.store.QueryCondition;
import org.ojai.store.SortOrder;
import org.ojai.types.OTimestamp;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.parser.Part;
import org.springframework.data.repository.query.parser.PartTree;

import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.util.*;

public class QueryUtils {

    public static QueryCondition getQueryCondition(Connection connection, PartTree tree, Object[] parameters) {
        Iterator parametersIterator = Arrays.asList(parameters).iterator();

        QueryCondition conditions = connection.newCondition();

        for(PartTree.OrPart orPart : tree) {
            QueryCondition cond = convertOrPartToQueryCondition(connection, orPart, parametersIterator);
            if(conditions.isEmpty())
                conditions.or().condition(cond);
            else
                conditions.condition(cond);
        }

        if(!conditions.isEmpty())
            conditions.close();

        return conditions.build();
    }

    public static QueryCondition convertOrPartToQueryCondition(Connection connection, PartTree.OrPart orPart, Iterator itr) {

        QueryCondition condition = connection.newCondition();

        for(Part p : orPart) {
            QueryCondition cond = convertPartToQueryCondition(connection, p, itr);
            if(condition.isEmpty())
                condition.and().condition(cond);
            else
                condition.condition(cond);
        }

        return condition.close().build();
    }

    public static QueryCondition convertPartToQueryCondition(Connection connection, Part part, Iterator itr) {

        String name = part.getProperty().getSegment();
        QueryCondition condition = connection.newCondition();

        Object parameters;
        switch(part.getType()) {
            case SIMPLE_PROPERTY:
                parameters = itr.next();
                if(!(parameters instanceof Collection<?>))
                    setIsCondition(condition, name, QueryCondition.Op.EQUAL, parameters);
                else
                    throw new UnsupportedOperationException(part.getType().toString() + " method with Example is not supported yet");
                break;
            case NEGATING_SIMPLE_PROPERTY:
                parameters = itr.next();
                if(!(parameters instanceof Collection<?>))
                    setIsCondition(condition, name, QueryCondition.Op.NOT_EQUAL, parameters);
                else
                    throw new UnsupportedOperationException(part.getType().toString() + " method with Example is not supported yet");
                break;
            case LIKE:
                condition.like(name, itr.next().toString());
                break;
            case NOT_LIKE:
                condition.notLike(name, itr.next().toString());
                break;
            case IN:
                condition.in(name, new ArrayList<>((Collection<?>) itr.next()));
                break;
            case NOT_IN:
                condition.notIn(name, new ArrayList<>((Collection<?>) itr.next()));
                break;
            case EXISTS:
                condition.exists(name);
                break;
            case LESS_THAN:
                setIsCondition(condition, name, QueryCondition.Op.LESS, itr.next());
                break;
            case LESS_THAN_EQUAL:
                setIsCondition(condition, name, QueryCondition.Op.LESS_OR_EQUAL, itr.next());
                break;
            case GREATER_THAN:
                setIsCondition(condition, name, QueryCondition.Op.GREATER, itr.next());
                break;
            case GREATER_THAN_EQUAL:
                setIsCondition(condition, name, QueryCondition.Op.GREATER_OR_EQUAL, itr.next());
                break;
            case TRUE:
                condition.is(name, QueryCondition.Op.EQUAL, true);
                break;
            case FALSE:
                condition.is(name, QueryCondition.Op.EQUAL, false);
                break;
            case BETWEEN:
                condition.and();
                setIsCondition(condition, name, QueryCondition.Op.GREATER_OR_EQUAL, itr.next());
                setIsCondition(condition, name, QueryCondition.Op.LESS_OR_EQUAL, itr.next());
                condition.close();
                break;
            default:
                throw new UnsupportedOperationException(part.getType().toString() + " method is not supported yet");
        }

        return condition.build();
    }

    public static Query addPageableToQuery(Query query, Pageable page) {
        addSortToQuery(query, page.getSort());
        addOffsetAndLimitToQuery(query, page.getOffset(), page.getPageSize());

        return query;
    }

    public static Query addSortToQuery(Query query, Sort sort) {
        for(Sort.Order o : sort)
            query = query.orderBy(o.getProperty(), o.isAscending() ? SortOrder.ASC : SortOrder.DESC);

        return query;
    }

    public static Query addOffsetAndLimitToQuery(Query query, long offset, long limit) {
        return query.offset(offset).limit(limit);
    }

    public static QueryCondition setIsCondition(QueryCondition condition, String name, QueryCondition.Op op, Object object) {

        if(object instanceof BigDecimal)
            condition.is(name, op, (BigDecimal) object);

        else if(object instanceof Boolean)
            condition.is(name, op, Boolean.parseBoolean(object.toString()));

        else if(object instanceof Byte)
            condition.is(name, op, (Byte) object);

        else if(object instanceof ByteBuffer)
            condition.is(name, op, (ByteBuffer) object);

        else if(object instanceof Double)
            condition.is(name, op, (Double) object);

        else if(object instanceof Float)
            condition.is(name, op, (Float) object);

        else if(object instanceof Integer)
            condition.is(name, op, (Integer) object);

        else if(object instanceof Long)
            condition.is(name, op, (Long) object);

        else if(object instanceof Short)
            condition.is(name, op, (Short) object);

        else if(object instanceof Date)
            condition.is(name, op, new OTimestamp((Date) object));

        else if(object instanceof String)
            condition.is(name, op, object.toString());

        else
            throw new UnsupportedOperationException(object.getClass().getCanonicalName() +  " type is not supported");

        return condition;
    }
}
