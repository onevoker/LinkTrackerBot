package edu.java.bot.configuration;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class CounterConfig {
    private final ApplicationConfig.CustomMetrics customMetrics;
    private final MeterRegistry meterRegistry;
    @Value("${spring.application.name}")
    private String applicationName;

    @Bean
    public Counter messagesProcessedCounter() {
        var messagesProcessedMetric = customMetrics.messagesProcessed();

        return Counter
            .builder(messagesProcessedMetric.name())
            .description(messagesProcessedMetric.description())
            .tag(messagesProcessedMetric.tag(), applicationName)
            .register(meterRegistry);
    }
}
