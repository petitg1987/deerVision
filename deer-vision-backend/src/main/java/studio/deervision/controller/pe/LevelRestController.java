package studio.deervision.controller.pe;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import studio.deervision.dto.AppUsageDto;
import studio.deervision.dto.UsageDto;
import studio.deervision.exception.ApplicationException;
import studio.deervision.repository.LevelCompletionTimeRange;
import studio.deervision.service.pe.LevelService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class LevelRestController {

    private final LevelService levelService;

    @Autowired
    public LevelRestController(LevelService levelService) {
        this.levelService = levelService;
    }

    //curl -X POST -H "Content-Type: text/plain" -H "X-Key: 0-17" --data "3" "http://localhost:5000/api/pe/levels/0/completionTime?appId=photonEngineer&appVersion=1.0.0"
    @PostMapping(value = "/pe/levels/{id}/completionTime", consumes = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> levelCompletionTime(@PathVariable("id") Integer levelId, @RequestBody String value, @RequestParam("appId") String appId, @RequestParam(value="appVersion") String appVersion) {
        String requestKey = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        long completionTimeInSec;
        try {
            completionTimeInSec = Long.parseLong(value);
        } catch (NumberFormatException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
        try {
            levelService.registerLevelCompletionTime(requestKey, appId, appVersion, levelId, completionTimeInSec);
        } catch (ApplicationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
        return ResponseEntity.ok(null);
    }

    //curl -H "Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJqdGkiOiJkdnNKV1QiLCJzdWIiOiJhZG1pbiIsImF1dGhvcml0aWVzIjpbIlJPTEVfVVNFUiJdLCJpYXQiOjE2MzQ1NzIzODgsImV4cCI6MTk0OTkzMjM4OH0.S-VnMofcbTMv4epZCT3Es1zezcvXsN4xL0gmkXca3vGHsXvwa5MB1puaw6Y8wBUZLLifvXLLGZUcYvYoDvLOWQ" http://localhost:5000/api/admin/levels/completionTime?levelId=0 | jq .
    @GetMapping(value = "/admin/levels/completionTime")
    public List<LevelCompletionTimeRange> getLevelCompletionTimeRanges(@RequestParam("levelId") Integer levelId) {
        return levelService.getLevelCompletionTimeRanges(levelId);
        //TODO add missing minutes !
    }

    //curl -H "Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJqdGkiOiJkdnNKV1QiLCJzdWIiOiJhZG1pbiIsImF1dGhvcml0aWVzIjpbIlJPTEVfVVNFUiJdLCJpYXQiOjE2MzQ1NzIzODgsImV4cCI6MTk0OTkzMjM4OH0.S-VnMofcbTMv4epZCT3Es1zezcvXsN4xL0gmkXca3vGHsXvwa5MB1puaw6Y8wBUZLLifvXLLGZUcYvYoDvLOWQ" http://localhost:5000/api/admin/levels/id
    @GetMapping(value = "/admin/levels/id")
    public List<Integer> getLevelIds() {
        return levelService.getLevelIds();
    }

}
