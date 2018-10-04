package com.mapr.springframework.data.maprdb.core.mapping;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

public class MapRJsonConverter {

    private ObjectMapper mapper;

    public MapRJsonConverter() {
        mapper = new ObjectMapper();
        mapper.setAnnotationIntrospector(new MapRAnnotationIntrospector());
    }

    public <T> Map toJson(T objectToConvert) {
        return mapper.convertValue(objectToConvert, Map.class);
    }

    public <T> T toObject(Map json, Class<T> entityClass) {
        return mapper.convertValue(json, entityClass);
    }
}
