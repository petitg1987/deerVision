package studio.deervision.service;

import studio.deervision.model.OperatingSystem;
import studio.deervision.model.Usage;
import studio.deervision.repository.UsageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class UsageService {

    private final UsageRepository usageRepository;

    @Autowired
    public UsageService(UsageRepository usageRepository) {
        this.usageRepository = usageRepository;
    }

    public void registerNewUsage(String systemKey, String appId, String appVersion, OperatingSystem operatingSystem) {
        usageRepository.saveAndFlush(new Usage(systemKey, appId, appVersion, operatingSystem));
    }

    public List<String> findDistinctAppId() {
        return usageRepository.findDistinctAppId();
    }

    public Map<LocalDate, Long> findUsagesBetweenDates(String appId, LocalDate startDate, LocalDate endDate) {
        List<Usage> usages = usageRepository.findBetweenDates(appId, toStartDateTime(startDate), toEndDateTime(endDate));
        return usages.stream().collect(Collectors.groupingBy(bva -> bva.getDateTime().toLocalDate(), Collectors.counting()));
    }

    private LocalDateTime toStartDateTime(LocalDate startDate){
        return startDate.atTime(LocalTime.MIN);
    }

    private LocalDateTime toEndDateTime(LocalDate endDate){
        return endDate.atTime(LocalTime.MAX);
    }
}
