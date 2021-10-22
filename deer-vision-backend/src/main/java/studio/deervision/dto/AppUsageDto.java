package studio.deervision.dto;

import java.util.List;

public class AppUsageDto {

    private final String appId;
    private final List<Long> usageCounts;

    public AppUsageDto(String appId, List<Long> usageCounts) {
        this.appId = appId;
        this.usageCounts = usageCounts;
    }

    public String getAppId() {
        return appId;
    }

    public List<Long> getUsageCounts() {
        return usageCounts;
    }
}
