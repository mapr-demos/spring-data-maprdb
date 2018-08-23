package com.mapr.springframework.data.maprdb.repository.support;

import com.mapr.springframework.data.maprdb.core.MapROperations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.core.support.RepositoryFactoryBeanSupport;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;
import org.springframework.util.Assert;

import java.io.Serializable;

public class MapRRepositoryFactoryBean<T extends Repository<S, ID>, S, ID extends Serializable>
        extends RepositoryFactoryBeanSupport<T, S, ID> {

    private MapROperations mapROperations;

    @Autowired
    protected MapRRepositoryFactoryBean(Class<? extends T> repositoryInterface) {
        super(repositoryInterface);
    }

    @Autowired
    public void setMapROperations(final MapROperations mapROperations) {
        this.mapROperations = mapROperations;
    }

    @Override
    protected RepositoryFactorySupport createRepositoryFactory() {
        Assert.notNull(mapROperations, "mapROperations not configured");
        return new MapRRepositoryFactory(mapROperations);
    }
}
