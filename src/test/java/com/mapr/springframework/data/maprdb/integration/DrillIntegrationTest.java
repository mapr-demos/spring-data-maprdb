package com.mapr.springframework.data.maprdb.integration;

import com.mapr.db.MapRDB;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.*;

public class DrillIntegrationTest {

    public final static String TABLE_PATH = "/test/user";
    public static String DRILL_JDBC_URL = "jdbc:drill:drillbit=node1";

    public Connection connection;

    @Before
    public void init() throws SQLException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        destroy();
        MapRDB.createTable(TABLE_PATH);
        Class.forName("org.apache.drill.jdbc.Driver").newInstance();
        connection = DriverManager.getConnection(DRILL_JDBC_URL, "root", "");
    }

    @After
    public void destroy() {
        if(MapRDB.tableExists(TABLE_PATH))
            MapRDB.deleteTable(TABLE_PATH);
    }

    @Test
    public void drillCustomConnectionTest() throws SQLException {
        System.out.println("==== Start Application ===");

        Statement statement = connection.createStatement();

        statement.close();
        connection.close();
    }

    @Test
    public void drillRequestTest() throws SQLException {
        System.out.println("==== Start Application ===");

        Statement statement = connection.createStatement();

        final String sql = "SELECT * FROM dfs.`" + TABLE_PATH + "`";
        System.out.println("Query: " + sql);

        ResultSet result = statement.executeQuery(sql);

        while(result.next()){
            System.out.println(result.getString(1));
        }

        statement.close();
        connection.close();
    }

}
