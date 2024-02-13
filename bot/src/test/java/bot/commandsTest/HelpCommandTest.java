package bot.commandsTest;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.commands.HelpCommand;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.doReturn;

class HelpCommandTest {
    @ExtendWith(MockitoExtension.class)

    @Mock
    private Message message;
    @Mock
    private Chat chat;
    @Mock
    private Update update;

    private void setUpTest() {
        doReturn(message).when(update).message();
        doReturn(chat).when(message).chat();
        doReturn(-14L).when(chat).id();
    }

    @InjectMocks
    private HelpCommand helpCommand;

    @Test
    void testCommand() {
        String nameOfCommand = helpCommand.command();
        String expected = "/help";
        assertThat(nameOfCommand).isEqualTo(expected);
    }

    @Test
    void testDescription() {
        String description = helpCommand.description();
        String expectedDescription = "Вам помогут";

        assertThat(description).isEqualTo(expectedDescription);

    }

    @Test
    void testHandle() {
        setUpTest();

        String expectedHandleText =
            "На данный момент поддерживается отслеживание ссылок с таких ресурсов как github и stackoverflow\n\n"
                + "Доступные команды:\n"
                + "/start -- зарегистрировать пользователя\n"
                + "/help -- сообщение с подробным описанием команд\n"
                + "/track ,,ссылка,, -- начать отслеживание ссылки\n"
                + "/untrack ,,ссылка,, -- прекратить отслеживание ссылки\n"
                + "/list -- показать список отслеживаемых ссылок\n";
        SendMessage result = helpCommand.handle(update);
        SendMessage expected = new SendMessage(-14L, expectedHandleText);

        assertThat(result.toWebhookResponse()).isEqualTo(expected.toWebhookResponse());
    }
}
