package bot.kafkaTest;

import edu.java.bot.configuration.ApplicationConfig;
import edu.java.bot.dto.response.LinkUpdateResponse;
import edu.java.bot.retry.BackOfType;
import edu.java.bot.updateHandlers.BotUpdaterService;
import edu.java.bot.updateHandlers.kafka.BotQueueConsumer;
import java.time.Duration;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class BotQueueConsumerTest {

    private static final ApplicationConfig applicationConfig = new ApplicationConfig(
        null,
        null,
        List.of("https://github\\.com/[^/]+/[^/]+/?", "https://stackoverflow\\.com/questions/\\d+/[^/]+/?"),
        Duration.ofSeconds(15),
        new ApplicationConfig.RetrySettings(BackOfType.CONSTANT, 3, Duration.ofSeconds(3), Collections.emptySet()),
        null,
        new ApplicationConfig.Kafka(
            "updates",
            "bot",
            "localhost:9092",
            "edu.java.scrapper.dto.response.LinkUpdateResponse:edu.java.bot.dto.response.LinkUpdateResponse",
            "badResponse"
        ),
        null
    );

    @Mock
    private BotUpdaterService updaterService;

    @Mock
    private KafkaTemplate<String, LinkUpdateResponse> kafkaDlqTemplate;

    @InjectMocks
    private BotQueueConsumer botQueueConsumer;


    @Test
    public void testListen() {
        LinkUpdateResponse update = new LinkUpdateResponse(null, null, null);
        doNothing().when(updaterService).sendUpdate(any());

        botQueueConsumer.listen(update);

        verify(updaterService, times(1)).sendUpdate(update);
    }

    @Test
    public void testListenException() {
        LinkUpdateResponse update = new LinkUpdateResponse(null, null, null);
        doThrow(new RuntimeException()).when(updaterService).sendUpdate(any());
        botQueueConsumer = new BotQueueConsumer(applicationConfig, updaterService, kafkaDlqTemplate);

        botQueueConsumer.listen(update);

        ArgumentCaptor<LinkUpdateResponse> captor = ArgumentCaptor.forClass(LinkUpdateResponse.class);
        verify(kafkaDlqTemplate, times(1)).send(anyString(), captor.capture());

        assertThat(update).isEqualTo(captor.getValue());
    }
}
