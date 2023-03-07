package studio.deervision.repository;

import studio.deervision.model.Visitor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface VisitorRepository extends JpaRepository<Visitor, String> {

    @Query("SELECT v.visitDate as date, COUNT(v.id) as count from Visitor v WHERE v.visitDate >= ?1 GROUP BY v.visitDate")
    List<Object[]> findVisitorCount(LocalDate fromDate);

    @Query("SELECT v.countryCode as country, COUNT(v.id) as count from Visitor v WHERE v.visitDate >= ?1 AND v.countryCode IS NOT NULL GROUP BY v.countryCode")
    List<Object[]> findVisitorCountByCountry(LocalDate fromDate);

    void deleteByVisitDateBefore(LocalDate visitDate);
}
