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

public class IntegrationTest {

    public final static String TABLE_PATH = "/test/user";

    @Before
    @After
    public void init() {
        destroy();
        MapRDB.createTable(TABLE_PATH);
    }

    @After
    public void destroy() {
        if(MapRDB.tableExists(TABLE_PATH))
            MapRDB.deleteTable(TABLE_PATH);
    }

    @Test
    public void connectionTest() {
        System.out.println("==== Start Application ===");
        final Connection connection = DriverManager.getConnection("ojai:mapr:");

        final DocumentStore store = connection.getStore(TABLE_PATH);

        final Query query = connection.newQuery().build();

        final DocumentStream stream = store.find(query);

        for (final Document userDocument : stream) {
            System.out.println(userDocument.asJsonString());
        }

        store.close();

        connection.close();
    }
}
