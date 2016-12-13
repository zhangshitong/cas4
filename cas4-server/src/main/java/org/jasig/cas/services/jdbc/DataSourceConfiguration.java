package org.jasig.cas.services.jdbc;

import org.apache.commons.dbcp.BasicDataSourceFactory;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.Map;
import java.util.Properties;

/**
 * Created by stzhang on 2016/10/31.
 */
@Configuration
public class DataSourceConfiguration implements ApplicationContextAware{
    private static final Logger LOGGER = LoggerFactory.getLogger(DataSourceConfiguration.class);

    private ApplicationContext context;

    @Value("${cas.jdbc.driverClass:}")
    private String driverClass;

    @Value("${cas.jdbc.url:}")
    private String jdbcUrl;

    @Value("${cas.jdbc.user:}")
    private String user;

    @Value("${cas.jdbc.password:}")
    private String password;

    @Value("${cas.jdbc.pool.minSize:6}")
    private Integer initialPoolSize;

    @Value("${cas.jdbc.pool.minSize:6}")
    private Integer minPoolSize;

    @Value("${cas.jdbc.pool.maxSize:18}")
    private Integer maxPoolSize;

    @Value("${cas.jdbc.pool.maxIdleTime:1000}")
    private Integer maxIdleTime;

    @Value("${cas.jdbc.pool.maxWait:2000}")
    private Integer maxWaitTime;

    @Bean(name="authDataSource")
    public DataSource authDataSource() throws Exception{
        Map<String, DataSource> factory =  context.getBeansOfType(DataSource.class);
        if(factory != null && !factory.isEmpty()){
            DataSource ds = factory.get("cloudDataSource");
            if(ds != null){
                LOGGER.debug("Use exist DataSource. beanName:{} ", "cloudDataSource");
                return ds;
            }
        }
        //create a new Datasource.
        return  createBasicDataSource();
    }


    /**
     * create new BasicDataSource
     * @return
     */
    private DataSource createBasicDataSource() throws Exception{
        if(StringUtils.isNotEmpty(driverClass) && StringUtils.isNotEmpty(jdbcUrl)) {
            Properties dataSourceProperties = new Properties();
            dataSourceProperties.setProperty("driverClassName", driverClass);
            dataSourceProperties.setProperty("defaultAutoCommit", "true");
            dataSourceProperties.setProperty("initialSize", initialPoolSize + "");
            dataSourceProperties.setProperty("maxActive", maxPoolSize + "");
            dataSourceProperties.setProperty("testOnBorrow", "true");
            dataSourceProperties.setProperty("minIdle", maxIdleTime + "");
            dataSourceProperties.setProperty("maxWait", maxWaitTime + "");
            dataSourceProperties.setProperty("url", jdbcUrl);
            dataSourceProperties.setProperty("username", user);
            dataSourceProperties.setProperty("password", password);

            LOGGER.info("create BasicDatasource using properties: {}", dataSourceProperties);
            DataSource dataSource = BasicDataSourceFactory.createDataSource(dataSourceProperties);
            LOGGER.info("create BasicDatasource successful: {}", dataSource);
            return dataSource;
        }

        return null;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }
}
