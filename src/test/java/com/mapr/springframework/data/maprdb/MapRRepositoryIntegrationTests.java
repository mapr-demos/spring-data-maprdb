package com.mapr.springframework.data.maprdb;

import com.mapr.springframework.data.maprdb.config.AbstractMapRConfiguration;
import com.mapr.springframework.data.maprdb.repository.config.EnableMapRRepository;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.ojai.store.exceptions.DocumentExistsException;

import java.util.*;
import java.util.stream.Collectors;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class MapRRepositoryIntegrationTests {

    public final static int LIST_SIZE = 100;
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
    public UserRepository userRepository;

    @After
    public void delete() {
        userRepository.deleteAll();
    }

    @Test
    public void repositoryFactoryTest() {
        Assert.assertNotNull(userRepository);
    }


    @Test(expected = UnsupportedOperationException.class)
            public void countTest() {
        Assert.assertEquals(0, userRepository.count());
    }

    @Test
    public void saveAndFindTest() {
        User user = getUser();

        userRepository.save(user);

        User userFromDB = userRepository.findById(user.getId()).get();

        Assert.assertEquals(user, userFromDB);
    }

    @Test
    public void replaceSaveTest() {
        User user = getUser();
        userRepository.save(user);
        user.setName("new_test_user");
        userRepository.save(user);

        User userFromDB = userRepository.findById(user.getId()).get();

        Assert.assertEquals(user, userFromDB);
    }

    @Test
    public void multipleSaveAndFindTest() {
        List<User> users = getUsers();

        userRepository.saveAll(users);

        List<User> usersFromDB = userRepository.findAll();

        Assert.assertEquals(new HashSet<>(users), new HashSet<>(usersFromDB));
    }

    @Test
    public void insertTest() {
        User user = getUser();

        userRepository.insert(user);

        User userFromDB = userRepository.findById(user.getId()).get();

        Assert.assertEquals(user, userFromDB);
    }

    @Test
    public void multipleInsertTest() {
        List<User> users = getUsers();

        userRepository.insert(users);

        List<User> usersFromDB = userRepository.findAll();

        Assert.assertEquals(new HashSet<>(users), new HashSet<>(usersFromDB));
    }

    @Test(expected = DocumentExistsException.class)
    public void insertReplaceTest() {
        User user = getUser();
        userRepository.insert(user);
        user.setName("new_test_user");
        userRepository.insert(user);
    }

    @Test
    public void findAllByIdTest() {
        List<User> users = getUsers();

        userRepository.saveAll(users);
        List<User> usersForSearch = Arrays.asList(users.get(5), users.get(10), users.get(20));
        List<String> ids = usersForSearch.stream().map(User::getId).collect(Collectors.toList());
        List<User> usersFromDB = userRepository.findAllById(ids);

        Assert.assertEquals(new HashSet<>(usersForSearch), new HashSet<>(usersFromDB));
    }

    @Test
    public void deleteTest() {
        User user1 = getUser();
        User user2 = getUser();
        userRepository.save(user1);
        userRepository.save(user2);

        userRepository.delete(user1);

        List<User> users = userRepository.findAll();

        Assert.assertEquals(1, users.size());
        Assert.assertEquals(user2, users.get(0));
    }

    @Test
    public void deleteByIdTest() {
        User user1 = getUser();
        User user2 = getUser();
        userRepository.save(user1);
        userRepository.save(user2);

        userRepository.deleteById(user1.getId());

        List<User> users = userRepository.findAll();

        Assert.assertEquals(1, users.size());
        Assert.assertEquals(user2, users.get(0));
    }

    @Test
    public void deleteAllTest() {
        userRepository.saveAll(getUsers());

        userRepository.deleteAll();

        Assert.assertEquals(0, userRepository.findAll().size());
    }

    @Test
    public void multipleDeleteTest() {
        List<User> users = getUsers();

        userRepository.saveAll(users);

        User user = users.get(3);

        users.remove(3);

        userRepository.deleteAll(users);

        User userFromDB = userRepository.findById(user.getId()).get();

        Assert.assertEquals(user, userFromDB);
    }

    public User getUser() {
        User user = new User();

        user.setId(UUID.randomUUID().toString());
        user.setName("test_user");

        return user;
    }

    public List<User> getUsers() {
        List<User> users = new LinkedList<>();

        for(int i = 0; i < LIST_SIZE; i++)
            users.add(getUser());

        return users;
    }

}
