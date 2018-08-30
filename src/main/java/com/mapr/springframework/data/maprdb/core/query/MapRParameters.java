package com.mapr.springframework.data.maprdb.core.query;

import org.springframework.core.MethodParameter;
import org.springframework.data.repository.query.Parameter;
import org.springframework.data.repository.query.Parameters;

import java.lang.reflect.Method;
import java.util.List;

public class MapRParameters extends Parameters<MapRParameters, MapRParameters.MapRParameter> {

    public MapRParameters(Method method) {
        super(method);
    }

    @Override
    protected MapRParameter createParameter(MethodParameter parameter) {
        return null;
    }

    @Override
    protected MapRParameters createFrom(List<MapRParameter> parameters) {
        return null;
    }

    class MapRParameter extends Parameter {

        protected MapRParameter(MethodParameter parameter) {
            super(parameter);
        }

    }

}
