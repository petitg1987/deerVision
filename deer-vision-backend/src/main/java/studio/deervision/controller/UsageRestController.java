package studio.deervision.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import studio.deervision.config.properties.UsageProperties;
import studio.deervision.dto.AppUsageDto;
import studio.deervision.dto.UsageDto;
import studio.deervision.model.OperatingSystem;
import studio.deervision.service.UsageService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class UsageRestController {

    private static final String CHARTS_DATE_FORMAT = "dd/MM";

    private final UsageService usageService;
    private final UsageProperties usageProperties;

    @Autowired
    public UsageRestController(UsageService usageService,UsageProperties usageProperties) {
        this.usageService = usageService;
        this.usageProperties = usageProperties;
    }

    //curl -X POST -H "X-SystemKey: 0-17" "http://localhost:5000/api/usage?appId=photonEngineer&appVersion=1.0.0&os=linux"
    //curl -X POST -H "X-SystemKey: 0-17" "http://localhost:5000/api/usage?appId=photonEngineer&appVersion=1.0.0&os=windows"
    @PostMapping(value="/usage")
    public void postUsage(@RequestParam("appId") String appId, @RequestParam("appVersion") String appVersion, @RequestParam("os") String os) {
        String systemKey = (String)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        usageService.registerNewUsage(systemKey, appId, appVersion, OperatingSystem.toOperatingSystem(os));
    }

    //curl -H "Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJqdGkiOiJkdnNKV1QiLCJzdWIiOiJhZG1pbiIsImF1dGhvcml0aWVzIjpbIlJPTEVfVVNFUiJdLCJpYXQiOjE2MzQ1NzIzODgsImV4cCI6MTk0OTkzMjM4OH0.S-VnMofcbTMv4epZCT3Es1zezcvXsN4xL0gmkXca3vGHsXvwa5MB1puaw6Y8wBUZLLifvXLLGZUcYvYoDvLOWQ" http://localhost:5000/api/admin/usage | jq .
    @GetMapping(value = "/admin/usage")
    public UsageDto getUsage() {
        UsageDto usageDto = new UsageDto();

        List<LocalDate> chartDates = retrieveChartsDates(usageProperties.getChartDays());
        LocalDate startDate = chartDates.get(0);
        LocalDate endDate = chartDates.get(chartDates.size() - 1);

        usageDto.setDates(chartDates.stream()
                .map(ld -> ld.format(DateTimeFormatter.ofPattern(CHARTS_DATE_FORMAT)))
                .collect(Collectors.toList()));

        List<String> appIds = usageService.findDistinctAppId();
        for(String appId : appIds) {
            Map<LocalDate, Long> mapUsageCounts = addMissingDates(usageService.findUsagesBetweenDates(appId, startDate, endDate), chartDates);
            List<Long> usageCounts = mapUsageCounts.keySet().stream()
                    .sorted()
                    .map(mapUsageCounts::get)
                    .collect(Collectors.toList());
            usageDto.addAppUsage(new AppUsageDto(appId, usageCounts));
        }

        return usageDto;
    }

    private List<LocalDate> retrieveChartsDates(int nbChartDays) {
        List<LocalDate> dates = new ArrayList<>();
        LocalDate currentDate = LocalDate.now().minusDays(nbChartDays);

        for(int i=0; i<nbChartDays; ++i) {
            currentDate = currentDate.plusDays(1);
            dates.add(currentDate);
        }

        return dates;
    }

    private Map<LocalDate, Long> addMissingDates(Map<LocalDate, Long> map, List<LocalDate> dates){
        dates.forEach(d -> map.putIfAbsent(d,  0L));
        return map;
    }
}
