package edu.java.scrapper.configuration;

import jakarta.validation.constraints.NotNull;
import java.time.Duration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "app", ignoreUnknownFields = false)
public record ApplicationConfig(
    @NotNull
    @Bean
    Scheduler scheduler,
    String databaseAccessType,
    RateLimitingSettings rateLimitingSettings,
    @Bean
    SwaggerEndpoints swaggerEndpoints,
    Kafka kafka,
    boolean useQueue,
    @Bean
    CustomMetrics customMetrics
) {
    public record Scheduler(boolean enable, @NotNull Duration interval, @NotNull Duration forceCheckDelay) {
    }

    public record RateLimitingSettings(int count, int tokens, Duration period) {
    }

    public record Kafka(String bootstrapServers, String topicName) {
    }

    public record SwaggerEndpoints(String swagger, String apiDocs) {
    }

    public record CustomMetrics(MessagesProcessed messagesProcessed) {
        public record MessagesProcessed(String name, String description, String tag) {
        }
    }
}
