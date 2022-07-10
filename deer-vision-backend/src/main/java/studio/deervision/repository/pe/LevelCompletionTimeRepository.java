package studio.deervision.repository.pe;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import studio.deervision.model.pe.LevelCompletionTime;

import java.util.List;

@Repository
public interface LevelCompletionTimeRepository extends JpaRepository<LevelCompletionTime, Long> {

    @Query("SELECT ROUND(lct.completionTime / 60.0) as minute, count(lct.completionTime) as quantity, lct.actionName as actionName FROM LevelCompletionTime lct WHERE lct.levelId=?1 " +
            "AND (true=?2 OR lct.appVersion not like '%snapshot') " +
            "GROUP BY ROUND(lct.completionTime / 60.0), lct.actionName")
    List<LevelCompletionTimeRange> findCompletionTimesGroupByMinute(int levelId, boolean includeSnapshot);

    @Query("SELECT DISTINCT lct.levelId FROM LevelCompletionTime lct")
    List<Integer> findDistinctByLevelId();

    long countByRequestKeyAndLevelIdAndActionName(String requestKey, Integer levelId, String actionName);
}
