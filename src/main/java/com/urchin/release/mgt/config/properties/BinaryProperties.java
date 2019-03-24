package com.urchin.release.mgt.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "binary")
public class BinaryProperties {

    private String awsBucketName;

    private String awsAccessKeyId;

    private String awsSecretAccessKey;

    private String versionPattern;

    private int chartDays;

    private String uploadPassword;

    public String getAwsBucketName() {
        return awsBucketName;
    }

    public void setAwsBucketName(String awsBucketName) {
        this.awsBucketName = awsBucketName;
    }

    public String getAwsAccessKeyId() {
        return awsAccessKeyId;
    }

    public void setAwsAccessKeyId(String awsAccessKeyId) {
        this.awsAccessKeyId = awsAccessKeyId;
    }

    public String getAwsSecretAccessKey() {
        return awsSecretAccessKey;
    }

    public void setAwsSecretAccessKey(String awsSecretAccessKey) {
        this.awsSecretAccessKey = awsSecretAccessKey;
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

    public String getUploadPassword() {
        return uploadPassword;
    }

    public void setUploadPassword(String uploadPassword) {
        this.uploadPassword = uploadPassword;
    }
    }
