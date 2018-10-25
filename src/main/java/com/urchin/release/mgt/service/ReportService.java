package com.urchin.engine.monitor.service;

import com.urchin.engine.monitor.model.Report;
import com.urchin.engine.monitor.config.ReportProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ReportService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReportService.class);
    private static final String DATE_PATTERN = "yyyyMMdd";
    private static final String DATE_TIME_PATTERN = DATE_PATTERN + "-HHmmss";
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(DATE_TIME_PATTERN);

    private ReportProperties reportProperties;

    @Autowired
    public ReportService(ReportProperties reportProperties){
        this.reportProperties = reportProperties;
    }

    public void writeReport(String reportValue, String callerId){
        String uuid = UUID.randomUUID().toString();
        String errorReportFilename = determineFilename(uuid);
        LOGGER.info("Error report received from {}. Report saved in {}", callerId, errorReportFilename);

        Path path = Paths.get(errorReportFilename);
        try {
            Files.write(path, reportValue.getBytes());
        } catch (IOException e) {
            LOGGER.error("Impossible to write error report on disk", e);
        }
    }

    public Page<Report> findPaginated(Pageable pageable){
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;

        List<Report> reports = getReports(startItem, pageSize);
        return new PageImpl<>(reports, PageRequest.of(currentPage, pageSize), totalReports());
    }

    public Report findByFilename(String filename){
        Path path = Paths.get(reportProperties.getBaseFolder() + filename);
        return new Report(extractDate(path), path);
    }

    public void removeByFilename(String filename){
        Path path = Paths.get(reportProperties.getBaseFolder() + filename);
        try {
            Files.deleteIfExists(path);
        } catch (IOException e) {
            throw new IllegalStateException("Impossible to remove file: " + filename);
        }
    }

    public List<Report> findByLocalDate(LocalDate localDate){
        return streamPathReports()
                .filter(p ->  p.getFileName().toString().startsWith(localDate.format(DateTimeFormatter.ofPattern(DATE_PATTERN))))
                .map(p -> new Report(extractDate(p), p))
                .collect(Collectors.toList());
    }

    private List<Report> getReports(int startItem, int pageSize){
        return streamPathReports()
                .sorted((p1, p2) -> p2.getFileName().compareTo(p1.getFileName()))
                .skip(startItem)
                .limit(pageSize)
                .map(p -> new Report(extractDate(p), p))
                .collect(Collectors.toList());
    }

    private long totalReports(){
        return streamPathReports().count();
    }

    private Stream<Path> streamPathReports(){
        try {
            return Files.list(Paths.get(reportProperties.getBaseFolder()))
                    .filter(Files::isRegularFile);
        } catch (IOException e) {
            throw new IllegalArgumentException("Impossible to read files in folder: " + reportProperties.getBaseFolder(), e);
        }
    }

    private String determineFilename(String uuid) {
        String date = LocalDateTime.now().format(DATE_TIME_FORMATTER);
        return reportProperties.getBaseFolder() + date + "-" + uuid + "-error-report.txt";
    }

    private LocalDateTime extractDate(Path path){
        String dateString = path.getFileName().toString().substring(0, DATE_TIME_PATTERN.length());
        return LocalDateTime.parse(dateString, DATE_TIME_FORMATTER);
    }

}
