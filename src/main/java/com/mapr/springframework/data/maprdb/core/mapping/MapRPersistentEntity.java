package com.mapr.springframework.data.maprdb.core.mapping;

import org.springframework.data.mapping.PersistentEntity;

public interface MapRPersistentEntity<T> extends PersistentEntity<T, MapRPersistentProperty> {

    String getCollection();
}
