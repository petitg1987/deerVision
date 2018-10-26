package com.urchin.release.mgt.service;

import com.urchin.release.mgt.config.properties.IssueProperties;
import com.urchin.release.mgt.model.Issue;
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
public class IssueService {

    private static final Logger LOGGER = LoggerFactory.getLogger(IssueService.class);
    private static final String DATE_PATTERN = "yyyyMMdd";
    private static final String DATE_TIME_PATTERN = DATE_PATTERN + "-HHmmss";
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(DATE_TIME_PATTERN);

    private IssueProperties issueProperties;

    @Autowired
    public IssueService(IssueProperties issueProperties){
        this.issueProperties = issueProperties;
    }

    public void writeIssue(String issueValue, String callerId){
        String uuid = UUID.randomUUID().toString();
        String issueFilename = determineFilename(uuid);
        LOGGER.info("Issue received from {} and saved in {}", callerId, issueFilename);

        Path path = Paths.get(issueFilename);
        try {
            Files.write(path, issueValue.getBytes());
        } catch (IOException e) {
            LOGGER.error("Impossible to write issue on disk", e);
        }
    }

    public Page<Issue> findPaginated(Pageable pageable){
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;

        List<Issue> issues = getIssues(startItem, pageSize);
        return new PageImpl<>(issues, PageRequest.of(currentPage, pageSize), totalIssues());
    }

    public Issue findByFilename(String filename){
        Path path = Paths.get(issueProperties.getBaseFolder() + filename);
        return new Issue(extractDate(path), path);
    }

    public void removeByFilename(String filename){
        Path path = Paths.get(issueProperties.getBaseFolder() + filename);
        try {
            Files.deleteIfExists(path);
        } catch (IOException e) {
            throw new IllegalStateException("Impossible to remove file: " + filename);
        }
    }

    public List<Issue> findByLocalDate(LocalDate localDate){
        return streamPathIssues()
                .filter(p ->  p.getFileName().toString().startsWith(localDate.format(DateTimeFormatter.ofPattern(DATE_PATTERN))))
                .map(p -> new Issue(extractDate(p), p))
                .collect(Collectors.toList());
    }

    private List<Issue> getIssues(int startItem, int pageSize){
        return streamPathIssues()
                .sorted((p1, p2) -> p2.getFileName().compareTo(p1.getFileName()))
                .skip(startItem)
                .limit(pageSize)
                .map(p -> new Issue(extractDate(p), p))
                .collect(Collectors.toList());
    }

    private long totalIssues(){
        return streamPathIssues().count();
    }

    private Stream<Path> streamPathIssues(){
        try {
            return Files.list(Paths.get(issueProperties.getBaseFolder()))
                    .filter(Files::isRegularFile);
        } catch (IOException e) {
            throw new IllegalArgumentException("Impossible to read files in folder: " + issueProperties.getBaseFolder(), e);
        }
    }

    private String determineFilename(String uuid) {
        String date = LocalDateTime.now().format(DATE_TIME_FORMATTER);
        return issueProperties.getBaseFolder() + date + "-" + uuid + "-error-issue.txt";
    }

    private LocalDateTime extractDate(Path path){
        String dateString = path.getFileName().toString().substring(0, DATE_TIME_PATTERN.length());
        return LocalDateTime.parse(dateString, DATE_TIME_FORMATTER);
    }

}
