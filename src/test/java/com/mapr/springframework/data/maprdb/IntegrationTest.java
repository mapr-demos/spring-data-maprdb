package com.mapr.springframework.data.maprdb;


import com.mapr.springframework.data.maprdb.config.AbstractMapRConfiguration;
import com.mapr.springframework.data.maprdb.repository.config.EnableMapRRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class IntegrationTest {

    @Configuration
    @EnableMapRRepository
    static class Config extends AbstractMapRConfiguration {

        @Override
        protected String database() {
            return "test";
        }

    }

    @Autowired
    public UserRepository userRepository;

    @Test
    public void RepositoryFactoryTest() {
        Assert.assertNotNull(userRepository);
    }

}
