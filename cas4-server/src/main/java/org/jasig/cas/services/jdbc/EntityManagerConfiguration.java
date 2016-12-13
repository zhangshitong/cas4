package org.jasig.cas.services.jdbc;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.annotation.Resource;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by stzhang on 2016/10/31.
 */
@Configuration
@EnableTransactionManagement
public class EntityManagerConfiguration {

    private String[] backagesToScan = new String[]{"org.jasig.cas.services.jdbc.domain"};

    @Value("${cas.jdbc.gen.ddl: true}")
    private boolean databaseGenDDL;

    @Value("${cas.jdbc.show.sql: true}")
    private boolean databaseShowSql ;

    @Value("${cas.jdbc.ddl.auto:create-drop}")
    private String databaseHbm2ddl;

    @Value("${cas.jdbc.hibernate.batchSize:1}")
    private Integer databaseBatchSize ;

    @Value("${cas.jdbc.hibernate.dialect:}")
    private String databaseHbmDialect;

    @Resource(name = "authDataSource")
    private DataSource dataSource;

    @Bean(name = "entityManagerFactory")
    public EntityManagerFactory entityManagerFactory() {
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setGenerateDdl(databaseGenDDL);
        vendorAdapter.setShowSql(databaseShowSql);
        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        factory.setJpaVendorAdapter(vendorAdapter);
        factory.setPackagesToScan(backagesToScan);
        factory.setDataSource(dataSource);

        Map<String, Object> jpaProperties = new HashMap<>();
        jpaProperties.put("hibernate.hbm2ddl.auto", databaseHbm2ddl);
        jpaProperties.put("hibernate.jdbc.batch_size", databaseBatchSize);
        jpaProperties.put("hibernate.dialect", databaseHbmDialect);
        factory.setJpaPropertyMap(jpaProperties);
        factory.afterPropertiesSet();
        return factory.getObject();
    }


    @Bean(name = "transactionManager")
    public PlatformTransactionManager transactionManager() {
        JpaTransactionManager txManager = new JpaTransactionManager();
        txManager.setEntityManagerFactory(entityManagerFactory());
        return txManager;
    }

}
