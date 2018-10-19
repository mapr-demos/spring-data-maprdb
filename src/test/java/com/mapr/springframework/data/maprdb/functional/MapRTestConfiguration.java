package com.mapr.springframework.data.maprdb.functional;

import com.mapr.springframework.data.maprdb.config.AbstractMapRConfiguration;
import com.mapr.springframework.data.maprdb.repository.config.EnableMapRRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@EnableMapRRepository
@PropertySource("classpath:tests.properties")
public class MapRTestConfiguration extends AbstractMapRConfiguration {

    @Value("${database.name}")
    public String databaseName;

    @Value("${database.host}")
    public String databaseHost;

    @Value("${database.username}")
    public String databaseUsername;

    @Value("${database.password}")
    public String databasePassword;

    @Override
    protected String getDatabaseName() {
        return databaseName;
    }

    @Override
    protected String getHost() {
        return databaseHost;
    }

    @Override
    protected String getUsername() {
        return databaseUsername;
    }

    @Override
    protected String getPassword() {
        return databasePassword;
    }

}
