package com.mapr.springframework.data.maprdb.core.mapping;

import org.springframework.data.mapping.model.BasicPersistentEntity;
import org.springframework.data.util.TypeInformation;

public class BasicMapRPersistentEntity<T> extends BasicPersistentEntity<T, MapRPersistentProperty>
        implements MapRPersistentEntity<T> {

    public BasicMapRPersistentEntity(TypeInformation<T> information) {
        super(information);
    }

    @Override
    public String getCollection() {
        return null;
    }

}
