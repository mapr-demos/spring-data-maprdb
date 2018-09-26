package com.mapr.springframework.data.maprdb.unit;

import com.mapr.springframework.data.maprdb.core.mapping.MapRJsonConverter;
import com.mapr.springframework.data.maprdb.model.User;
import org.junit.Assert;
import org.junit.Test;

public class MapRJsonConverterUnitTest {

    public MapRJsonConverter converter = new MapRJsonConverter();

    @Test
    public void converterTest() {
        User user = new User();
        user.setId("123");

        String json = converter.toJson(user);
        Assert.assertTrue(json.contains("\"_id\""));

        User parsedUser = converter.toObject(json, User.class);
        Assert.assertEquals(user, parsedUser);
    }

}
