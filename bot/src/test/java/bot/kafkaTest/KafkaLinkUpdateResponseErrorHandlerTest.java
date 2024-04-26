package bot.kafkaTest;

import edu.java.bot.configuration.ApplicationConfig;
import edu.java.bot.updateHandlers.kafka.KafkaLinkUpdateResponseErrorHandler;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.ListenerExecutionFailedException;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.GenericMessage;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class KafkaLinkUpdateResponseErrorHandlerTest {
    private static final String DLQ_TOPIC_NAME = "badResponse";

    @Mock
    private ApplicationConfig.KafkaSettings kafkaSettings;

    @Mock
    private KafkaTemplate<String, String> kafkaDlqTemplate;
    @Mock
    private ListenerExecutionFailedException exception;

    @InjectMocks
    private KafkaLinkUpdateResponseErrorHandler errorHandler;

    @Test
    public void testHandleError() {
        String invalidMessage = "{\"a\": \"badValue\"}";

        doReturn(DLQ_TOPIC_NAME).when(kafkaSettings).dlqTopicName();

        Message<?> message = new GenericMessage<>(invalidMessage);
        errorHandler.handleError(message, exception);

        verify(kafkaDlqTemplate).send(eq(DLQ_TOPIC_NAME), eq(invalidMessage));
    }
}
