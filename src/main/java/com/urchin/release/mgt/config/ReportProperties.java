package com.urchin.release.mgt.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "report")
public class ReportProperties {

    private String baseFolder;
    private int pageSize;
    private int chartDays;

    public String getBaseFolder() {
        return baseFolder;
    }

    public void setBaseFolder(String baseFolder) {
        this.baseFolder = baseFolder;
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
