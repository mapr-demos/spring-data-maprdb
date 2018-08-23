package com.mapr.springframework.data.maprdb.core.mapping;

import org.springframework.data.mapping.Association;
import org.springframework.data.mapping.PersistentEntity;
import org.springframework.data.mapping.model.AnnotationBasedPersistentProperty;
import org.springframework.data.mapping.model.Property;
import org.springframework.data.mapping.model.SimpleTypeHolder;

public class BasicMapRPersistentProperty extends AnnotationBasedPersistentProperty<MapRPersistentProperty>
        implements MapRPersistentProperty {

    public BasicMapRPersistentProperty(Property property, PersistentEntity<?, MapRPersistentProperty> owner, SimpleTypeHolder simpleTypeHolder) {
        super(property, owner, simpleTypeHolder);
    }

    @Override
    public String getFieldName() {
        return null;
    }

    @Override
    protected Association<MapRPersistentProperty> createAssociation() {
        return null;
    }
}
