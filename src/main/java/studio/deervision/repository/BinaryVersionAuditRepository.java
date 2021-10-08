package studio.deervision.repository;

import studio.deervision.model.OperatingSystem;
import studio.deervision.model.audit.BinaryVersionAudit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BinaryVersionAuditRepository extends JpaRepository<BinaryVersionAudit, Long> {

    List<BinaryVersionAudit> findByOperatingSystemAndDateTimeBetween(OperatingSystem operatingSystem, LocalDateTime startDateTime, LocalDateTime endDateTime);

}
