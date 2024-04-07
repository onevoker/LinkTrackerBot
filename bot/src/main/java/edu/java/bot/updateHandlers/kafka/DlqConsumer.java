package edu.java.bot.updateHandlers.kafka;

import edu.java.bot.dto.response.LinkUpdateResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Log4j2
public class DlqConsumer {
    @KafkaListener(topics = "${app.kafka.dlq-topic-name}", groupId = "${app.kafka.consumer-group-id}")
    public void listen(LinkUpdateResponse update) {
        log.info(update.toString());
    }
}
