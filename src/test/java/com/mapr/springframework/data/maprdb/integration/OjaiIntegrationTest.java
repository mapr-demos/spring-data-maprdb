package com.mapr.springframework.data.maprdb.integration;

import com.mapr.db.MapRDB;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.ojai.Document;
import org.ojai.DocumentStream;
import org.ojai.store.Connection;
import org.ojai.store.DocumentStore;
import org.ojai.store.DriverManager;
import org.ojai.store.Query;

public class OjaiIntegrationTest {

    public final static String TABLE_PATH = "/test/user";

    public Connection connection;
    public DocumentStore store;

    @Before
    public void init() {
        destroy();
        MapRDB.createTable(TABLE_PATH);
        connection = DriverManager.getConnection("ojai:mapr:");
        store = connection.getStore(TABLE_PATH);
    }

    @After
    public void destroy() {
        if(MapRDB.tableExists(TABLE_PATH))
            MapRDB.deleteTable(TABLE_PATH);
    }

    @Test
    public void connectionTest() {
        System.out.println("==== Start Application ===");

        Query query = connection.newQuery().build();

        DocumentStream stream = store.find(query);

        for (final Document userDocument : stream) {
            System.out.println(userDocument.asJsonString());
        }

        store.close();

        connection.close();
    }

    @Test
    public void drillConnectionTest() {
        System.out.println("==== Start Application ===");

        Query query = connection.newQuery().orderBy("name").build();

        DocumentStream stream = store.find(query);

        for (final Document userDocument : stream) {
            System.out.println(userDocument.asJsonString());
        }

        store.close();

        connection.close();
    }

}
