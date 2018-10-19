package com.mapr.springframework.data.maprdb.unit;

import com.mapr.springframework.data.maprdb.core.MapROperations;
import com.mapr.springframework.data.maprdb.core.MapRTemplate;
import com.mapr.springframework.data.maprdb.model.User;
import com.mapr.springframework.data.maprdb.repository.support.MapRRepositoryFactory;
import com.mapr.springframework.data.maprdb.unit.repository.UserRepository;
import com.mapr.springframework.data.maprdb.utils.UserUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.ojai.store.Connection;
import org.ojai.store.DriverManager;
import org.ojai.store.Query;

import java.util.List;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.data.repository.query.QueryLookupStrategy.Key.CREATE_IF_NOT_FOUND;

public class ConditionBasedQueryUnitTests {

    public Connection connection = DriverManager.getConnection("ojai:mapr:");
    public MapROperations operations;
    public MapRRepositoryFactory factory;
    public UserRepository repository;

    public List users;

    @Before
    public void init() {
        users = UserUtils.getUsers();

        operations = mock(MapRTemplate.class);
        when(operations.getConnection()).thenReturn(connection);
        when(operations.execute(any(Query.class), any())).thenReturn(users);

        factory = new MapRRepositoryFactory(operations);
        factory.setQueryLookupStrategyKey(CREATE_IF_NOT_FOUND);

        repository = factory.getRepository(UserRepository.class);
    }

    @Test
    public void streamReturnTest() {
        Stream stream = repository.findByEnabledTrue();

        Assert.assertEquals(users.size(), stream.count());
    }

    @Test
    public void collectionReturnTest() {
        List list = repository.findByEnabledFalse();

        Assert.assertEquals(users.size(), list.size());
    }

    @Test
    public void getOneReturnTest() {
        User user = repository.findByName("");

        Assert.assertEquals(users.get(0), user);
    }

    @Test
    public void existsMethodTest() {
        Assert.assertTrue(repository.existsByEnabledTrue());
    }

    @Test
    public void countMethodTest() {
        Assert.assertEquals(users.size(), repository.countByEnabledTrue());
    }

}
