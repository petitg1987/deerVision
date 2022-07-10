package studio.deervision.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import studio.deervision.exception.ApplicationException;
import studio.deervision.exception.LevelException;
import studio.deervision.model.completion.ActionCompletionCount;
import studio.deervision.model.completion.ActionCompletionCountForMinute;
import studio.deervision.model.completion.ActionsCompletionCountForMinute;
import studio.deervision.service.ActionCompletionTimeService;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ActionCompletionTimeRestController {

    private final ActionCompletionTimeService levelService;

    @Autowired
    public ActionCompletionTimeRestController(ActionCompletionTimeService levelService) {
        this.levelService = levelService;
    }

    //curl -X POST -H "Content-Type: text/plain" -H "X-Key: 0-17" --data "Open Cage Door:122" "http://localhost:5000/api/pe/levels/0/completionTime?appId=photonEngineer&appVersion=1.0.0"
    //curl -X POST -H "Content-Type: text/plain" -H "X-Key: 1-18" --data "Open Cage Door:181" "http://localhost:5000/api/pe/levels/0/completionTime?appId=photonEngineer&appVersion=1.0.0"
    //curl -X POST -H "Content-Type: text/plain" -H "X-Key: 2-19" --data "Open Cage Door:185" "http://localhost:5000/api/pe/levels/0/completionTime?appId=photonEngineer&appVersion=1.0.0"
    //curl -X POST -H "Content-Type: text/plain" -H "X-Key: 0-17" --data "Complete Puzzle:182" "http://localhost:5000/api/pe/levels/0/completionTime?appId=photonEngineer&appVersion=1.0.0"
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
        } catch (ApplicationException | LevelException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
        return ResponseEntity.ok(null);
    }

    //curl -H "Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJqdGkiOiJkdnNKV1QiLCJzdWIiOiJhZG1pbiIsImF1dGhvcml0aWVzIjpbIlJPTEVfVVNFUiJdLCJpYXQiOjE2NTc0NzE4NDMsImV4cCI6MTk3MjgzMTg0M30.S16GDOuf4RU3_puN6xAuVRDNcEiAJtngFmkTfo37kqalaN3c3m9OdxGWuXv49u9jvOyGraNaXDCvuH9bnrtfiA" "http://localhost:5000/api/admin/levels/0/completionTimes?appId=photonEngineer&includeSnapshot=true" | jq .
    @GetMapping(value = "/admin/levels/{levelId}/completionTimes")
    public List<ActionsCompletionCountForMinute> getLevelCompletionTimesGroupByMinute(@PathVariable("levelId") Integer levelId, @RequestParam("appId") String appId, @RequestParam("includeSnapshot") Boolean includeSnapshot) {
        List<ActionCompletionCountForMinute> actionCompletionCountForMinutes = levelService.groupCompletionTimeByMinute(appId, levelId, includeSnapshot);
        return toActionsCompletionCountForMinute(actionCompletionCountForMinutes);
    }

    //curl -H "Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJqdGkiOiJkdnNKV1QiLCJzdWIiOiJhZG1pbiIsImF1dGhvcml0aWVzIjpbIlJPTEVfVVNFUiJdLCJpYXQiOjE2NTc0NzE4NDMsImV4cCI6MTk3MjgzMTg0M30.S16GDOuf4RU3_puN6xAuVRDNcEiAJtngFmkTfo37kqalaN3c3m9OdxGWuXv49u9jvOyGraNaXDCvuH9bnrtfiA" "http://localhost:5000/api/admin/levels/ids?appId=photonEngineer"
    @GetMapping(value = "/admin/levels/ids")
    public List<Integer> getLevelIds(@RequestParam("appId") String appId) {
        return levelService.getLevelIds(appId);
    }

    List<ActionsCompletionCountForMinute> toActionsCompletionCountForMinute(List<ActionCompletionCountForMinute> actionCompletionCountForMinutes) {
        List<ActionsCompletionCountForMinute> result = new ArrayList<>();
        for (int minute = 0; minute <= ActionCompletionTimeService.MAX_COMPLETION_TIME_MIN; ++minute) {
            int finalMinute = minute;
            List<ActionCompletionCountForMinute> completionTimesMinute = actionCompletionCountForMinutes.stream().filter(e -> e.getMinute() == finalMinute).toList();

            //add actions from DB
            ActionsCompletionCountForMinute actionsCompletionCountForMinute = new ActionsCompletionCountForMinute(minute);
            for (ActionCompletionCountForMinute completionTimeMinute : completionTimesMinute) {
                actionsCompletionCountForMinute.addActionCompletionCounts(new ActionCompletionCount(completionTimeMinute.getActionName(), completionTimeMinute.getPlayerCount()));
            }

            //add missing actions
            for (String actionName : ActionCompletionTimeService.ACTIONS_NAMES) {
                boolean missingAction = actionsCompletionCountForMinute.getActionCompletionCounts().stream().noneMatch(qts -> actionName.equals(qts.actionName()));
                if (missingAction) {
                    actionsCompletionCountForMinute.addActionCompletionCounts(new ActionCompletionCount(actionName, 0));
                }
            }

            actionsCompletionCountForMinute.sortActionCompletionCounts();
            result.add(actionsCompletionCountForMinute);
        }
        return result;
    }

}
