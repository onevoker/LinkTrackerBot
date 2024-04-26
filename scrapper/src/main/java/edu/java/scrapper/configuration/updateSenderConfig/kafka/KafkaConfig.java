package edu.java.scrapper.configuration.updateSenderConfig.kafka;

import edu.java.scrapper.configuration.ApplicationConfig;
import edu.java.scrapper.dto.response.LinkUpdateResponse;
import edu.java.scrapper.senderUpdates.UpdateSender;
import edu.java.scrapper.senderUpdates.kafka.ScrapperQueueProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;

@Configuration
@ConditionalOnProperty(prefix = "app", name = "use-queue", havingValue = "true")
public class KafkaConfig {
    @Autowired
    private KafkaTemplate<String, LinkUpdateResponse> kafkaTemplate;
    @Autowired
    private ApplicationConfig applicationConfig;

    @Bean
    public UpdateSender scrapperQueueProducer() {
        String topicName = applicationConfig.kafka().topicName();
        return new ScrapperQueueProducer(kafkaTemplate, topicName);
    }
}
