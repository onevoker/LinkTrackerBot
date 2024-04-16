package edu.java.scrapper.senderUpdates.kafka;

import edu.java.scrapper.dto.response.LinkUpdateResponse;
import edu.java.scrapper.senderUpdates.UpdateSender;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class ScrapperQueueProducer implements UpdateSender {
    private final KafkaTemplate<String, LinkUpdateResponse> kafkaTemplate;
    private final String topicName;

    @Override
    public Mono<Void> sendUpdate(LinkUpdateResponse update) {
        return Mono.fromRunnable(() -> kafkaTemplate.send(topicName, update)).then();
//        return Mono.just(kafkaTemplate.send(topicName, update)).then();
    }
}
