package com.mapr.springframework.data.maprdb.core.query;

import org.springframework.data.repository.query.Parameters;
import org.springframework.data.repository.query.ParametersParameterAccessor;

public class MapRParametersParameterAccessor extends ParametersParameterAccessor implements MapRParameterAcessor {

    public MapRParametersParameterAccessor(Parameters<?, ?> parameters, Object[] values) {
        super(parameters, values);
    }

}
