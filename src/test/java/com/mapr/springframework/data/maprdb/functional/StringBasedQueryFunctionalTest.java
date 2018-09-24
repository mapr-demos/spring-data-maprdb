package com.mapr.springframework.data.maprdb.functional;

import com.mapr.springframework.data.maprdb.model.User;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashSet;
import java.util.List;

public class StringBasedQueryFunctionalTest extends AbstractFunctionalTests {

    @Test
    public void customFindQuery() {
        User user = users.get(5);
        user.setEnabled(true);
        repository.save(user);

        List<User> usersFromDB = repository.findCustom();

        Assert.assertEquals(1, usersFromDB.size());

        Assert.assertEquals(user, usersFromDB.get(0));
    }

    @Test
    public void customDeleteQuery() {
        User user = users.get(5);
        user.setEnabled(true);
        repository.save(user);
        users.remove(5);

        repository.deleteCustom();
        List<User> usersFromDB = repository.findAll();

        Assert.assertEquals(users.size(), usersFromDB.size());

        Assert.assertEquals(new HashSet<>(users), new HashSet<>(users));
    }

}
