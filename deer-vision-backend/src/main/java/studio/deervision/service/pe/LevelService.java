package studio.deervision.service.pe;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import studio.deervision.config.properties.AppProperties;
import studio.deervision.exception.ApplicationException;
import studio.deervision.exception.LevelException;
import studio.deervision.model.pe.ActionCompletionTime;
import studio.deervision.repository.pe.ActionCompletionCountForMinute;
import studio.deervision.repository.pe.ActionCompletionTimeRepository;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

@Service
public class LevelService {

    public static final int MAX_COMPLETION_TIME_MIN = 35;
    public static final List<String> ACTIONS_NAMES = Arrays.asList("Open Cage Door", "Complete Puzzle");
    private static final Logger LOGGER = LoggerFactory.getLogger(LevelService.class);

    private final ActionCompletionTimeRepository actionCompletionTimeRepository;
    private final AppProperties appProperties;

    @Autowired
    public LevelService(ActionCompletionTimeRepository actionCompletionTimeRepository, AppProperties appProperties) {
        this.actionCompletionTimeRepository = actionCompletionTimeRepository;
        this.appProperties = appProperties;
    }

    public void registerLevelCompletionTime(String requestKey, String appId, String appVersion, int levelId, String actionName, long completionTimeInSec) throws ApplicationException, LevelException {
        if (!appId.equals("photonEngineer")) {
            throw new ApplicationException("Invalid application id: " + appId);
        } else if (!Pattern.matches(appProperties.getVersionPattern(), appVersion)) {
            throw new ApplicationException("Invalid application version: " + appVersion);
        } else if (!ACTIONS_NAMES.contains(actionName)) {
            throw new LevelException("Invalid action name: " + actionName);
        } else if (Math.round(completionTimeInSec / 60.0) > MAX_COMPLETION_TIME_MIN) {
            LOGGER.info("Ignore completion of {} seconds in level {} for request key: {} (reason: time too high)", completionTimeInSec, levelId, requestKey);
            return;
        } else if (actionCompletionTimeRepository.countByRequestKeyAndLevelIdAndActionName(requestKey, levelId, actionName) != 0) {
            LOGGER.info("Ignore completion of {} seconds in level {} for action name {} and for request key: {} (reason: already registered)", completionTimeInSec, levelId, actionName, requestKey);
            return;
        }
        actionCompletionTimeRepository.saveAndFlush(new ActionCompletionTime(requestKey, appVersion, levelId, actionName, completionTimeInSec));
    }

    public List<ActionCompletionCountForMinute> groupCompletionTimeByMinute(int levelId, boolean includeSnapshot) {
        return actionCompletionTimeRepository.groupCompletionTimeByMinute(levelId, includeSnapshot);
    }

    public List<Integer> getLevelIds() {
        return actionCompletionTimeRepository.findDistinctByLevelId();
    }

}
