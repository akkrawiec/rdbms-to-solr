package org.ak.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import com.mchange.v2.c3p0.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;


import javax.sql.DataSource;
import java.beans.PropertyVetoException;

@Configuration
@PropertySource(value = { "classpath:jdbc.properties" })
@EnableAutoConfiguration
public class JdbcConfig {
    public static final String ID = "id";

    @Bean
    public DataSource  dataSource() throws PropertyVetoException {
        ComboPooledDataSource dataSource = new ComboPooledDataSource();
        dataSource.setMinPoolSize(Integer.parseInt(env.getProperty("db.minPoolSize")));
        dataSource.setMaxPoolSize(Integer.parseInt(env.getProperty("db.maxPoolSize")));
        dataSource.setJdbcUrl(env.getProperty("db.url"));
        dataSource.setDriverClass(env.getProperty("db.driverClassName"));
        //dataSource.setTestConnectionOnCheckout(true);
        return dataSource;
    }

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean
    public NamedParameterJdbcTemplate npJdbcTemplate(DataSource dataSource) {
        return new NamedParameterJdbcTemplate(dataSource);
    }

    @Autowired
    private Environment env;

    @Autowired
    private DataSource dataSource;
}

