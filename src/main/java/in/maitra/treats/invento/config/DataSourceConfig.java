package in.maitra.treats.invento.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import in.maitra.treats.invento.config.properties.DataSourceProperties;
import in.maitra.treats.invento.util.filter.PersistentEntityFilterMetadataLoader;
import in.maitra.treats.invento.util.persistence.AuditableEntityLoader;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Configuration
@EnableConfigurationProperties(DataSourceProperties.class)
public class DataSourceConfig {

    @Bean
    public DataSource dataSource(DataSourceProperties dataSourceProperties) {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(dataSourceProperties.getUrl());
        config.setUsername(dataSourceProperties.getUsername());
        config.setPassword(dataSourceProperties.getPswrd());
        config.setDriverClassName(dataSourceProperties.getDriverClassName());
        config.setMaximumPoolSize(dataSourceProperties.getMaximumPoolSize());
        config.setMinimumIdle(dataSourceProperties.getMinimumIdle());
        config.setConnectionTimeout(dataSourceProperties.getConnectionTimeout());
        config.setIdleTimeout(dataSourceProperties.getIdleTimeout());
        config.setMaxLifetime(dataSourceProperties.getMaxLifetime());
        return new HikariDataSource(config);
    }

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean
    public PersistentEntityFilterMetadataLoader persistentEntityFilterMetadataLoader(DataSourceProperties dataSourceProperties) {
        return new PersistentEntityFilterMetadataLoader(dataSourceProperties.getPersistableEntityPackageNames());
    }

    @Bean
    public AuditableEntityLoader auditableEntityLoader(DataSourceProperties dataSourceProperties) {
        return new AuditableEntityLoader(dataSourceProperties.getPersistableEntityPackageNames());
    }
}
