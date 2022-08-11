package studio.deervision.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import studio.deervision.exception.ApplicationException;
import studio.deervision.exception.LevelException;
import studio.deervision.model.completion.ActionCompletionCountForMinute;
import studio.deervision.model.completion.ActionCompletionCountForMinuteImpl;
import studio.deervision.service.ActionCompletionTimeService;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ActionCompletionTimeRestController {

    private final ActionCompletionTimeService actionCompletionTimeService;

    @Autowired
    public ActionCompletionTimeRestController(ActionCompletionTimeService actionCompletionTimeService) {
        this.actionCompletionTimeService = actionCompletionTimeService;
    }

    //curl -X POST -H "Content-Type: text/plain" -H "X-Key: 0-17" --data "OpenCageDoor:122" "http://localhost:5000/api/pe/levels/0/completionTime?appId=photonEngineer&appVersion=1.0.0"
    //curl -X POST -H "Content-Type: text/plain" -H "X-Key: 1-18" --data "OpenCageDoor:181" "http://localhost:5000/api/pe/levels/0/completionTime?appId=photonEngineer&appVersion=1.0.0"
    //curl -X POST -H "Content-Type: text/plain" -H "X-Key: 2-19" --data "OpenCageDoor:185" "http://localhost:5000/api/pe/levels/0/completionTime?appId=photonEngineer&appVersion=1.0.0"
    //curl -X POST -H "Content-Type: text/plain" -H "X-Key: 0-17" --data "CompletePuzzle:182" "http://localhost:5000/api/pe/levels/0/completionTime?appId=photonEngineer&appVersion=1.0.0"
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
            actionCompletionTimeService.registerLevelCompletionTime(requestKey, appId, appVersion, levelId, actionName, completionTimeInSec);
        } catch (ApplicationException | LevelException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
        return ResponseEntity.ok(null);
    }

    //curl -H "Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJqdGkiOiJkdnNKV1QiLCJzdWIiOiJhZG1pbiIsImF1dGhvcml0aWVzIjpbIlJPTEVfVVNFUiJdLCJpYXQiOjE2NTc0NzE4NDMsImV4cCI6MTk3MjgzMTg0M30.S16GDOuf4RU3_puN6xAuVRDNcEiAJtngFmkTfo37kqalaN3c3m9OdxGWuXv49u9jvOyGraNaXDCvuH9bnrtfiA" "http://localhost:5000/api/admin/levels/ids?appId=photonEngineer"
    @GetMapping(value = "/admin/levels/ids")
    public List<Integer> getLevelIds(@RequestParam("appId") String appId) {
        return actionCompletionTimeService.getLevelIds(appId);
    }

    //curl -H "Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJqdGkiOiJkdnNKV1QiLCJzdWIiOiJhZG1pbiIsImF1dGhvcml0aWVzIjpbIlJPTEVfVVNFUiJdLCJpYXQiOjE2NTc0NzE4NDMsImV4cCI6MTk3MjgzMTg0M30.S16GDOuf4RU3_puN6xAuVRDNcEiAJtngFmkTfo37kqalaN3c3m9OdxGWuXv49u9jvOyGraNaXDCvuH9bnrtfiA" "http://localhost:5000/api/admin/levels/0/actionNames?appId=photonEngineer"
    @GetMapping(value = "/admin/levels/{levelId}/actionNames")
    public List<String> getActionNames(@PathVariable("levelId") Integer levelId, @RequestParam("appId") String appId) {
        return actionCompletionTimeService.getActionNames(appId, levelId);
    }

    //curl -H "Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJqdGkiOiJkdnNKV1QiLCJzdWIiOiJhZG1pbiIsImF1dGhvcml0aWVzIjpbIlJPTEVfVVNFUiJdLCJpYXQiOjE2NTc0NzE4NDMsImV4cCI6MTk3MjgzMTg0M30.S16GDOuf4RU3_puN6xAuVRDNcEiAJtngFmkTfo37kqalaN3c3m9OdxGWuXv49u9jvOyGraNaXDCvuH9bnrtfiA" "http://localhost:5000/api/admin/levels/0/completionTimes/OpenCageDoor?appId=photonEngineer&includeSnapshot=true" | jq .
    @GetMapping(value = "/admin/levels/{levelId}/completionTimes/{type}")
    public List<ActionCompletionCountForMinute> getLevelCompletionTimesGroupByMinute(@PathVariable("levelId") Integer levelId, @PathVariable("type") String actionName, @RequestParam("appId") String appId, @RequestParam("includeSnapshot") Boolean includeSnapshot) {
        return completeActionCompletionCountForMinute(actionCompletionTimeService.groupCompletionTimeByMinute(appId, levelId, actionName, includeSnapshot), actionName);
    }

    List<ActionCompletionCountForMinute> completeActionCompletionCountForMinute(List<ActionCompletionCountForMinute> actionCompletionCountForMinutes, String actionName) {
        List<ActionCompletionCountForMinute> result = new ArrayList<>();
        for (int minute = 0; minute <= ActionCompletionTimeService.MAX_COMPLETION_TIME_MIN; ++minute) {
            int finalMinute = minute;
            List<ActionCompletionCountForMinute> completionTimesMinute = actionCompletionCountForMinutes.stream().filter(e -> e.getMinute() == finalMinute).toList();

            if (completionTimesMinute.isEmpty()) {
                result.add(new ActionCompletionCountForMinuteImpl(minute, 0));
            } else if (completionTimesMinute.size() == 1) {
                result.add(new ActionCompletionCountForMinuteImpl(minute, completionTimesMinute.get(0).getPlayerCount()));
            } else {
                throw new IllegalArgumentException("Found " + completionTimesMinute.size() + " completion times minute for action " + actionName + " and for minute " + minute);
            }
        }
        return result;
    }

}
