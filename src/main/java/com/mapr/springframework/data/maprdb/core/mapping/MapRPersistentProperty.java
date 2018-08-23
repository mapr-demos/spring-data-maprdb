package com.mapr.springframework.data.maprdb.core.mapping;

import org.springframework.data.mapping.PersistentProperty;

public interface MapRPersistentProperty extends PersistentProperty<MapRPersistentProperty> {

    String getFieldName();
}
