package com.mapr.springframework.data.maprdb;

import com.mapr.springframework.data.maprdb.core.MapROperations;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ojai.store.Connection;
import org.ojai.store.DriverManager;
import org.ojai.store.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { MapRTestConfiguration.class })
public class MapROperationsIntegrationTests {

    public final static String TABLE_NAME = "/user";
    public final static String CUSTOM_TABLE_NAME = "/user2";

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

        Assert.assertTrue(mapROperations.tableExists(User.class));
    }

    @Test
    public void createCustomTableByEntityTest() {
        mapROperations.createTable(UserWithCustomTable.class);

        Assert.assertTrue(mapROperations.tableExists(CUSTOM_TABLE_NAME));
    }

    @Test
    public void getStoreTest() {
        mapROperations.createTable(TABLE_NAME);

        Assert.assertNotNull(mapROperations.getStore(TABLE_NAME));
    }

    @Test
    public void getStoreByEntity() {
        mapROperations.createTable(User.class);

        Assert.assertNotNull(mapROperations.getStore(User.class));
    }

    @Test
    public void executeTest() {
        mapROperations.createTable(User.class);

        Query query = mapROperations.getConnection().newQuery().select("_id").orderBy("_id").limit(1).build();

        List<User> list = mapROperations.execute(query, User.class);
    }

}
