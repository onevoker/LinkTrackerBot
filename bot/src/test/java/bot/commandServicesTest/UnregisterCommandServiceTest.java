package bot.commandServicesTest;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.clients.ScrapperTelegramChatClient;
import edu.java.bot.commandServices.UnregisterCommandService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
@ExtendWith(MockitoExtension.class)
public class UnregisterCommandServiceTest {
    @Mock
    private Message message;
    @Mock
    private Chat chat;
    @Mock
    private Update update;
    @Mock
    private ScrapperTelegramChatClient chatClient;

    @InjectMocks
    private UnregisterCommandService unregisterCommandService;

    @Test
    void testHandle() {
        doReturn(message).when(update).message();
        doReturn(chat).when(message).chat();
        doReturn(1L).when(chat).id();
        doNothing().when(chatClient).unregisterChat(1);
        unregisterCommandService = new UnregisterCommandService(chatClient);

        String expectedHandleText = "Чат успешно удален";
        SendMessage result = unregisterCommandService.handle(update);
        SendMessage expected = new SendMessage(1L, expectedHandleText);

        assertThat(result.toWebhookResponse()).isEqualTo(expected.toWebhookResponse());
    }
}
