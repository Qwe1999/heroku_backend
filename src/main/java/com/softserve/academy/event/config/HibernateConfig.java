package com.softserve.academy.event.config;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.beans.PropertyVetoException;
import java.util.Objects;
import java.util.Properties;

import static org.hibernate.cfg.AvailableSettings.*;

@Configuration
@PropertySource("classpath:database.properties")
@ComponentScan(basePackages = "com.softserve.academy.event.repository")
@EnableTransactionManagement
@Slf4j
public class HibernateConfig {

    private static final String ENTITY_PACKAGE = "hibernate.entity.package";

    private final Environment environment;

    @Autowired
    public HibernateConfig(Environment environment) {
        this.environment = environment;
    }

    @Bean
    public DataSource getDataSource() {
        ComboPooledDataSource dataSource = new ComboPooledDataSource();
        try {
            dataSource.setDriverClass(Objects.requireNonNull(environment.getProperty(DRIVER)));
        } catch (PropertyVetoException e) {
            log.error(e.getMessage());
            System.exit(1);
        }

        String dbUrl = System.getenv("JDBC_DATABASE_URL");
        String username = System.getenv("JDBC_DATABASE_USERNAME");
        String password = System.getenv("JDBC_DATABASE_PASSWORD");

        dataSource.setJdbcUrl(dbUrl);
        dataSource.setUser(username);
        dataSource.setPassword(password);
//        dataSource.setJdbcUrl(Objects.requireNonNull(environment.getProperty(URL)));
//        dataSource.setUser(Objects.requireNonNull(environment.getProperty(USER)));
//        dataSource.setPassword(Objects.requireNonNull(environment.getProperty(PASS)));
        return dataSource;
    }

    @Bean
    public LocalSessionFactoryBean getSessionFactory() {
        LocalSessionFactoryBean sessionFactoryBean = new LocalSessionFactoryBean();
        sessionFactoryBean.setDataSource(getDataSource());
        Properties properties = new Properties();

        properties.put(SHOW_SQL, Objects.requireNonNull(environment.getProperty(SHOW_SQL)));
        properties.put(HBM2DDL_AUTO, Objects.requireNonNull(environment.getProperty(HBM2DDL_AUTO)));
        properties.put(DIALECT, Objects.requireNonNull(environment.getProperty(DIALECT)));

        properties.put(C3P0_MIN_SIZE, Objects.requireNonNull(environment.getProperty(C3P0_MIN_SIZE)));
        properties.put(C3P0_MAX_SIZE, Objects.requireNonNull(environment.getProperty(C3P0_MAX_SIZE)));
        properties.put(C3P0_ACQUIRE_INCREMENT, Objects.requireNonNull(environment.getProperty(C3P0_ACQUIRE_INCREMENT)));
        properties.put(C3P0_TIMEOUT, Objects.requireNonNull(environment.getProperty(C3P0_TIMEOUT)));
        properties.put(C3P0_MAX_STATEMENTS, Objects.requireNonNull(environment.getProperty(C3P0_MAX_STATEMENTS)));

        sessionFactoryBean.setHibernateProperties(properties);
        sessionFactoryBean.setPackagesToScan(environment.getProperty(ENTITY_PACKAGE));

        return sessionFactoryBean;
    }

    @Bean
    public HibernateTransactionManager getTransactionManager() {
        HibernateTransactionManager transactionManager = new HibernateTransactionManager();
        transactionManager.setSessionFactory(getSessionFactory().getObject());
        return transactionManager;
    }
}
