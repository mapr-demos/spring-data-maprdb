package com.mapr.springframework.data.maprdb.config;
import com.mapr.springframework.data.maprdb.core.MapROperations;
import com.mapr.springframework.data.maprdb.core.MapRTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Set;

@Configuration
public abstract class AbstractMapRConfiguration {

    protected abstract String getDatabaseName();

    protected abstract String getHost();

    protected abstract String getUsername();

    protected abstract String getPassword();

    @Bean
    public MapROperations maprOperations() {
        return new MapRTemplate(getDatabaseName(), getHost(), getUsername(), getPassword());
    }

    protected String[] getEntityBasePackages() {
        return new String[] { getClass().getPackage().getName() };
    }
}
