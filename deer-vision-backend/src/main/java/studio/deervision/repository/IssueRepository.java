package studio.deervision.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import studio.deervision.model.Issue;

import java.util.List;

@Repository
public interface IssueRepository extends JpaRepository<Issue, Long> {

    @Query("SELECT i.id as id, i.origin as origin, i.requestKey as requestKey, i.appId as appId, i.appVersion as appVersion, i.operatingSystem as operatingSystem, " +
            "i.dateTime as dateTime from Issue i ORDER BY i.dateTime DESC")
    List<LightIssue> findAllOrderByDates();

    void deleteByRequestKey(String requestKey);

}
