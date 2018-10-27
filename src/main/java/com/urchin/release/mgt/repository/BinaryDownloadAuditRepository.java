package com.urchin.release.mgt.repository;

import com.urchin.release.mgt.model.BinaryDownloadAudit;
import com.urchin.release.mgt.model.BinaryType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BinaryDownloadAuditRepository extends JpaRepository<BinaryDownloadAudit, Long> {

    List<BinaryDownloadAudit> findByBinaryTypeAndDateTimeBetween(BinaryType binaryType, LocalDateTime startDateTime, LocalDateTime endDateTime);

}
