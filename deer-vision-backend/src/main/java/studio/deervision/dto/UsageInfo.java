package studio.deervision.dto;

import studio.deervision.model.OperatingSystem;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

class AppOsUsage {
    Map<OperatingSystem, List<Long>> osUsages = new HashMap<>();

    public Map<OperatingSystem, List<Long>> getOsUsages() {
        return osUsages;
    }

    public void addOsUsages(OperatingSystem operatingSystem, List<Long> appOsUsages) {
        osUsages.put(operatingSystem, appOsUsages);
    }
}

public class UsageInfo {

    List<String> dates;
    Map<String, AppOsUsage> appIdUsages = new HashMap<>();

    public List<String> getDates() {
        return dates;
    }

    public void setDates(List<String> dates) {
        this.dates = dates;
    }

    public Map<String, AppOsUsage> getAppUsages() {
        return appIdUsages;
    }

    public void addUsage(String appId, OperatingSystem operatingSystem, List<Long> appOsUsages) {
        AppOsUsage appUsage = appIdUsages.computeIfAbsent(appId, ai -> new AppOsUsage());
        appUsage.addOsUsages(operatingSystem, appOsUsages);
    }
}
