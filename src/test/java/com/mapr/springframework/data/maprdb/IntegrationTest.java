package com.mapr.springframework.data.maprdb;

import com.mapr.db.Table;
import com.mapr.springframework.data.maprdb.config.AbstractMapRConfiguration;
import com.mapr.springframework.data.maprdb.core.MapROperations;
import com.mapr.springframework.data.maprdb.repository.config.EnableMapRRepository;
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
public class IntegrationTest {

    public final static String TABLE_NAME = "/test";
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
    public void init() {
        mapROperations.dropTable(TABLE_NAME);
    }

    @Test
    public void mapROperationsTest() {
        mapROperations.createTable(TABLE_NAME);
        Table table = mapROperations.getTable(TABLE_NAME);
        Assert.assertNotNull(table);
    }

}
