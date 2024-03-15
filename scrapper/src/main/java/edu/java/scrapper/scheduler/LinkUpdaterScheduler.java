package edu.java.scrapper.scheduler;

import edu.java.scrapper.clients.BotClient;
import edu.java.scrapper.dto.request.LinkUpdateRequest;
import edu.java.scrapper.scheduler.updaterWorkers.LinkUpdaterService;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Log4j2
@Component
@RequiredArgsConstructor
public class LinkUpdaterScheduler {
    private final BotClient botClient;
    private final LinkUpdaterService linkUpdaterService;

    @Scheduled(fixedDelayString = "#{scheduler.interval()}")
    public void update() {
        OffsetDateTime timeNow = OffsetDateTime.now().with(ZoneOffset.UTC);
        List<LinkUpdateRequest> requests = linkUpdaterService.getUpdates(timeNow);

        if (!requests.isEmpty()) {
            for (var request : requests) {
                botClient.sendUpdate(request);
            }
        } else {
            log.info("No updates");
        }
    }
}
