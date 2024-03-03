package bot.commandServicesTest;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.commandServices.HelpCommandService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class HelpCommandServiceTest {
    @Mock
    private Message message;
    @Mock
    private Chat chat;
    @Mock
    private Update update;

    @InjectMocks
    private HelpCommandService helpCommandService;

    @Test
    void testHandle() {
        doReturn(message).when(update).message();
        doReturn(chat).when(message).chat();
        doReturn(1L).when(chat).id();

        String expectedHandleText =
            "На данный момент поддерживается отслеживание ссылок с таких ресурсов как github и stackoverflow\n\n"
                + "Доступные команды:\n"
                + "/start -- зарегистрировать пользователя\n"
                + "/help -- сообщение с подробным описанием команд\n"
                + "/track ,,ссылка,, -- начать отслеживание ссылки\n"
                + "/untrack ,,ссылка,, -- прекратить отслеживание ссылки\n"
                + "/list -- показать список отслеживаемых ссылок\n";
        SendMessage result = helpCommandService.handle(update);
        SendMessage expected = new SendMessage(1L, expectedHandleText);

        assertThat(result.toWebhookResponse()).isEqualTo(expected.toWebhookResponse());
    }
}
