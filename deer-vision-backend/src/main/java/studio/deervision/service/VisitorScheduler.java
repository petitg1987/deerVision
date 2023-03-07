package studio.deervision.service;

import studio.deervision.model.Visitor;
import studio.deervision.repository.VisitorRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Component
public class VisitorScheduler {

    private static final Logger LOGGER = LoggerFactory.getLogger(VisitorScheduler.class);

    private final VisitorRepository visitorRepository;

    public VisitorScheduler(VisitorRepository visitorRepository) {
        this.visitorRepository = visitorRepository;
    }

    //Every 10 sec: */10 * * * * *
    //Every day at 7 o'clock: "0 0 7 * * *"
    @Scheduled(cron = "0 0 7 * * *")
    @Transactional
    public void removeOldVisitorRecord() {
        LOGGER.info("Start cleaning visitor count records");
        LocalDate compareDate = LocalDate.now().minusDays(Visitor.SAVE_DAYS);
        this.visitorRepository.deleteByVisitDateBefore(compareDate);
    }

}
