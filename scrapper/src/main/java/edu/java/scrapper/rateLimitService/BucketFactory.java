package edu.java.scrapper.rateLimitService;

import edu.java.scrapper.configuration.ApplicationConfig;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BucketFactory {
    private final ApplicationConfig applicationConfig;

    public Bucket newBucket() {
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
