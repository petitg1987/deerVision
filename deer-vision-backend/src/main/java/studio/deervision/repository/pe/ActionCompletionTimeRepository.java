package studio.deervision.repository.pe;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import studio.deervision.model.pe.ActionCompletionTime;

import java.util.List;

@Repository
public interface ActionCompletionTimeRepository extends JpaRepository<ActionCompletionTime, Long> {

    @Query("SELECT act.actionName as actionName, ROUND(act.completionTime / 60.0) as minute, count(act.completionTime) as playerCount FROM ActionCompletionTime act WHERE act.levelId=?1 " +
            "AND (true=?2 OR act.appVersion not like '%snapshot') " +
            "GROUP BY ROUND(act.completionTime / 60.0), act.actionName")
    List<ActionCompletionCountForMinute> groupCompletionTimeByMinute(int levelId, boolean includeSnapshot);

    @Query("SELECT DISTINCT act.levelId FROM ActionCompletionTime act")
    List<Integer> findDistinctByLevelId();

    long countByRequestKeyAndLevelIdAndActionName(String requestKey, Integer levelId, String actionName);
}
