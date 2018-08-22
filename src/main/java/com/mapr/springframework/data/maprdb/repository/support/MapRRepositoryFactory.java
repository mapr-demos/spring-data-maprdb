package com.mapr.springframework.data.maprdb.repository.support;

import org.springframework.data.repository.core.EntityInformation;
import org.springframework.data.repository.core.RepositoryInformation;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;

public class MapRRepositoryFactory extends RepositoryFactorySupport {
    @Override
    public <T, ID> EntityInformation<T, ID> getEntityInformation(Class<T> domainClass) {
        return null;
    }

    @Override
    protected Object getTargetRepository(RepositoryInformation metadata) {
        return null;
    }

    @Override
    protected Class<?> getRepositoryBaseClass(RepositoryMetadata metadata) {
        return null;
    }
}
