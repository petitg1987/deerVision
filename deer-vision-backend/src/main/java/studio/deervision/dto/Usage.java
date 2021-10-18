package studio.deervision.dto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Usage {

    List<String> dates;
    Map<String, List<Long>> osUsages = new HashMap<>();

    public List<String> getDates() {
        return dates;
    }

    public void setDates(List<String> dates) {
        this.dates = dates;
    }

    public Map<String, List<Long>> getOsUsages() {
        return osUsages;
    }

    public void addOsUsages(String osKey, List<Long> usage) {
        osUsages.put(osKey, usage);
    }
}
