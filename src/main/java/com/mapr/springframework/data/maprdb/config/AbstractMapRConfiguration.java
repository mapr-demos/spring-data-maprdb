package com.mapr.springframework.data.maprdb.config;
import com.mapr.springframework.data.maprdb.core.MapROperations;
import com.mapr.springframework.data.maprdb.core.MapRTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public abstract class AbstractMapRConfiguration {

    protected abstract String database();

    @Bean
    public MapROperations maprOperations() throws Exception {
        return new MapRTemplate(database());
    }

}
