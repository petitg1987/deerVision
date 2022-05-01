package studio.deervision.controller.pe;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import studio.deervision.exception.ApplicationException;
import studio.deervision.service.pe.LevelService;

@RestController
@RequestMapping("/api")
public class LevelRestController {

    private final LevelService levelService;

    @Autowired
    public LevelRestController(LevelService levelService) {
        this.levelService = levelService;
    }

    //curl -X POST -H "Content-Type: text/plain" -H "X-Key: 0-17" --data "3" "http://localhost:5000/api/pe/level/0/completionTime?appId=photonEngineer&appVersion=1.0.0"
    @PostMapping(value = "/pe/level/{id}/completionTime", consumes = MediaType.TEXT_PLAIN_VALUE)
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

}
