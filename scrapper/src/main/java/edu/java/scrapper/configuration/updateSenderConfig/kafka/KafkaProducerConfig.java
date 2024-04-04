package edu.java.scrapper.configuration.updateSenderConfig.kafka;

import edu.java.scrapper.configuration.ApplicationConfig;
import edu.java.scrapper.dto.response.LinkUpdateResponse;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.serializer.JsonSerializer;

@Configuration
@RequiredArgsConstructor
public class KafkaProducerConfig {
    private final ApplicationConfig applicationConfig;

    @Bean
    public KafkaTemplate<String, LinkUpdateResponse> kafkaTemplate() {
        var kafka = applicationConfig.kafka();

        return new KafkaTemplate<>(
            new DefaultKafkaProducerFactory<>(Map.of(
                ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafka.bootstrapServers(),
                ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class,
                ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class
            ))
        );
    }
}
