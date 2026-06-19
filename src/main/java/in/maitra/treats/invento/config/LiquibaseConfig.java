package in.maitra.treats.invento.config;

import in.maitra.treats.invento.config.properties.LiquibaseProperties;
import liquibase.integration.spring.SpringLiquibase;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
@EnableConfigurationProperties(LiquibaseProperties.class)
public class LiquibaseConfig {
    @Bean
    public SpringLiquibase liquibase(LiquibaseProperties liquibaseProperties, DataSource dataSource) {
        SpringLiquibase liquibase = new SpringLiquibase();
        liquibase.setDataSource(dataSource);
        liquibase.setChangeLog(liquibaseProperties.getChangeLog());
        liquibase.setContexts(liquibaseProperties.getContexts());
        liquibase.setDefaultSchema(liquibaseProperties.getDefaultSchema());
        liquibase.setShouldRun(liquibaseProperties.isEnabled());
        liquibase.setDropFirst(liquibaseProperties.isDropFirst());
        return liquibase;
    }
}
