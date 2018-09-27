package com.mapr.springframework.data.maprdb.core.mapping;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import org.springframework.data.annotation.Id;

public class MapRAnnotationIntrospector extends JacksonAnnotationIntrospector {

    @Override
    public PropertyName findNameForSerialization(Annotated a) {
        if(isId(a))
            return PropertyName.construct("_id");
        else
            return super.findNameForSerialization(a);
    }

    @Override
    public PropertyName findNameForDeserialization(Annotated a) {
        if(isId(a))
            return PropertyName.construct("_id");
        else
            return super.findNameForDeserialization(a);
    }

    @Override
    public JsonInclude.Value findPropertyInclusion(Annotated a) {
        if(isId(a))
            return JsonInclude.Value.construct(JsonInclude.Include.NON_NULL, JsonInclude.Include.NON_NULL);
        else
            return super.findPropertyInclusion(a);
    }

    private boolean isId(Annotated a) {
        Id id = _findAnnotation(a, Id.class);
        MapRId mapRId = _findAnnotation(a, MapRId.class);

        return id != null || mapRId != null;
    }

}
