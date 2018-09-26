package com.mapr.springframework.data.maprdb.core.mapping;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.RuntimeJsonMappingException;

import java.io.IOException;

public class MapRJsonConverter {

    private ObjectMapper mapper;

    public MapRJsonConverter() {
        mapper = new ObjectMapper();
        mapper.setAnnotationIntrospector(new MapRAnnotationIntrospector());
    }

    public <T> String toJson(T objectToConvert) {
        try {
            return mapper.writeValueAsString(objectToConvert);
        } catch (JsonProcessingException e) {
            RuntimeJsonMappingException exception = new RuntimeJsonMappingException(e.getMessage());
            exception.setStackTrace(e.getStackTrace());
            throw exception;
        }
    }

    public <T> T toObject(String json, Class<T> entityClass) {
        try {
            return mapper.readValue(json, entityClass);
        } catch (IOException e) {
            RuntimeException exception = new RuntimeException(e.getMessage());
            exception.setStackTrace(e.getStackTrace());
            throw exception;
        }
    }

}
