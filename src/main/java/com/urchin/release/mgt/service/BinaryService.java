package com.urchin.release.mgt.service;

import com.urchin.release.mgt.model.OperatingSystem;
import com.urchin.release.mgt.model.audit.BinaryVersionAudit;
import com.urchin.release.mgt.repository.BinaryVersionAuditRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class BinaryService {

    private final BinaryVersionAuditRepository binaryVersionAuditRepository;

    @Autowired
    public BinaryService(BinaryVersionAuditRepository binaryVersionAuditRepository) {
        this.binaryVersionAuditRepository = binaryVersionAuditRepository;
    }

    public void newAuditVersion(String binaryVersion, String userKey, OperatingSystem operatingSystem){
        binaryVersionAuditRepository.saveAndFlush(new BinaryVersionAudit(binaryVersion, userKey, operatingSystem));
    }

    public Map<LocalDate, Long> findBinaryVersionAuditsGroupByDate(OperatingSystem operatingSystem, LocalDate startDate, LocalDate endDate){
        List<BinaryVersionAudit> binaryVersionAudits = binaryVersionAuditRepository.findByOperatingSystemAndDateTimeBetween(operatingSystem, toStartDateTime(startDate), toEndDateTime(endDate));
        return binaryVersionAudits.stream().collect(Collectors.groupingBy(bva -> bva.getDateTime().toLocalDate(), Collectors.counting()));
    }

    private LocalDateTime toStartDateTime(LocalDate startDate){
        return startDate.atTime(LocalTime.MIN);
    }

    private LocalDateTime toEndDateTime(LocalDate endDate){
        return endDate.atTime(LocalTime.MAX);
    }
}
