package bot.commandServicesTest;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.commandServices.StartCommandService;
import edu.java.bot.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
public class StartCommandServiceTest {
    @Mock
    private Message message;
    @Mock
    private Chat chat;
    @Mock
    private Update update;
    private final UserRepository users = new UserRepository();

    @InjectMocks
    private StartCommandService startCommandService = new StartCommandService(users);

    private static final User USER = new User(1L);

    @Test
    void testHandle() {
        doReturn(message).when(update).message();
        doReturn(chat).when(message).chat();
        doReturn(USER).when(message).from();
        doReturn(-1L).when(chat).id();

        String expectedHandleText = "Начинаем регистрацию...\nДля получения списка команд используйте /help";
        SendMessage result = startCommandService.handle(update);
        SendMessage expected = new SendMessage(-1L, expectedHandleText);

        assertThat(result.toWebhookResponse()).isEqualTo(expected.toWebhookResponse());
    }
}