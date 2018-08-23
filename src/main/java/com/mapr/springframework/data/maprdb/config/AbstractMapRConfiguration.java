package com.mapr.springframework.data.maprdb.config;
import com.mapr.springframework.data.maprdb.core.MapROperations;
import com.mapr.springframework.data.maprdb.core.MapRTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Set;

@Configuration
public abstract class AbstractMapRConfiguration {

    protected abstract String database();

    @Bean
    public MapROperations maprOperations() throws Exception {
        return new MapRTemplate(database());
    }

    private Set<? extends Class<?>> getInitialEntitySet() throws ClassNotFoundException {
        return MapREntityClassScanner.scanForEntities(getEntityBasePackages());
    }

    protected String[] getEntityBasePackages() {
        return new String[] { getClass().getPackage().getName() };
    }
}
