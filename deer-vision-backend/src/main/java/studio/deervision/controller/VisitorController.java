package studio.deervision.controller;

import studio.deervision.model.Visitor;
import studio.deervision.service.VisitorService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class VisitorController {

    private static final String CHARTS_DATE_FORMAT = "dd/MM";

    private final VisitorService visitorService;

    @Autowired
    VisitorController(VisitorService visitorService) {
        this.visitorService = visitorService;
    }

    //curl -X POST http://localhost:5000/api/visitor/add
    @PostMapping(value = "/visitor/add")
    public ResponseEntity<String> addVisitor(HttpServletRequest request) {
        visitorService.addVisitor(request);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    //curl -X GET http://localhost:5000/api/visitor/country
    @GetMapping(value = "/visitor/country")
    public ResponseEntity<String> getVisitorCountry(HttpServletRequest request) {
        String visitorCountry = visitorService.getVisitorCountry(request);
        return ResponseEntity.ok(visitorCountry);
    }

    //curl -H "Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJqdGkiOiJkdnNKV1QiLCJzdWIiOiJhZG1pbiIsImF1dGhvcml0aWVzIjpbIlJPTEVfVVNFUiJdLCJpYXQiOjE2NTc0NzE4NDMsImV4cCI6MTk3MjgzMTg0M30.S16GDOuf4RU3_puN6xAuVRDNcEiAJtngFmkTfo37kqalaN3c3m9OdxGWuXv49u9jvOyGraNaXDCvuH9bnrtfiA" "http://localhost:5000/api/admin/visitor-count?retrieveDays=30" | jq .
    //curl -H "Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJqdGkiOiJkdnNKV1QiLCJzdWIiOiJhZG1pbiIsImF1dGhvcml0aWVzIjpbIlJPTEVfVVNFUiJdLCJpYXQiOjE2NTc0NzE4NDMsImV4cCI6MTk3MjgzMTg0M30.S16GDOuf4RU3_puN6xAuVRDNcEiAJtngFmkTfo37kqalaN3c3m9OdxGWuXv49u9jvOyGraNaXDCvuH9bnrtfiA" "https://backend.placecard.net/api/admin/visitor-count?retrieveDays=30" | jq .
    @GetMapping(value = "/admin/visitor-count", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getVisitorCount(@RequestParam("retrieveDays") Integer retrieveDays) {
        if (retrieveDays > Visitor.SAVE_DAYS) {
            return ResponseEntity.badRequest().body("Retrieve days must be lower than: " + Visitor.SAVE_DAYS);
        }

        List<LocalDate> chartDates = retrieveChartsDates(retrieveDays);
        LocalDate fromDate = chartDates.get(0);

        Map<LocalDate, Long> visitorCountMap = new HashMap<>(visitorService.findVisitorCount(fromDate));
        chartDates.forEach(d -> visitorCountMap.putIfAbsent(d,  0L)); //add missing dates
        Map<String, Long> visitorCount = visitorCountMap.keySet().stream()
                .sorted(LocalDate::compareTo)
                .collect(Collectors.toMap(
                    d -> d.format(DateTimeFormatter.ofPattern(CHARTS_DATE_FORMAT)),
                    visitorCountMap::get,
                    Math::addExact,
                    LinkedHashMap::new)
                );
        return ResponseEntity.ok(visitorCount);
    }

    //curl -H "Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJqdGkiOiJkdnNKV1QiLCJzdWIiOiJhZG1pbiIsImF1dGhvcml0aWVzIjpbIlJPTEVfVVNFUiJdLCJpYXQiOjE2NTc0NzE4NDMsImV4cCI6MTk3MjgzMTg0M30.S16GDOuf4RU3_puN6xAuVRDNcEiAJtngFmkTfo37kqalaN3c3m9OdxGWuXv49u9jvOyGraNaXDCvuH9bnrtfiA" "http://localhost:5000/api/admin/visitor-country?retrieveDays=40" | jq .
    //curl -H "Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJqdGkiOiJkdnNKV1QiLCJzdWIiOiJhZG1pbiIsImF1dGhvcml0aWVzIjpbIlJPTEVfVVNFUiJdLCJpYXQiOjE2NTc0NzE4NDMsImV4cCI6MTk3MjgzMTg0M30.S16GDOuf4RU3_puN6xAuVRDNcEiAJtngFmkTfo37kqalaN3c3m9OdxGWuXv49u9jvOyGraNaXDCvuH9bnrtfiA" "https://backend.placecard.net/api/admin/visitor-country?retrieveDays=40" | jq .
    @GetMapping(value = "/admin/visitor-country", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getVisitorCountry(@RequestParam("retrieveDays") Integer retrieveDays) {
        if (retrieveDays > Visitor.SAVE_DAYS) {
            return ResponseEntity.badRequest().body("Retrieve days must be lower than: " + Visitor.SAVE_DAYS);
        }

        LocalDate fromDate = LocalDate.now().minusDays(Math.max(0, retrieveDays - 1));
        Map<String, Long> visitorCountryMap = new HashMap<>(visitorService.findVisitorCountByCountry(fromDate));
        return ResponseEntity.ok(visitorCountryMap);
    }

    private List<LocalDate> retrieveChartsDates(int nbChartDays) {
        List<LocalDate> dates = new ArrayList<>();

        LocalDate currentDate = LocalDate.now().minusDays(Math.max(0, nbChartDays - 1));
        for (int i = 0; i < nbChartDays; ++i) {
            dates.add(currentDate);
            currentDate = currentDate.plusDays(1);
        }

        return dates;
    }

}
