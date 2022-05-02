package studio.deervision.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import studio.deervision.model.Usage;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface UsageRepository extends JpaRepository<Usage, Long> {

    @Query("SELECT DISTINCT u.appId FROM Usage u")
    List<String> findDistinctAppId();

    @Query(nativeQuery = true, value = "SELECT TO_DATE(u.DATE_TIME, 'YYYY-MM-DD') as UsageDate, COUNT(DISTINCT u.REQUEST_KEY) as UsageCount from Usage u WHERE u.APP_ID=?1 AND u.DATE_TIME BETWEEN ?2 AND ?3 AND (true=?4 OR u.APP_VERSION not like '%snapshot') GROUP BY UsageDate")
    List<UsageCount> findUsageCountBetweenDatesUnique(String appId, LocalDateTime startDateTime, LocalDateTime endDateTime, boolean includeSnapshot);
    @Query(nativeQuery = true, value = "SELECT TO_DATE(u.DATE_TIME, 'YYYY-MM-DD') as UsageDate, COUNT(u.DATE_TIME) as UsageCount from Usage u WHERE u.APP_ID=?1 AND u.DATE_TIME BETWEEN ?2 AND ?3 AND (true=?4 OR u.APP_VERSION not like '%snapshot') GROUP BY UsageDate")
    List<UsageCount> findUsageCountBetweenDates(String appId, LocalDateTime startDateTime, LocalDateTime endDateTime, boolean includeSnapshot);

    @Query("SELECT COUNT(DISTINCT u.requestKey) FROM Usage u WHERE u.appId =?1")
    Long countDistinctRequestKeys(String appId);
}
