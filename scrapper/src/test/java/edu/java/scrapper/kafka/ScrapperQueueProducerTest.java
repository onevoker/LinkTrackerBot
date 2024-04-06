package edu.java.scrapper.kafka;

import edu.java.scrapper.dto.response.LinkUpdateResponse;
import edu.java.scrapper.senderUpdates.kafka.ScrapperQueueProducer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class ScrapperQueueProducerTest {

    @Mock
    private KafkaTemplate<String, LinkUpdateResponse> kafkaTemplate;

    private ScrapperQueueProducer scrapperQueueProducer;
    private static final String TOPIC = "testTopic";

    @BeforeEach
    public void setUp() {
        scrapperQueueProducer = new ScrapperQueueProducer(kafkaTemplate, TOPIC);
    }

    @Test
    public void testSendUpdate() {
        LinkUpdateResponse update = new LinkUpdateResponse(null, null, null);

        scrapperQueueProducer.sendUpdate(update);

        verify(kafkaTemplate, times(1)).send(TOPIC, update);
    }
}
