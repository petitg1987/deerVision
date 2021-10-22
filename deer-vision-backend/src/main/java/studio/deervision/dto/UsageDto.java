package studio.deervision.dto;

import java.util.ArrayList;
import java.util.List;

public class UsageDto {

    List<String> dates;
    List<AppUsageDto> appUsages = new ArrayList<>();

    public List<String> getDates() {
        return dates;
    }

    public void setDates(List<String> dates) {
        this.dates = dates;
    }

    public List<AppUsageDto> getAppUsages() {
        return appUsages;
    }

    public void addAppUsage(AppUsageDto appUsage) {
        this.appUsages.add(appUsage);
    }
}
