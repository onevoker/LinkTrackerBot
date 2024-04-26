package edu.java.scrapper.kafkaTest;

import edu.java.scrapper.dto.response.LinkUpdateResponse;
import edu.java.scrapper.senderUpdates.kafka.ScrapperKafkaQueueProducer;
import java.net.URI;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class ScrapperKafkaQueueProducerTest {

    @Mock
    private KafkaTemplate<String, LinkUpdateResponse> kafkaTemplate;

    @InjectMocks
    private ScrapperKafkaQueueProducer scrapperKafkaQueueProducer;

    private static final String TOPIC = "testTopic";

    @BeforeEach
    public void setUp() {
        scrapperKafkaQueueProducer = new ScrapperKafkaQueueProducer(kafkaTemplate, TOPIC);
    }

    @Test
    public void testSendUpdate() {
        LinkUpdateResponse update =
            new LinkUpdateResponse(
                URI.create("https://github.com/onevoker/LinkTrackerBot"),
                "Description",
                List.of(1L, 2L)
            );

        scrapperKafkaQueueProducer.sendUpdate(update);

        verify(kafkaTemplate, times(1)).send(eq(TOPIC), eq(update));
    }
}
