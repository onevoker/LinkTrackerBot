package edu.java.bot.configuration;

import edu.java.bot.retry.BackOffType;
import jakarta.validation.constraints.NotEmpty;
import java.time.Duration;
import java.util.List;
import java.util.Set;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "app", ignoreUnknownFields = false)
public record ApplicationConfig(
    @NotEmpty
    String telegramToken,
    String scrapperBaseUrl,
    List<String> validatorRegexp,
    Duration responseTimeout,
    @Bean
    RetrySettings retrySettings,
    RateLimitingSettings rateLimitingSettings,
    @Bean
    KafkaSettings kafkaSettings,
    @Bean
    CustomMetrics customMetrics
) {
    public record RetrySettings(BackOffType backOffType, int retryCount, Duration step, Set<HttpStatus> httpStatuses) {
    }

    public record RateLimitingSettings(int count, int tokens, Duration period) {
    }

    public record KafkaSettings(String topicName,
                        String consumerGroupId,
                        String bootstrapServer,
                        String dlqTopicName) {
    }

    public record CustomMetrics(MessagesProcessed messagesProcessed) {
        public record MessagesProcessed(String name, String description, String tag) {
        }
    }
}
