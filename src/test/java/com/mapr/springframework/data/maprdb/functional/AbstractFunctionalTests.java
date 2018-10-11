package com.mapr.springframework.data.maprdb.functional;

import com.mapr.db.MapRDB;
import com.mapr.springframework.data.maprdb.core.MapROperations;
import com.mapr.springframework.data.maprdb.model.User;
import com.mapr.springframework.data.maprdb.functional.repository.ComplexUserRepository;
import com.mapr.springframework.data.maprdb.utils.UserUtils;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { MapRTestConfiguration.class })
public abstract class AbstractFunctionalTests {

    @Autowired
    public ComplexUserRepository repository;

    @Autowired
    public MapROperations operations;

    public List<User> users;

    @Before
    public void init() {
        if(!operations.tableExists(User.class))
            operations.createTable(User.class);

        delete();
        users = UserUtils.getUsers();
        users = repository.saveAll(users);
    }

    @After
    public void delete() {
        repository.deleteAll();
    }

}
