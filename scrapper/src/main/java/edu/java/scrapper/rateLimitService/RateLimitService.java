package edu.java.scrapper.rateLimitService;

import edu.java.scrapper.controllers.exceptions.RateLimitException;
import io.github.bucket4j.Bucket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RateLimitService {
    private final BucketFactory bucketFactory;
    private final Map<Long, Bucket> buckets = new ConcurrentHashMap<>();
    private static final int TOKEN_COUNT = 1;

    public void consume(Long chatId) {
        Bucket bucket = buckets.computeIfAbsent(chatId, k -> bucketFactory.newBucket());
        if (!bucket.tryConsume(TOKEN_COUNT)) {
            throw new RateLimitException();
        }
    }
}

