package com.mapr.springframework.data.maprdb.unit;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mapr.springframework.data.maprdb.model.User;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class EntityConverterUnitTest {

    @Test
    public void converterTest() throws IOException {
        User user = new User();
        user.setId("123");

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(user);
        Assert.assertTrue(json.contains("\"_id\""));

        User parsedUser = mapper.readValue(json, User.class);

        Assert.assertEquals(user, parsedUser);
    }

}
