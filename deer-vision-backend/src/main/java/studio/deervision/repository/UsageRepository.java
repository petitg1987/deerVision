package studio.deervision.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import studio.deervision.model.OperatingSystem;
import studio.deervision.model.Usage;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface UsageRepository extends JpaRepository<Usage, Long> {

    @Query("SELECT DISTINCT u.appId FROM Usage u")
    List<String> findDistinctAppId();

    List<Usage> findByAppIdAndOperatingSystemAndDateTimeBetween(String appId, OperatingSystem operatingSystem,
                                                                LocalDateTime startDateTime, LocalDateTime endDateTime);

}
