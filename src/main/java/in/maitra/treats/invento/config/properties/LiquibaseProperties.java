package in.maitra.treats.invento.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "application.liquibase")
public class LiquibaseProperties {
    private String changeLog;
    private String contexts;
    private boolean enabled;
    private boolean dropFirst = false;
    private String defaultSchema;
}
