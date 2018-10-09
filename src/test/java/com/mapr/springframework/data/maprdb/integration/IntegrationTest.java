package com.mapr.springframework.data.maprdb.integration;

import com.mapr.db.MapRDB;
import org.junit.*;
import org.ojai.Document;
import org.ojai.DocumentStream;
import org.ojai.store.DocumentStore;
import org.ojai.store.Query;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class IntegrationTest {

    public final static String TABLE_PATH = "/test/user";
    public static String DRILL_JDBC_URL = "jdbc:drill:drillbit=node1";

    @BeforeClass
    public static void init() {
        destroy();

        if(!MapRDB.tableExists(TABLE_PATH))
            MapRDB.createTable(TABLE_PATH);

    }

    @AfterClass
    public static void destroy() {
        if(MapRDB.tableExists(TABLE_PATH))
            MapRDB.deleteTable(TABLE_PATH);
    }

    @Test
    public void ojaiConnectionTest() {
        System.out.println("==== Start OJAI Application ===");

        org.ojai.store.Connection connection = org.ojai.store.DriverManager.getConnection("ojai:mapr:");
        DocumentStore store = connection.getStore(TABLE_PATH);
        Query query = connection.newQuery().build();

        DocumentStream stream = store.find(query);

        for (final Document userDocument : stream) {
            System.out.println(userDocument.asJsonString());
        }

        store.close();
        connection.close();
    }


    @Test
    public void ojaiDrillConnectionTest() {
        System.out.println("==== OJAI Drill Application ===");

        org.ojai.store.Connection connection = org.ojai.store.DriverManager.getConnection("ojai:mapr:");
        DocumentStore store = connection.getStore(TABLE_PATH);
        Query query = connection.newQuery().select("_id", "name").orderBy("name").build();

        DocumentStream stream = store.find(query);

        for (final Document userDocument : stream) {
            System.out.println(userDocument.asJsonString());
        }

        store.close();
        connection.close();
    }

    @Test
    public void drillCustomConnectionTest() throws SQLException {
        System.out.println("==== Start DRILL Application ===");

        java.sql.Connection connection = java.sql.DriverManager.getConnection(DRILL_JDBC_URL, "root", "");
        Statement statement = connection.createStatement();

        Assert.assertNotNull(statement);

        statement.close();
        connection.close();
    }

    @Test
    public void drillRequestTest() throws SQLException {
        System.out.println("==== DRILL Application ===");

        java.sql.Connection connection = java.sql.DriverManager.getConnection(DRILL_JDBC_URL, "root", "");
        Statement statement = connection.createStatement();

        final String sql = "SELECT * FROM dfs.`" + TABLE_PATH + "`";
        System.out.println("Query: " + sql);

        ResultSet result = statement.executeQuery(sql);

        while(result.next()){
            System.out.println(result.getString(1));
        }

        Assert.assertNotNull(statement);

        statement.close();
        connection.close();
    }

}
