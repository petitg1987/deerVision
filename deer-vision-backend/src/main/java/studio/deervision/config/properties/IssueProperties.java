package studio.deervision.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "issue")
public class IssueProperties {

    private String versionPattern;
    private int pageSize;
    private int chartDays;

    public String getVersionPattern() {
        return versionPattern;
    }

    public void setVersionPattern(String versionPattern) {
        this.versionPattern = versionPattern;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getChartDays() {
        return chartDays;
    }

    public void setChartDays(int chartDays) {
        this.chartDays = chartDays;
    }
}
