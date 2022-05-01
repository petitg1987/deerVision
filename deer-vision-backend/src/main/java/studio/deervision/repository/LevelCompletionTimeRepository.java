package studio.deervision.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import studio.deervision.model.pe.LevelCompletionTime;

@Repository
public interface LevelCompletionTimeRepository extends JpaRepository<LevelCompletionTime, Long> {

}
