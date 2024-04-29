package edu.java.scrapper.senderUpdates.kafka;

import dto.response.LinkUpdateResponse;
import edu.java.scrapper.senderUpdates.UpdateSender;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;

@RequiredArgsConstructor
public class ScrapperKafkaQueueProducer implements UpdateSender {
    private final KafkaTemplate<String, LinkUpdateResponse> kafkaTemplate;
    private final String topicName;

    @Override
    public void sendUpdate(LinkUpdateResponse update) {
        kafkaTemplate.send(topicName, update);
    }
}
