package org.chang.springboot;


import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories
public class DatabaseConfiguration {
  @Value("${spring.datasource.url}")
  String url;

  @Value("${spring.datasource.username}")
  String userName;

  @Value("${spring.datasource.password}")
  String password;

  @Bean
  public HikariDataSource getDataSource() {

    HikariConfig config = new HikariConfig();
    config.setJdbcUrl(url);
    config.setUsername(userName);
    config.setPassword(password);
    //    config.addDataSourceProperty("cachePrepStmts", "true");
    //    config.addDataSourceProperty("prepStmtCacheSize", "250");
    //    config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

    HikariDataSource ds = new HikariDataSource(config);
    return ds;
  }
}
