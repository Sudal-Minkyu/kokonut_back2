package com.app.common;

import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

/**
 * @author Woody
 * Date : 2022-12-29
 * Time :
 * Remark : 코코넛 메인(kokonut) Database 연결 Configuration
 */
@Configuration
@PropertySource({"classpath:application.yml"})
@EnableTransactionManagement
public class KokonutMainDatasourceConfiguration {

    @Bean
    @Primary
    @ConfigurationProperties("spring.datasource.kokonut")
    public DataSourceProperties todosDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    @Primary
    public DataSource todosDataSource() {
        return todosDataSourceProperties()
                .initializeDataSourceBuilder()
                .build();
    }

}
