package com.mapr.springframework.data.maprdb;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { MapRTestConfiguration.class })
public class ComplexMapRRepositoryIntegrationTests {

    @Autowired
    public ComplexUserRepository repository;

    public List<User> users;

    @Before
    public void init() {
        delete();
        users = UserUtils.getUsers();
        repository.saveAll(users);
    }

    @After
    public void delete() {
        repository.deleteAll();
    }

    @Test
    public void findEqualsTest() {
        User user = users.get(3);

        List<User> usersFromDB = repository.findByName(user.getName());

        Assert.assertEquals(1, usersFromDB.size());

        Assert.assertEquals(user, usersFromDB.get(0));
    }

    @Test
    public void findNotEqualsTest() {
        User user = users.get(3);

        users.remove(3);

        List<User> usersFromDB = repository.findByNameNot(user.getName());

        Assert.assertEquals(new HashSet<>(users), new HashSet<>(usersFromDB));
    }

    @Test
    public void findLikeTest() {
        List<User> usersFromDB = repository.findByNameLike(users.get(10).getName());

        Assert.assertEquals(1, usersFromDB.size());

        Assert.assertEquals(users.get(10), usersFromDB.get(0));
    }

    @Test
    public void findLikeNotTest() {
        User user = users.get(3);

        users.remove(3);

        List<User> usersFromDB = repository.findByNameNotLike(user.getName());

        Assert.assertEquals(new HashSet<>(users), new HashSet<>(usersFromDB));
    }

    @Test
    public void findInTest() {
        Set<User> usersForSearch = new HashSet<>();
        usersForSearch.add(users.get(5));
        usersForSearch.add(users.get(10));
        usersForSearch.add(users.get(15));

        List<User> usersFromDB = repository.findByNameIn(usersForSearch.stream().map(User::getName)
                .collect(Collectors.toList()));

        Assert.assertEquals(usersForSearch.size(), usersFromDB.size());

        Assert.assertEquals(usersForSearch, new HashSet<>(usersFromDB));
    }

    @Test
    public void findNotInTest() {
        Set<User> usersForSearch = new HashSet<>();
        usersForSearch.add(users.get(5));
        usersForSearch.add(users.get(10));
        usersForSearch.add(users.get(15));

        users.remove(15);
        users.remove(10);
        users.remove(5);

        List<User> usersFromDB = repository.findByNameNotIn(usersForSearch.stream().map(User::getName)
                .collect(Collectors.toList()));

        Assert.assertEquals(users.size(), usersFromDB.size());

        Assert.assertEquals(new HashSet<>(users), new HashSet<>(usersFromDB));
    }

    @Test
    public void findExists() {
        List<User> usersFromDB = repository.findByNameExists();

        Assert.assertEquals(users.size(), usersFromDB.size());

        Assert.assertEquals(new HashSet<>(users), new HashSet<>(usersFromDB));
    }

    @Test
    public void findTrue() {
        User user = users.get(5);
        user.setEnabled(true);
        repository.save(user);

        List<User> usersFromDB = repository.findByEnabledTrue();

        Assert.assertEquals(1, usersFromDB.size());

        Assert.assertEquals(user, usersFromDB.get(0));
    }

    @Test
    public void findFalse() {
        User user = users.get(5);
        user.setEnabled(true);
        repository.save(user);

        users.remove(5);

        List<User> usersFromDB = repository.findByEnabledFalse();

        Assert.assertEquals(users.size(), usersFromDB.size());

        Assert.assertEquals(new HashSet<>(users), new HashSet<>(usersFromDB));
    }

    @Test
    public void findLessThanTest() {
        List<Integer> indexes = Arrays.asList(5, 10, 15);

        List<User> usersForSearch = new LinkedList<>();

        indexes.forEach(i-> {
            User user = users.get(i);
            user.setAge(19);
            usersForSearch.add(user);
        });

        repository.saveAll(usersForSearch);

        List<User> usersFromDB = repository.findByAgeLessThan(20);

        Assert.assertEquals(usersForSearch.size(), usersFromDB.size());

        Assert.assertEquals(new HashSet<>(usersForSearch), new HashSet<>(usersFromDB));
    }

    @Test
    public void findLessThanEqualTest() {
        List<Integer> indexes = Arrays.asList(5, 10, 15);

        List<User> usersForSearch = new LinkedList<>();

        indexes.forEach(i-> {
            User user = users.get(i);
            user.setAge(19);
            usersForSearch.add(user);
        });

        repository.saveAll(usersForSearch);

        List<User> usersFromDB = repository.findByAgeLessThanEqual(19);

        Assert.assertEquals(usersForSearch.size(), usersFromDB.size());

        Assert.assertEquals(new HashSet<>(usersForSearch), new HashSet<>(usersFromDB));
    }

    @Test
    public void findGreaterThanTest() {
        List<Integer> indexes = Arrays.asList(5, 10, 15);

        List<User> usersForSearch = new LinkedList<>();

        indexes.forEach(i-> {
            User user = users.get(i);
            user.setAge(30);
            usersForSearch.add(user);
        });

        repository.saveAll(usersForSearch);

        List<User> usersFromDB = repository.findByAgeGreaterThan(29);

        Assert.assertEquals(usersForSearch.size(), usersFromDB.size());

        Assert.assertEquals(new HashSet<>(usersForSearch), new HashSet<>(usersFromDB));
    }

    @Test
    public void findGreaterThanEqualTest() {
        List<Integer> indexes = Arrays.asList(5, 10, 15);

        List<User> usersForSearch = new LinkedList<>();

        indexes.forEach(i-> {
            User user = users.get(i);
            user.setAge(30);
            usersForSearch.add(user);
        });

        repository.saveAll(usersForSearch);

        List<User> usersFromDB = repository.findByAgeGreaterThanEqual(30);

        Assert.assertEquals(usersForSearch.size(), usersFromDB.size());

        Assert.assertEquals(new HashSet<>(usersForSearch), new HashSet<>(usersFromDB));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void notImplementedMethod() {
        List<User> users = UserUtils.getUsers();

        repository.saveAll(users);

        User user = users.get(3);

        repository.findByNameContaining(user.getName());
    }

}
