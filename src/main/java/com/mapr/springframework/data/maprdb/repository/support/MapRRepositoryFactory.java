package com.mapr.springframework.data.maprdb.repository.support;

import com.mapr.springframework.data.maprdb.core.MapROperations;
import com.mapr.springframework.data.maprdb.repository.MapRPersistentEntityInformation;
import org.springframework.data.repository.core.EntityInformation;
import org.springframework.data.repository.core.RepositoryInformation;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;

public class MapRRepositoryFactory extends RepositoryFactorySupport {

    private final MapROperations mapROperations;

    public MapRRepositoryFactory(final MapROperations mapROperations) {
        this.mapROperations = mapROperations;
    }

    @Override
    public <T, ID> EntityInformation<T, ID> getEntityInformation(Class<T> domainClass) {
        return new MapRPersistentEntityInformation<>(
                null);
    }

    @Override
    protected Object getTargetRepository(final RepositoryInformation metadata) {
        return new SimpleMapRRepository<>(mapROperations, metadata.getDomainType());
    }

    @Override
    protected Class<?> getRepositoryBaseClass(final RepositoryMetadata metadata) {
        return SimpleMapRRepository.class;
    }
}
