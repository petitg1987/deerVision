package studio.deervision.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import studio.deervision.model.pe.LevelCompletionTime;

import java.util.List;

@Repository
public interface LevelCompletionTimeRepository extends JpaRepository<LevelCompletionTime, Long> {

    @Query("SELECT ROUND(lct.completionTime / 60.0) as minute, count(lct.completionTime) as quantity FROM LevelCompletionTime lct WHERE lct.levelId=?1 " +
            "GROUP BY ROUND(lct.completionTime / 60.0)")
    List<LevelCompletionTimeRange> findCompletionTimesGroupByMinute(int levelId);

    @Query("SELECT DISTINCT lct.levelId FROM LevelCompletionTime lct")
    List<Integer> findDistinctByLevelId();

    long countByRequestKeyAndLevelId(String requestKey, Integer levelId);
}
