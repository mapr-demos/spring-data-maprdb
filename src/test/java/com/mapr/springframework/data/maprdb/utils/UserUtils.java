package com.mapr.springframework.data.maprdb.utils;

import com.mapr.springframework.data.maprdb.model.User;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class UserUtils {

    public final static int LIST_SIZE = 100;
    public final static int MIN_USER_NAME_LENGTH = 10;
    public final static int MAX_USER_NAME_LENGTH = 20;

    public static User getUser() {
        User user = new User();

        user.setName(getRandomString(MIN_USER_NAME_LENGTH, MAX_USER_NAME_LENGTH));

        return user;
    }

    public static List<User> getUsers() {
        List<User> users = new LinkedList<>();

        for(int i = 0; i < LIST_SIZE; i++)
            users.add(getUser());

        return users;
    }

    public static String getRandomString(int minLength, int maxLength) {
        int leftLimit = 97;
        int rightLimit = 122;
        int targetStringLength = ThreadLocalRandom.current().nextInt(minLength, maxLength - 1);

        Random random = new Random();

        StringBuilder buffer = new StringBuilder(targetStringLength);
        buffer.append("{");
        for (int i = 0; i < targetStringLength; i++) {
            int randomLimitedInt = leftLimit + (int)
                    (random.nextFloat() * (rightLimit - leftLimit + 1));
            buffer.append((char) randomLimitedInt);
        }
        buffer.append("}");

        return buffer.toString();
    }

}
