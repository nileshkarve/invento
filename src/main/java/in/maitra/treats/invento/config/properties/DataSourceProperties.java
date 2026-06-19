package in.maitra.treats.invento.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@Getter
@Setter
@ConfigurationProperties(prefix = "application.datasource")
public class DataSourceProperties {
    private String url;
    private String username;
    private String pswrd;
    private String driverClassName;
    public int maximumPoolSize = 20;
    public int minimumIdle = 5;
    public int connectionTimeout = 30000;
    public int idleTimeout = 600000;
    public int maxLifetime = 1800000;
    private List<String> persistableEntityPackageNames;
    private Integer maxBatchSize;
}
