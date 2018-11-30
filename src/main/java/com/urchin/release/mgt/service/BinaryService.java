package com.urchin.release.mgt.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.urchin.release.mgt.config.properties.BinaryProperties;
import com.urchin.release.mgt.model.Binary;
import com.urchin.release.mgt.model.BinaryType;
import com.urchin.release.mgt.model.audit.BinaryDownloadAudit;
import com.urchin.release.mgt.model.audit.BinaryVersionAudit;
import com.urchin.release.mgt.repository.BinaryDownloadAuditRepository;
import com.urchin.release.mgt.repository.BinaryVersionAuditRepository;
import com.urchin.release.mgt.repository.DownloadByVersionCount;
import com.urchin.release.mgt.utils.AppVersionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class BinaryService {

    private BinaryProperties binaryProperties;
    private BinaryDownloadAuditRepository binaryDownloadAuditRepository;
    private BinaryVersionAuditRepository binaryVersionAuditRepository;

    @Autowired
    public BinaryService(BinaryProperties binaryProperties, BinaryDownloadAuditRepository binaryDownloadAuditRepository,
                         BinaryVersionAuditRepository binaryVersionAuditRepository) {
        this.binaryProperties = binaryProperties;
        this.binaryDownloadAuditRepository = binaryDownloadAuditRepository;
        this.binaryVersionAuditRepository = binaryVersionAuditRepository;
    }

    public List<Binary> getLastBinaries(){
        List<Binary> lastBinaries = new ArrayList<>();
        for(BinaryType binaryType : BinaryType.values()) {
            getLastBinary(binaryType).ifPresent(lastBinaries::add);
        }

        return lastBinaries;
    }

    public Optional<Binary> getLastBinary(BinaryType binaryType){
        return streamBinaries()
                .filter(b -> b.getFileName().endsWith(binaryType.getExtension()))
                .max((e1, e2) -> AppVersionUtils.compareVersion(e1.getVersion(), e2.getVersion()));
    }

    public void uploadOrReplace(String filename, byte[] bytes) {
        deleteIfExist(filename);
        upload(filename, bytes);
    }

    public void newAuditDownload(String appVersion, BinaryType binaryType){
        binaryDownloadAuditRepository.saveAndFlush(new BinaryDownloadAudit(appVersion, binaryType));
    }

    public void newAuditVersion(String appVersion, BinaryType binaryType){
        binaryVersionAuditRepository.saveAndFlush(new BinaryVersionAudit(appVersion, binaryType));
    }

    public Map<LocalDate, Long> findBinaryVersionAuditsGroupByDate(LocalDate startDate, LocalDate endDate){
        LocalDateTime startDateTime = startDate.atTime(LocalTime.MIN);
        LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);

        List<BinaryVersionAudit> binaryVersionAudits = binaryVersionAuditRepository.findByDateTimeBetween(startDateTime, endDateTime);
        return binaryVersionAudits.stream().collect(Collectors.groupingBy(bva -> bva.getDateTime().toLocalDate(), Collectors.counting()));
    }

    public Map<LocalDate, Long> findBinaryDownloadAuditsGroupByDate(BinaryType binaryType, LocalDate startDate, LocalDate endDate){
        LocalDateTime startDateTime = startDate.atTime(LocalTime.MIN);
        LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);

        List<BinaryDownloadAudit> binaryDownloadAudits = binaryDownloadAuditRepository.findByBinaryTypeAndDateTimeBetween(binaryType, startDateTime, endDateTime);
        return binaryDownloadAudits.stream().collect(Collectors.groupingBy(bda -> bda.getDateTime().toLocalDate(), Collectors.counting()));
    }

    public List<DownloadByVersionCount> findDownloadsByVersionCount(){
        return binaryDownloadAuditRepository.findDownloadsByVersionCount();
    }

    private Stream<Binary> streamBinaries(){
        final AmazonS3 s3 = AmazonS3ClientBuilder.defaultClient();
        ListObjectsV2Result result = s3.listObjectsV2(binaryProperties.getAwsBucketName());
        return result.getObjectSummaries()
                .stream()
                .map(o -> new Binary(binaryProperties.getBaseUrl() + o.getKey(), o.getSize(), retrieveVersion(o.getKey())));
    }

    private void deleteIfExist(String filename){
        final AmazonS3 s3 = AmazonS3ClientBuilder.defaultClient();
        s3.deleteObject(binaryProperties.getAwsBucketName(), filename);
    }

    private void upload(String filename, byte[] bytes){
        final AmazonS3 s3 = AmazonS3ClientBuilder.defaultClient();
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(bytes.length);
        s3.putObject(binaryProperties.getAwsBucketName(), filename, new ByteArrayInputStream(bytes), objectMetadata);

        //TODO: make it public !!!
    }

    private String retrieveVersion(String filename){
        Matcher matcher = Pattern.compile(binaryProperties.getVersionPattern()).matcher(filename);
        if(!matcher.find()){
            throw new IllegalArgumentException("Impossible to find binary version on '" + filename + "' with: " + binaryProperties.getVersionPattern());
        }

        return matcher.group(0);
    }
}
