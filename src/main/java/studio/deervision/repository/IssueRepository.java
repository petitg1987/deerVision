package studio.deervision.repository;

import studio.deervision.model.Issue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface IssueRepository extends JpaRepository<Issue, Long> {

    List<Issue> findByDateTimeBetween(LocalDateTime startDateTime, LocalDateTime endDateTime);

}
