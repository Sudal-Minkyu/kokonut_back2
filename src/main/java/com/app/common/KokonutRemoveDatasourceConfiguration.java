package com.app.common;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

/**
 * @author Woody
 * Date : 2022-12-29
 * Time :
 * Remark : 코코넛 탈퇴(remove) Database 연결 Configuration
 */
@Configuration
@PropertySource({"classpath:application.yml"})
@EnableTransactionManagement
public class KokonutRemoveDatasourceConfiguration {

    @Bean
    @ConfigurationProperties("spring.datasource.kokonut-remove")
    public DataSourceProperties kokonutRemoveDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    public DataSource kokonutRemoveDataSource() {
        return kokonutRemoveDataSourceProperties()
                .initializeDataSourceBuilder()
                .build();
    }

    @Bean
    public JdbcTemplate kokonutRemoveJdbcTemplate(@Qualifier("kokonutRemoveDataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

}