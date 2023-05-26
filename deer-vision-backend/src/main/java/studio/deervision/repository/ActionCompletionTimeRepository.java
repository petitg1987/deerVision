package studio.deervision.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import studio.deervision.model.completion.ActionCompletionCountForMinute;
import studio.deervision.model.completion.ActionCompletionTime;
import studio.deervision.model.completion.LastActionCompletion;

import java.util.List;

@Repository
public interface ActionCompletionTimeRepository extends JpaRepository<ActionCompletionTime, Long> {

    @Query("SELECT DISTINCT act.levelId FROM ActionCompletionTime act WHERE act.appId=?1 ORDER BY act.levelId")
    List<Integer> findDistinctByLevelId(String appId);

    @Query("SELECT DISTINCT act.actionName FROM ActionCompletionTime act WHERE act.appId=?1 AND act.levelId=?2")
    List<String> findDistinctByActionName(String appId, int levelId);

    @Query("SELECT ROUND(act.completionTime / 60.0, 0) as minute, count(act.completionTime) as playerCount FROM ActionCompletionTime act " +
            "WHERE act.appId=?1 AND act.levelId=?2 AND act.actionName=?3 " +
            "AND (true=?4 OR act.appVersion not like '%snapshot') " +
            "GROUP BY ROUND(act.completionTime / 60.0, 0), act.actionName")
    List<ActionCompletionCountForMinute> groupCompletionTimeByMinute(String appId, int levelId, String actionName, boolean includeSnapshot);

    @Query (value = "SELECT DISTINCT ON (act.request_key) act.request_key as requestKey, act.level_id as levelId, act.action_name as actionName, TO_CHAR(act.creation_date_time, 'dd/mm/yyyy HH24:MI') as creationDateTime FROM action_completion_time act " +
            "WHERE act.app_id=?1 " +
            "AND (true=?2 OR act.app_version not like '%snapshot') " +
            "ORDER BY act.request_key, act.creation_date_time DESC " +
            "LIMIT ?3", nativeQuery = true)
    List<LastActionCompletion> getLastCompletionsOfDistinctUsers(String appId, boolean includeSnapshot, int resultLimit);

    long countByRequestKeyAndAppIdAndLevelIdAndActionName(String requestKey, String appId, Integer levelId, String actionName);
}
