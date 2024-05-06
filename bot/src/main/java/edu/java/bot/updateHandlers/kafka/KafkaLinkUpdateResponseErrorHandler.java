package edu.java.bot.updateHandlers.kafka;

import edu.java.bot.configuration.ApplicationConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.KafkaListenerErrorHandler;
import org.springframework.kafka.listener.ListenerExecutionFailedException;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Log4j2
public class KafkaLinkUpdateResponseErrorHandler implements KafkaListenerErrorHandler {
    private final ApplicationConfig.KafkaSettings kafkaSettings;
    private final KafkaTemplate<String, String> kafkaDlqTemplate;

    @Override
    public Object handleError(Message<?> message, ListenerExecutionFailedException exception) {
        log.info("Bad Response: {}", message.getPayload().toString());
        kafkaDlqTemplate.send(kafkaSettings.dlqTopicName(), message.getPayload().toString());
        return null;
    }
}
