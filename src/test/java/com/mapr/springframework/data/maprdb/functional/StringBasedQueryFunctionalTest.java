package com.mapr.springframework.data.maprdb.functional;

import com.mapr.springframework.data.maprdb.functional.model.User;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class StringBasedQueryFunctionalTest extends AbstractFunctionalTests {

    @Test
    public void customQuery() {
        User user = users.get(5);
        user.setEnabled(true);
        repository.save(user);

        List<User> usersFromDB = repository.findCustom();

        Assert.assertEquals(1, usersFromDB.size());

        Assert.assertEquals(user, usersFromDB.get(0));
    }

}
