package edu.java.bot.configuration;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class BucketConfig {
    private final ApplicationConfig applicationConfig;

    @Bean
    public Bucket bucket() {
        var limitingSettings = applicationConfig.rateLimitingSettings();

        Bandwidth limit = Bandwidth.builder()
            .capacity(limitingSettings.count())
            .refillGreedy(limitingSettings.tokens(), limitingSettings.period())
            .build();

        return Bucket.builder()
            .addLimit(limit)
            .build();
    }
}
