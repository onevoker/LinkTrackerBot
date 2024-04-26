package bot.kafkaTest;

import edu.java.bot.dto.response.LinkUpdateResponse;
import edu.java.bot.updateHandlers.BotUpdaterService;
import edu.java.bot.updateHandlers.kafka.BotKafkaQueueConsumer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class BotKafkaQueueConsumerTest {
    @Mock
    private BotUpdaterService updaterService;

    @InjectMocks
    private BotKafkaQueueConsumer botKafkaQueueConsumer;

    @Test
    public void testListen() {
        LinkUpdateResponse update = new LinkUpdateResponse(null, null, null);
        doNothing().when(updaterService).sendUpdate(any());

        botKafkaQueueConsumer.listen(update);

        verify(updaterService, times(1)).sendUpdate(update);
    }
}
