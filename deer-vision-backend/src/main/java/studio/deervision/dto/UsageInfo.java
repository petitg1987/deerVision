package studio.deervision.dto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UsageInfo {

    List<String> dates;
    Map<String, List<Long>> appIdUsages = new HashMap<>();

    public List<String> getDates() {
        return dates;
    }

    public void setDates(List<String> dates) {
        this.dates = dates;
    }

    public Map<String, List<Long>> getAppUsages() {
        return appIdUsages;
    }

    public void addAppUsage(String appId, List<Long> appOsUsages) {
        appIdUsages.put(appId, appOsUsages);
    }
}
