package studio.deervision.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import studio.deervision.config.properties.AppProperties;
import studio.deervision.exception.ApplicationException;
import studio.deervision.exception.LevelException;
import studio.deervision.model.completion.ActionCompletionTime;
import studio.deervision.model.completion.ActionCompletionCountForMinute;
import studio.deervision.repository.ActionCompletionTimeRepository;

import java.util.*;
import java.util.regex.Pattern;

@Service
public class ActionCompletionTimeService {

    public static final int MAX_COMPLETION_TIME_MIN = 60;
    private static final Logger LOGGER = LoggerFactory.getLogger(ActionCompletionTimeService.class);

    private final ActionCompletionTimeRepository actionCompletionTimeRepository;
    private final AppProperties appProperties;

    @Autowired
    public ActionCompletionTimeService(ActionCompletionTimeRepository actionCompletionTimeRepository, AppProperties appProperties) {
        this.actionCompletionTimeRepository = actionCompletionTimeRepository;
        this.appProperties = appProperties;
    }

    public void registerLevelCompletionTime(String requestKey, String appId, String appVersion, int levelId, String actionName, long completionTimeInSec) throws ApplicationException, LevelException {
        if (!appId.equals("photonEngineer")) {
            throw new ApplicationException("Invalid application id: " + appId);
        } else if (!Pattern.matches(appProperties.getVersionPattern(), appVersion)) {
            throw new ApplicationException("Invalid application version: " + appVersion);
        } else if (Math.round(completionTimeInSec / 60.0) > MAX_COMPLETION_TIME_MIN) {
            LOGGER.info("Ignore completion of {} seconds in level {} for request key: {} (reason: time too high)", completionTimeInSec, levelId, requestKey);
            return;
        } else if (actionCompletionTimeRepository.countByRequestKeyAndAppIdAndLevelIdAndActionName(requestKey, appId, levelId, actionName) != 0) {
            LOGGER.info("Ignore completion of {} seconds in level {} for action name {} and for request key: {} (reason: already registered)", completionTimeInSec, levelId, actionName, requestKey);
            return;
        }
        actionCompletionTimeRepository.saveAndFlush(new ActionCompletionTime(requestKey, appId, appVersion, levelId, actionName, completionTimeInSec));
    }

    public List<Integer> getLevelIds(String appId) {
        return actionCompletionTimeRepository.findDistinctByLevelId(appId);
    }

    public List<String> getActionNames(String appId, int levelId) {
        return actionCompletionTimeRepository.findDistinctByActionName(appId, levelId);
    }

    public List<ActionCompletionCountForMinute> groupCompletionTimeByMinute(String appId, int levelId, String actionName, boolean includeSnapshot) {
        return actionCompletionTimeRepository.groupCompletionTimeByMinute(appId, levelId, actionName, includeSnapshot);
    }

}
