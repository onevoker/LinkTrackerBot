package edu.java.scheduler;

import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;

@Log4j2
public class LinkUpdaterScheduler {
    @Scheduled(fixedDelayString = "#{scheduler.forceCheckDelay()}")
    public void update() {
        log.info("checked updates in the database");
    }
}