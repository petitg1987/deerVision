package com.urchin.release.mgt.repository;

public class DownloadByVersionCount {

    private String appVersion;

    private Long count;

    public DownloadByVersionCount(String appVersion, Long count) {
        this.appVersion = appVersion;
        this.count = count;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public Long getCount() {
        return count;
    }
}
