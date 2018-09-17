package com.mapr.springframework.data.maprdb.unit;

import com.mapr.springframework.data.maprdb.model.User;
import com.mapr.springframework.data.maprdb.repository.query.QueryUtils;
import org.junit.Assert;
import org.junit.Test;
import org.ojai.store.Connection;
import org.ojai.store.DriverManager;
import org.ojai.store.QueryCondition;
import org.springframework.data.repository.query.parser.Part;
import org.springframework.data.repository.query.parser.PartTree;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;

import static org.springframework.data.repository.query.parser.Part.Type.*;

public class QueryUtilsUnitTests {

    public Connection connection = DriverManager.getConnection("ojai:mapr:");
    public String name = "name";
    public String value = "test";

    @Test
    public void convertPartToQueryConditionTest() {
        PartTree tree = new PartTree("findByName", User.class);
        Part part = tree.getParts(SIMPLE_PROPERTY).stream().findFirst().get();
        Iterator itr = Collections.singletonList(value).iterator();
        QueryCondition condition = QueryUtils.convertPartToQueryCondition(connection, part, itr);

        Assert.assertEquals(formatCondition(name, value), condition.toString());
    }

    @Test
    public void convertOrPartToQueryConditionTest() {
        PartTree tree = new PartTree("findByNameAndName", User.class);
        PartTree.OrPart orPart = tree.stream().findFirst().get();
        Iterator itr = Arrays.asList(value, value).iterator();
        QueryCondition condition = QueryUtils.convertOrPartToQueryCondition(connection, orPart, itr);

        Assert.assertEquals(getAndCondition(name, value), condition.toString());
    }

    @Test
    public void getQueryConditionTest() {
        PartTree tree = new PartTree("findByNameAndNameOrNameAndName", User.class);
        Object[] parameters = {value, value, value, value};
        QueryCondition condition = QueryUtils.getQueryCondition(connection, tree, parameters);

        Assert.assertEquals(getOrAndCondition(name, value), condition.toString());
    }

    public String getOrAndCondition(String name, Object value) {
        String condition = getAndCondition(name, value);
        return String.format("(%s or %s)", condition, condition);
    }

    public String getAndCondition(String name, Object value) {
        String condition = formatCondition(name, value);
        return String.format("(%s and %s)", condition, condition);
    }

    public String formatCondition(String name, Object value) {
        return String.format("(%s = \"%s\")", name, value);
    }

}
