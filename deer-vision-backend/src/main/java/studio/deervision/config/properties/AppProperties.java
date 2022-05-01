package studio.deervision.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "app")
public class AppProperties {

    private String versionPattern;

    public String getVersionPattern() {
        return versionPattern;
    }

    public void setVersionPattern(String versionPattern) {
        this.versionPattern = versionPattern;
    }
}
