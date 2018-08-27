package com.mapr.springframework.data.maprdb;

import com.mapr.springframework.data.maprdb.repository.MapRRepository;

public interface UserRepository extends MapRRepository <User, String> {
}
