package com.mapr.springframework.data.maprdb.integration;

import com.mapr.db.MapRDB;
import com.mapr.springframework.data.maprdb.utils.PropertiesReader;
import org.junit.*;
import org.ojai.Document;
import org.ojai.DocumentStream;
import org.ojai.store.DocumentStore;
import org.ojai.store.Query;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class IntegrationTest {

    public static String TABLE_PATH;

    public static String USERNAME;

    public static String PASSWORD;

    public static String DRILL_JDBC_URL;

    @BeforeClass
    public static void init() throws InterruptedException {
        Properties properties = PropertiesReader.getProperties("src/test/resources/tests.properties");

        TABLE_PATH = String.format("/%s/user", properties.getProperty("database.name"));
        USERNAME = properties.getProperty("database.user");
        PASSWORD = properties.getProperty("database.password");
        DRILL_JDBC_URL = String.format("jdbc:drill:drillbit=%s", properties.getProperty("database.host"));
        long delay = Long.parseLong(properties.getProperty("database.delay"));

        destroy();
        Thread.sleep(delay);

        if(!MapRDB.tableExists(TABLE_PATH))
            MapRDB.createTable(TABLE_PATH);

    }

    @AfterClass
    public static void destroy() throws InterruptedException {
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
    public void drillCustomConnectionTest() throws SQLException, ClassNotFoundException {
        System.out.println("==== Start DRILL Application ===");

        Class.forName("org.apache.drill.jdbc.Driver");
        java.sql.Connection connection = java.sql.DriverManager.getConnection(DRILL_JDBC_URL, USERNAME, PASSWORD);
        Statement statement = connection.createStatement();

        Assert.assertNotNull(statement);

        statement.close();
        connection.close();
    }

    @Test
    public void drillRequestTest() throws SQLException, ClassNotFoundException {
        System.out.println("==== DRILL Application ===");

        Class.forName("org.apache.drill.jdbc.Driver");
        java.sql.Connection connection = java.sql.DriverManager.getConnection(DRILL_JDBC_URL, USERNAME, PASSWORD);
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
