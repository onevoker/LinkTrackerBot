package edu.java.bot.updateHandlers.kafka;

import dto.response.LinkUpdateResponse;
import edu.java.bot.updateHandlers.BotUpdaterService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BotKafkaQueueConsumer {
    private final BotUpdaterService updaterService;

    @KafkaListener(topics = "${app.kafka-settings.topic-name}",
                   groupId = "${app.kafka-settings.consumer-group-id}",
                   errorHandler = "kafkaLinkUpdateResponseErrorHandler")
    public void listen(LinkUpdateResponse update) {
        updaterService.sendUpdate(update);
    }
}
