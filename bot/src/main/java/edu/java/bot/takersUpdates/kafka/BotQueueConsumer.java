package edu.java.bot.takersUpdates.kafka;

import edu.java.bot.configuration.ApplicationConfig;
import edu.java.bot.dto.response.LinkUpdateResponse;
import edu.java.bot.takersUpdates.BotUpdaterService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BotQueueConsumer {
    private final ApplicationConfig applicationConfig;
    private final BotUpdaterService updaterService;
    private final KafkaTemplate<String, LinkUpdateResponse> kafkaDlqTemplate;

    @KafkaListener(topics = "${app.kafka.topic-name}", groupId = "${app.kafka.consumer-group-id}")
    public void listen(LinkUpdateResponse update) {
        try {
            updaterService.sendUpdate(update);
        } catch (Exception e) {
            var kafka = applicationConfig.kafka();
            kafkaDlqTemplate.send(kafka.dlqTopicName(), update);
        }
    }
}
