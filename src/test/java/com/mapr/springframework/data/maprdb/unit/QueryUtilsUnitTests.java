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

import java.text.SimpleDateFormat;
import java.util.*;

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

    @Test
    public void booleanSetIsTest() {
        Object value = true;
        QueryCondition condition = connection.newCondition();

        QueryUtils.setIsCondition(condition, name, QueryCondition.Op.EQUAL, value);
        Assert.assertEquals(formatCondition(name, value), condition.toString());

        value = false;
        condition = connection.newCondition();

        QueryUtils.setIsCondition(condition, name, QueryCondition.Op.EQUAL, value);
        Assert.assertEquals(formatCondition(name, value), condition.toString());
    }

    @Test
    public void byteSetIsTest() {
        Object value = (byte) 0xFF;
        QueryCondition condition = connection.newCondition();

        QueryUtils.setIsCondition(condition, name, QueryCondition.Op.EQUAL, value);
        Assert.assertEquals(formatCondition(name, value), condition.toString());
    }

    @Test
    public void doubleSetIsTest() {
        Object value = 18.99;
        QueryCondition condition = connection.newCondition();

        QueryUtils.setIsCondition(condition, name, QueryCondition.Op.EQUAL, value);
        Assert.assertEquals(formatCondition(name, value), condition.toString());
    }

    @Test
    public void floatSetIsTest() {
        Object value = 18F;
        QueryCondition condition = connection.newCondition();

        QueryUtils.setIsCondition(condition, name, QueryCondition.Op.EQUAL, value);
        Assert.assertEquals(formatCondition(name, value), condition.toString());
    }

    @Test
    public void integerSetIsTest() {
        Object value = 10;
        QueryCondition condition = connection.newCondition();

        QueryUtils.setIsCondition(condition, name, QueryCondition.Op.EQUAL, value);
        Assert.assertEquals(formatCondition(name, value), condition.toString());
    }

    @Test
    public void shortSetIsTest() {
        Object value = (short) 10;
        QueryCondition condition = connection.newCondition();

        QueryUtils.setIsCondition(condition, name, QueryCondition.Op.EQUAL, value);
        Assert.assertEquals(formatCondition(name, value), condition.toString());
    }

    @Test
    public void longSetIsTest() {
        Object value = 10L;
        QueryCondition condition = connection.newCondition();

        QueryUtils.setIsCondition(condition, name, QueryCondition.Op.EQUAL, value);
        Assert.assertEquals(formatCondition(name, value), condition.toString());
    }

    @Test
    public void dateSetIsTest() {
        Object value = new Date();
        QueryCondition condition = connection.newCondition();

        QueryUtils.setIsCondition(condition, name, QueryCondition.Op.EQUAL, value);
        Assert.assertEquals(formatCondition(name, value), condition.toString());
    }

    @Test
    public void stringSetIsTest() {
        QueryCondition condition = connection.newCondition();

        QueryUtils.setIsCondition(condition, name, QueryCondition.Op.EQUAL, value);
        Assert.assertEquals(formatCondition(name, value), condition.toString());
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
        if(value.getClass() == String.class)
            return String.format("(%s = \"%s\")", name, value);
        else if(value.getClass() == Date.class) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
            return String.format("(%s = {\"$date\":\"%s\"})", name, sdf.format(value));
        } else if(value.getClass() == Long.class || value.getClass() == Byte.class
                || value.getClass() ==  Integer.class || value.getClass() ==  Short.class)
            return String.format("(%s = {\"$numberLong\":%s})", name, value);
        else if(value.getClass() == Float.class)
            return String.format("(%s = %2.0f)", name, value);
        else
            return String.format("(%s = %s)", name, value);
    }

}
