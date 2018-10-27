package com.urchin.release.mgt.repository;

import com.urchin.release.mgt.model.BinaryType;
import com.urchin.release.mgt.model.audit.BinaryDownloadAudit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BinaryDownloadAuditRepository extends JpaRepository<BinaryDownloadAudit, Long> {

    List<BinaryDownloadAudit> findByBinaryTypeAndDateTimeBetween(BinaryType binaryType, LocalDateTime startDateTime, LocalDateTime endDateTime);

    @Query("SELECT new com.urchin.release.mgt.repository.DownloadByVersionCount(bda.appVersion, COUNT(bda)) " +
            "FROM BinaryDownloadAudit bda " +
            "GROUP BY bda.appVersion")
    List<DownloadByVersionCount> findDownloadsByVersionCount();
}
