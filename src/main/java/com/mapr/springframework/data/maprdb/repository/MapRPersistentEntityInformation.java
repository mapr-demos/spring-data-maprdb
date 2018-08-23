package com.mapr.springframework.data.maprdb.repository;

import org.springframework.data.mapping.PersistentEntity;
import org.springframework.data.repository.core.support.PersistentEntityInformation;

public class MapRPersistentEntityInformation<T, ID> extends PersistentEntityInformation<T, ID>
        implements MapREntityInformation<T, ID> {

    public MapRPersistentEntityInformation(PersistentEntity<T, ?> entity) {
        super(entity);
    }
}
