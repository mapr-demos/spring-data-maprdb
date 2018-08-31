package com.mapr.springframework.data.maprdb;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ComplexUserRepository  extends CrudRepository<User, String> {
    List<User> findByName(String name);

    List<User> findByNameNot(String name);

    List<User> findByNameContaining(String name);
}
