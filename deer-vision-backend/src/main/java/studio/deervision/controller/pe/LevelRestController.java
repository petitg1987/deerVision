package studio.deervision.controller.pe;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import studio.deervision.exception.ApplicationException;
import studio.deervision.repository.pe.LevelCompletionTimeRange;
import studio.deervision.service.pe.LevelService;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class LevelRestController {

    private final LevelService levelService;

    @Autowired
    public LevelRestController(LevelService levelService) {
        this.levelService = levelService;
    }

    //curl -X POST -H "Content-Type: text/plain" -H "X-Key: 0-17" --data "cageDoorOpened:122" "http://localhost:5000/api/pe/levels/0/completionTime?appId=photonEngineer&appVersion=1.0.0"
    //curl -X POST -H "Content-Type: text/plain" -H "X-Key: 1-18" --data "cageDoorOpened:181" "http://localhost:5000/api/pe/levels/0/completionTime?appId=photonEngineer&appVersion=1.0.0"
    //curl -X POST -H "Content-Type: text/plain" -H "X-Key: 2-19" --data "cageDoorOpened:185" "http://localhost:5000/api/pe/levels/0/completionTime?appId=photonEngineer&appVersion=1.0.0"
    //curl -X POST -H "Content-Type: text/plain" -H "X-Key: 0-17" --data "puzzleCompleted:182" "http://localhost:5000/api/pe/levels/0/completionTime?appId=photonEngineer&appVersion=1.0.0"
    @PostMapping(value = "/pe/levels/{levelId}/completionTime", consumes = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> levelCompletionTime(@PathVariable("levelId") Integer levelId, @RequestBody String value, @RequestParam("appId") String appId, @RequestParam(value="appVersion") String appVersion) {
        String requestKey = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        String[] valueSplit = value.split(":");
        if (valueSplit.length != 2 || valueSplit[0].isEmpty() || valueSplit[1].isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Unexpected format for value: " + value);
        }
        String actionName = valueSplit[0];
        long completionTimeInSec;
        try {
            completionTimeInSec = Long.parseLong(valueSplit[1]);
        } catch (NumberFormatException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }

        try {
            levelService.registerLevelCompletionTime(requestKey, appId, appVersion, levelId, actionName, completionTimeInSec);
        } catch (ApplicationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
        return ResponseEntity.ok(null);
    }

    //curl -H "Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJqdGkiOiJkdnNKV1QiLCJzdWIiOiJhZG1pbiIsImF1dGhvcml0aWVzIjpbIlJPTEVfVVNFUiJdLCJpYXQiOjE2MzQ1NzIzODgsImV4cCI6MTk0OTkzMjM4OH0.S-VnMofcbTMv4epZCT3Es1zezcvXsN4xL0gmkXca3vGHsXvwa5MB1puaw6Y8wBUZLLifvXLLGZUcYvYoDvLOWQ" "http://localhost:5000/api/admin/levels/0/completionTimes?includeSnapshot=true" | jq .
    @GetMapping(value = "/admin/levels/{levelId}/completionTimes")
    public List<LevelCompletionTimeOutput> getLevelCompletionTimesGroupByMinute(@PathVariable("levelId") Integer levelId, @RequestParam("includeSnapshot") Boolean includeSnapshot) {
        List<LevelCompletionTimeRange> completionTimesGroupByMinute = levelService.getLevelCompletionTimesGroupByMinute(levelId, includeSnapshot);
        return toLevelCompletionTimeOutput(completionTimesGroupByMinute);
    }

    //curl -H "Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJqdGkiOiJkdnNKV1QiLCJzdWIiOiJhZG1pbiIsImF1dGhvcml0aWVzIjpbIlJPTEVfVVNFUiJdLCJpYXQiOjE2MzQ1NzIzODgsImV4cCI6MTk0OTkzMjM4OH0.S-VnMofcbTMv4epZCT3Es1zezcvXsN4xL0gmkXca3vGHsXvwa5MB1puaw6Y8wBUZLLifvXLLGZUcYvYoDvLOWQ" "http://localhost:5000/api/admin/levels/ids"
    @GetMapping(value = "/admin/levels/ids")
    public List<Integer> getLevelIds() {
        return levelService.getLevelIds();
    }

    List<LevelCompletionTimeOutput> toLevelCompletionTimeOutput(List<LevelCompletionTimeRange> completionTimesGroupByMinute) {
        List<LevelCompletionTimeOutput> result = new ArrayList<>();
        for (int minute = 0; minute <= LevelService.MAX_COMPLETION_TIME_MIN; ++minute) {
            int finalMinute = minute;
            List<LevelCompletionTimeRange> completionTimesMinute = completionTimesGroupByMinute.stream().filter(e -> e.getMinute() == finalMinute).toList();
            LevelCompletionTimeOutput levelCompletionTimeOutput = new LevelCompletionTimeOutput(minute);
            for (LevelCompletionTimeRange completionTimeMinute : completionTimesMinute) {
                levelCompletionTimeOutput.addQuantities(new LevelCompletionTimeQuantity(completionTimeMinute.getActionName(), completionTimeMinute.getQuantity()));
            }
            result.add(levelCompletionTimeOutput);
        }
        return result;
    }

}
