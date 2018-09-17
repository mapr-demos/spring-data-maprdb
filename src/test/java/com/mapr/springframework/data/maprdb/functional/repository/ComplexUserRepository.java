package com.mapr.springframework.data.maprdb.functional.repository;

import com.mapr.springframework.data.maprdb.functional.model.User;
import com.mapr.springframework.data.maprdb.repository.MapRRepository;
import com.mapr.springframework.data.maprdb.repository.Query;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ComplexUserRepository  extends MapRRepository<User, String> {
    List<User> findByName(String name);

    List<User> findByNameNot(String name);

    List<User> findByNameLike(String name);

    List<User> findByNameNotLike(String name);

    List<User> findByNameIn(List<String> names);

    List<User> findByNameNotIn(List<String> names);

    List<User> findByNameExists();

    List<User> findByEnabledTrue();

    List<User> findByEnabledFalse();

    List<User> findByAgeLessThan(int age);

    List<User> findByAgeLessThanEqual(int age);

    List<User> findByAgeGreaterThan(int age);

    List<User> findByAgeGreaterThanEqual(int age);

    List<User> findByNameOrEnabledTrueOrAgeGreaterThan(String name, int age);

    List<User> findByNameAndEnabledTrueAndAgeGreaterThan(String name, int age);

    List<User> findFirst10ByEnabledFalse();

    List<User> findTop10ByEnabledFalse();

    List<User> findByEnabledFalse(Pageable page);

    @Query("{\"$and\":[ {\"$eq\":{\"enabled\":true}}]}")
    List<User> findCustom();

    List<User> findByNameContaining(String name);
}
