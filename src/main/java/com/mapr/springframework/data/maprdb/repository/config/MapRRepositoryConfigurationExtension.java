package com.mapr.springframework.data.maprdb.repository.config;

import com.mapr.springframework.data.maprdb.core.mapping.Document;
import com.mapr.springframework.data.maprdb.repository.MapRRepository;
import com.mapr.springframework.data.maprdb.repository.support.MapRRepositoryFactoryBean;
import org.springframework.data.repository.config.RepositoryConfigurationExtensionSupport;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.Collections;

public class MapRRepositoryConfigurationExtension extends RepositoryConfigurationExtensionSupport {

    @Override
    public String getModuleName() {
        return "MapRDB";
    }

    @Override
    protected String getModulePrefix() {
        return "mapr";
    }

    @Override
    public String getRepositoryFactoryBeanClassName() {
        return MapRRepositoryFactoryBean.class.getName();
    }

    @Override
    protected Collection<Class<? extends Annotation>> getIdentifyingAnnotations() {
        return Collections.singleton(Document.class);
    }

    @Override
    protected Collection<Class<?>> getIdentifyingTypes() {
        return Collections.singleton(MapRRepository.class);
    }

}
