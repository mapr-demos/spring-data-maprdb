package com.mapr.springframework.data.maprdb.repository.config;

import org.springframework.data.repository.config.RepositoryBeanDefinitionRegistrarSupport;
import org.springframework.data.repository.config.RepositoryConfigurationExtension;

import java.lang.annotation.Annotation;

public class MapRRepositoriesRegistrar extends RepositoryBeanDefinitionRegistrarSupport {

    @Override
    protected Class<? extends Annotation> getAnnotation() {
        return EnableMapRRepository.class;
    }

    @Override
    protected RepositoryConfigurationExtension getExtension() {
        return new MapRRepositoryConfigurationExtension();
    }

}
