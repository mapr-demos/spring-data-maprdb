package com.mapr.springframework.data.maprdb;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashSet;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { MapRTestConfiguration.class })
public class ComplexMapRRepositoryIntegrationTests {

    @Autowired
    public ComplexUserRepository repository;

    @Before
    @After
    public void delete() {
        repository.deleteAll();
    }

    @Test
    public void findEqualsTest() {
        List<User> users = UserUtils.getUsers();

        repository.saveAll(users);

        User user = users.get(3);

        List<User> usersFromDB = repository.findByName(user.getName());

        Assert.assertEquals(1, usersFromDB.size());

        Assert.assertEquals(user, usersFromDB.get(0));
    }

    @Test
    public void findNotEqualsTest() {
        List<User> users = UserUtils.getUsers();

        repository.saveAll(users);

        User user = users.get(3);

        users.remove(3);

        List<User> usersFromDB = repository.findByNameNot(user.getName());

        Assert.assertEquals(new HashSet<>(users), new HashSet<>(usersFromDB));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void notImplementedMethod() {
        List<User> users = UserUtils.getUsers();

        repository.saveAll(users);

        User user = users.get(3);

        repository.findByNameContaining(user.getName());
    }

}
