package com.mapr.springframework.data.maprdb.unit.repository;

import com.mapr.springframework.data.maprdb.model.User;
import com.mapr.springframework.data.maprdb.repository.MapRRepository;

import java.util.List;
import java.util.stream.Stream;

public interface UserRepository extends MapRRepository<User, String> {

    Stream<User> findByEnabledTrue();

    List<User> findByEnabledFalse();

    User findByName(String name);

    boolean existsByEnabledTrue();

    int countByEnabledTrue();

}
