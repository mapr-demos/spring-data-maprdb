package com.mapr.springframework.data.maprdb.core.mapping;

import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import org.springframework.data.annotation.Id;

public class MapRAnnotationIntrospector extends JacksonAnnotationIntrospector {

    @Override
    public PropertyName findNameForSerialization(Annotated a) {
        Id id = _findAnnotation(a, Id.class);
        MapRId mapRId = _findAnnotation(a, MapRId.class);

        if(id != null || mapRId != null)
            return PropertyName.construct("_id");
        else
            return super.findNameForSerialization(a);
    }

    @Override
    public PropertyName findNameForDeserialization(Annotated a) {
        Id id = _findAnnotation(a, Id.class);
        MapRId mapRId = _findAnnotation(a, MapRId.class);

        if(id != null || mapRId != null)
            return PropertyName.construct("_id");
        else
            return super.findNameForDeserialization(a);
    }

}
