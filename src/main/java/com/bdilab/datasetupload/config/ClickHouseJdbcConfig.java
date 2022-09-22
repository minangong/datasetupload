package com.bdilab.datasetupload.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * ClickHouse JdbcTemplate.
 *
 * @author wh
 * @date 2022/04/28
 */
@Configuration
public class ClickHouseJdbcConfig {
  @Value("${clickhouse.jdbc.driver-class-name}")
  private String driverClassName;

  @Value("${clickhouse.jdbc.url}")
  private String url;

  @Bean(name = "clickHouseJdbcTemplate")
  public JdbcTemplate getBean(){
    DruidDataSource dataSource = new DruidDataSource();
    dataSource.setDriverClassName(driverClassName);
    dataSource.setUrl(url);
    dataSource.setMaxActive(20);
    dataSource.setMaxWait(60000);
    dataSource.setInitialSize(5);
    return new JdbcTemplate(dataSource);
  }
}
