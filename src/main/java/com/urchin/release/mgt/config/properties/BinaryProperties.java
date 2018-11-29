package com.urchin.release.mgt.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "binary")
public class BinaryProperties {

    private String baseUrl;

    private String versionPattern;

    private int chartDays;

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getVersionPattern() {
        return versionPattern;
    }

    public void setVersionPattern(String versionPattern) {
        this.versionPattern = versionPattern;
    }

    public int getChartDays() {
        return chartDays;
    }

    public void setChartDays(int chartDays) {
        this.chartDays = chartDays;
    }
}
