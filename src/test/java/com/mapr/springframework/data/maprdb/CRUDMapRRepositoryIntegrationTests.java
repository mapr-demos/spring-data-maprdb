package com.mapr.springframework.data.maprdb;

import com.google.common.collect.Lists;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.ojai.store.exceptions.DocumentExistsException;

import java.util.*;
import java.util.stream.Collectors;

import static com.mapr.springframework.data.maprdb.UserUtils.LIST_SIZE;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { MapRTestConfiguration.class })
public class CRUDMapRRepositoryIntegrationTests {

    @Autowired
    public UserRepository repository;

    @Before
    @After
    public void delete() {
        repository.deleteAll();
    }

    @Test
    public void repositoryFactoryTest() {
        Assert.assertNotNull(repository);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void countTest() {
        Assert.assertEquals(0, repository.count());
    }

    @Test
    public void saveAndFindTest() {
        User user = UserUtils.getUser();

        repository.save(user);

        User userFromDB = repository.findById(user.getId()).get();

        Assert.assertEquals(user, userFromDB);
    }

    @Test
    public void replaceSaveTest() {
        User user = UserUtils.getUser();
        repository.save(user);
        user.setName("new_test_user");
        repository.save(user);

        User userFromDB = repository.findById(user.getId()).get();

        Assert.assertEquals(user, userFromDB);
    }

    @Test
    public void multipleSaveAndFindTest() {
        List<User> users = UserUtils.getUsers();

        repository.saveAll(users);

        List<User> usersFromDB = repository.findAll();

        Assert.assertEquals(new HashSet<>(users), new HashSet<>(usersFromDB));
    }

    @Test
    public void insertTest() {
        User user = UserUtils.getUser();

        repository.insert(user);

        User userFromDB = repository.findById(user.getId()).get();

        Assert.assertEquals(user, userFromDB);
    }

    @Test
    public void multipleInsertTest() {
        List<User> users = UserUtils.getUsers();

        repository.insert(users);

        List<User> usersFromDB = repository.findAll();

        Assert.assertEquals(new HashSet<>(users), new HashSet<>(usersFromDB));
    }

    @Test(expected = DocumentExistsException.class)
    public void insertReplaceTest() {
        User user = UserUtils.getUser();
        repository.insert(user);
        user.setName("new_test_user");
        repository.insert(user);
    }

    @Test
    public void findAllByIdTest() {
        List<User> users = UserUtils.getUsers();

        repository.saveAll(users);
        List<User> usersForSearch = Arrays.asList(users.get(5), users.get(10), users.get(20));
        List<String> ids = usersForSearch.stream().map(User::getId).collect(Collectors.toList());
        List<User> usersFromDB = repository.findAllById(ids);

        Assert.assertEquals(new HashSet<>(usersForSearch), new HashSet<>(usersFromDB));
    }

    @Test
    public void deleteTest() {
        User user1 = UserUtils.getUser();
        User user2 = UserUtils.getUser();
        repository.save(user1);
        repository.save(user2);

        repository.delete(user1);

        List<User> users = repository.findAll();

        Assert.assertEquals(1, users.size());
        Assert.assertEquals(user2, users.get(0));
    }

    @Test
    public void deleteByIdTest() {
        User user1 = UserUtils.getUser();
        User user2 = UserUtils.getUser();
        repository.save(user1);
        repository.save(user2);

        repository.deleteById(user1.getId());

        List<User> users = repository.findAll();

        Assert.assertEquals(1, users.size());
        Assert.assertEquals(user2, users.get(0));
    }

    @Test
    public void deleteAllTest() {
        repository.saveAll(UserUtils.getUsers());

        repository.deleteAll();

        Assert.assertEquals(0, repository.findAll().size());
    }

    @Test
    public void multipleDeleteTest() {
        List<User> users = UserUtils.getUsers();

        repository.saveAll(users);

        User user = users.get(3);

        users.remove(3);

        repository.deleteAll(users);

        User userFromDB = repository.findById(user.getId()).get();

        Assert.assertEquals(user, userFromDB);
    }
    
    @Test
    public void existByIdTest() {
        User user = UserUtils.getUser();
        
        Assert.assertFalse(repository.existsById(user.getId()));
        
        repository.save(user);

        Assert.assertTrue(repository.existsById(user.getId()));
    }

    //TODO Test for queries with over 5000 records
    @Test
    public void SortTest() {
        List<User> users = UserUtils.getUsers();

        repository.saveAll(users);

        List<User> usersFromDB = repository.findAll(new Sort(Sort.Direction.DESC,"name"));

        List<User> sortedUsers = Lists.reverse(users.stream().sorted(Comparator.comparing(User::getName))
                .collect(Collectors.toList()));

        Assert.assertEquals(sortedUsers.size(), usersFromDB.size());

        Assert.assertEquals(sortedUsers, usersFromDB);
    }

    //TODO Test for queries with over 5000 records
    @Test
    public void MultipleSortTest() {
        List<User> users = UserUtils.getUsers();

        repository.saveAll(users);

        List<User> usersFromDB = repository.findAll(new Sort(Sort.Direction.ASC,"name", "_id"));

        List<User> sortedUsers = users.stream().sorted(Comparator.comparing(User::getName).thenComparing(User::getId))
                .collect(Collectors.toList());

        Assert.assertEquals(sortedUsers.size(), usersFromDB.size());

        Assert.assertEquals(sortedUsers, usersFromDB);
    }

    @Test
    public void PageableTest() {
        List<User> users = UserUtils.getUsers();

        repository.saveAll(users);

        int usersPerPage = 10;
        int pageNumber = 1;
        Page<User> pagedUsers = repository.findAll(PageRequest.of(pageNumber, usersPerPage));


        Assert.assertEquals(usersPerPage, pagedUsers.getSize());

        Assert.assertEquals(LIST_SIZE, pagedUsers.getTotalElements());

        Assert.assertEquals(LIST_SIZE / usersPerPage, pagedUsers.getTotalPages());

        List<User> filteredUsers = users.stream().sorted(Comparator.comparing(User::getId))
                .skip(pageNumber * usersPerPage).limit(usersPerPage).collect(Collectors.toList());

        Assert.assertEquals(filteredUsers, pagedUsers.getContent());
    }

    @Test
    public void PageableWithSortTest() {
        List<User> users = UserUtils.getUsers();

        repository.saveAll(users);

        int usersPerPage = 20;
        int pageNumber = 2;
        Page<User> pagedUsers = repository.findAll(
                PageRequest.of(pageNumber, usersPerPage, Sort.Direction.ASC, "name"));

        Assert.assertEquals(usersPerPage, pagedUsers.getSize());

        Assert.assertEquals(LIST_SIZE, pagedUsers.getTotalElements());

        Assert.assertEquals(LIST_SIZE / usersPerPage, pagedUsers.getTotalPages());

        List<User> filteredUsers = users.stream().sorted(Comparator.comparing(User::getName))
                .skip(pageNumber * usersPerPage).limit(usersPerPage).collect(Collectors.toList());

        Assert.assertEquals(filteredUsers, pagedUsers.getContent());
    }

}
