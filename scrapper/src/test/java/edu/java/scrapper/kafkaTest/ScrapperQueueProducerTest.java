package edu.java.scrapper.kafkaTest;

import edu.java.scrapper.dto.response.LinkUpdateResponse;
import edu.java.scrapper.senderUpdates.kafka.ScrapperQueueProducer;
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
public class ScrapperQueueProducerTest {

    @Mock
    private KafkaTemplate<String, LinkUpdateResponse> kafkaTemplate;

    @InjectMocks
    private ScrapperQueueProducer scrapperQueueProducer;

    private static final String TOPIC = "testTopic";

    @BeforeEach
    public void setUp() {
        scrapperQueueProducer = new ScrapperQueueProducer(kafkaTemplate, TOPIC);
    }

    @Test
    public void testSendUpdate() {
        LinkUpdateResponse update =
            new LinkUpdateResponse(
                URI.create("https://github.com/onevoker/LinkTrackerBot"),
                "Description",
                List.of(1L, 2L)
            );

        scrapperQueueProducer.sendUpdate(update);

        verify(kafkaTemplate, times(1)).send(eq(TOPIC), eq(update));
    }
}
