package com.urchin.release.mgt.repository;

import com.urchin.release.mgt.model.BinaryVersionAudit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BinaryVersionAuditRepository extends JpaRepository<BinaryVersionAudit, Long> {

    List<BinaryVersionAudit> findByDateBetween(LocalDate startDate, LocalDate endDate);

}
