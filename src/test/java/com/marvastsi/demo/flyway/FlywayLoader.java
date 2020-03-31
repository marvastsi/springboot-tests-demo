package com.marvastsi.demo.flyway;

import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.SystemConfiguration;
import org.flywaydb.core.Flyway;

/**
 * FlywayLoader
 */
public class FlywayLoader {

    private Flyway flyway;

    public static FlywayLoader build() {
        return new FlywayLoader();
    }

    public FlywayLoader load() throws ConfigurationException {
        CompositeConfiguration config = new CompositeConfiguration();
        config.addConfiguration(new SystemConfiguration());
        config.addConfiguration(new PropertiesConfiguration("application.properties"));
        String url = config.getString("spring.datasource.url");
        String user = config.getString("spring.datasource.username");
        String pass = config.getString("spring.datasource.password");
        this.flyway = Flyway.configure().dataSource(url, user, pass).load();
        return this;
    }

    public FlywayLoader clean() {
        this.flyway.clean();
        return this;
    }

    public FlywayLoader migrate() {
        this.flyway.migrate();
        return this;
    }
}