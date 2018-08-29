package com.mapr.springframework.data.maprdb;

import com.mapr.springframework.data.maprdb.config.AbstractMapRConfiguration;
import com.mapr.springframework.data.maprdb.core.MapROperations;
import com.mapr.springframework.data.maprdb.repository.config.EnableMapRRepository;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class MapROperationsIntegrationTests {

    public final static String TABLE_NAME = "/user";
    public final static String CUSTOM_TABLE_NAME = "/user2";
    public final static String DB_NAME = "test";

    @Configuration
    @EnableMapRRepository
    static class Config extends AbstractMapRConfiguration {

        @Override
        protected String database() {
            return DB_NAME;
        }

    }

    @Autowired
    public MapROperations mapROperations;

    @Before
    @After
    public void deleteTables() {
        mapROperations.dropTable(TABLE_NAME);
        mapROperations.dropTable(CUSTOM_TABLE_NAME);
    }

    @Test
    public void tableNotExistsTest() {
        Assert.assertFalse(mapROperations.tableExists(TABLE_NAME));
    }

    @Test
    public void tableExistsTest() {
        mapROperations.createTable(TABLE_NAME);

        Assert.assertTrue(mapROperations.tableExists(TABLE_NAME));
    }

    @Test
    public void getTableTest() {
        mapROperations.createTable(TABLE_NAME);

        Assert.assertNotNull(mapROperations.getTable(TABLE_NAME));
    }

    @Test
    public void createTableByEntityTest() {
        mapROperations.createTable(User.class);

        Assert.assertTrue(mapROperations.tableExists(TABLE_NAME));
    }

    @Test
    public void createCustomTableByEntityTest() {
        mapROperations.createTable(UserWithCustomTable.class);

        Assert.assertTrue(mapROperations.tableExists(CUSTOM_TABLE_NAME));
    }

}
