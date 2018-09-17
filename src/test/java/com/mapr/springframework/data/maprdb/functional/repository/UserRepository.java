package com.mapr.springframework.data.maprdb.functional.repository;

import com.mapr.springframework.data.maprdb.functional.model.User;
import com.mapr.springframework.data.maprdb.repository.MapRRepository;

public interface UserRepository extends MapRRepository <User, String> {
}
