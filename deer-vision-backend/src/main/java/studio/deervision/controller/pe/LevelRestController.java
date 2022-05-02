package studio.deervision.controller.pe;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import studio.deervision.exception.ApplicationException;
import studio.deervision.repository.LevelCompletionTimeRange;
import studio.deervision.service.pe.LevelService;

import java.util.Comparator;
import java.util.List;

@RestController
@RequestMapping("/api")
public class LevelRestController {

    private final LevelService levelService;

    @Autowired
    public LevelRestController(LevelService levelService) {
        this.levelService = levelService;
    }

    //curl -X POST -H "Content-Type: text/plain" -H "X-Key: 0-17" --data "3" "http://localhost:5000/api/pe/levels/0/completionTime?appId=photonEngineer&appVersion=1.0.0"
    @PostMapping(value = "/pe/levels/{levelId}/completionTime", consumes = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> levelCompletionTime(@PathVariable("levelId") Integer levelId, @RequestBody String value, @RequestParam("appId") String appId, @RequestParam(value="appVersion") String appVersion) {
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

    //curl -H "Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJqdGkiOiJkdnNKV1QiLCJzdWIiOiJhZG1pbiIsImF1dGhvcml0aWVzIjpbIlJPTEVfVVNFUiJdLCJpYXQiOjE2MzQ1NzIzODgsImV4cCI6MTk0OTkzMjM4OH0.S-VnMofcbTMv4epZCT3Es1zezcvXsN4xL0gmkXca3vGHsXvwa5MB1puaw6Y8wBUZLLifvXLLGZUcYvYoDvLOWQ" "http://localhost:5000/api/admin/levels/0/completionTimes?ignoreSnapshot=false" | jq .
    @GetMapping(value = "/admin/levels/{levelId}/completionTimes")
    public List<LevelCompletionTimeRange> getLevelCompletionTimesGroupByMinute(@PathVariable("levelId") Integer levelId, @RequestParam("ignoreSnapshot") Boolean ignoreSnapshot) {
        List<LevelCompletionTimeRange> completionTimesGroupByMinute = levelService.getLevelCompletionTimesGroupByMinute(levelId, ignoreSnapshot);
        addMissingMinutes(completionTimesGroupByMinute);
        return completionTimesGroupByMinute;
    }

    //curl -H "Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJqdGkiOiJkdnNKV1QiLCJzdWIiOiJhZG1pbiIsImF1dGhvcml0aWVzIjpbIlJPTEVfVVNFUiJdLCJpYXQiOjE2MzQ1NzIzODgsImV4cCI6MTk0OTkzMjM4OH0.S-VnMofcbTMv4epZCT3Es1zezcvXsN4xL0gmkXca3vGHsXvwa5MB1puaw6Y8wBUZLLifvXLLGZUcYvYoDvLOWQ" "http://localhost:5000/api/admin/levels/ids"
    @GetMapping(value = "/admin/levels/ids")
    public List<Integer> getLevelIds() {
        return levelService.getLevelIds();
    }

    void addMissingMinutes(List<LevelCompletionTimeRange> completionTimeGroupByMinute) {
        for (int minute = 0; minute <= LevelService.MAX_COMPLETION_TIME_MIN; ++minute) {
            boolean minuteExist = false;
            for (LevelCompletionTimeRange lctr : completionTimeGroupByMinute) {
                minuteExist = minuteExist || lctr.getMinute() == minute;
            }
            if (!minuteExist) {
                final int missingMinute = minute;
                completionTimeGroupByMinute.add(new LevelCompletionTimeRange() {
                    public Integer getMinute() {
                        return missingMinute;
                    }
                    public Integer getQuantity() {
                        return 0;
                    }
                });
            }
        }
        completionTimeGroupByMinute.sort(Comparator.comparing(LevelCompletionTimeRange::getMinute));
    }

}
