package bot.messageProcessorTest;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.commandServices.CommandService;
import edu.java.bot.exceptions.BlockedChatException;
import edu.java.bot.messageProcessor.UserMessageProcessorService;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
public class UserMessageProcessorServiceTest {
    @Mock
    private Message message;
    @Mock
    private Update update;

    @Mock
    private Chat chat;

    @InjectMocks
    private UserMessageProcessorService messageProcessor;

    @BeforeEach
    public void setUp() {
        List<CommandService> commands = List.of();
        this.messageProcessor = new UserMessageProcessorService(commands);
    }

    @Test
    void testProcessUnknownCommand() {
        String command = "привет";

        doReturn(message).when(update).message();
        doReturn(command).when(message).text();
        doReturn(chat).when(message).chat();
        doReturn(-1L).when(chat).id();

        String unknownCommandText =
            "Мне не известна эта команда, для получения доступных команд воспользуйтесь командой /help";
        SendMessage result = messageProcessor.process(update).block();
        SendMessage expected = new SendMessage(-1L, unknownCommandText).parseMode(ParseMode.Markdown);

        assertThat(result.toWebhookResponse()).isEqualTo(expected.toWebhookResponse());
    }

    @Test
    void testProcessNull() {
        doReturn(message).when(update).message();

        var exception = assertThrows(BlockedChatException.class, () -> messageProcessor.process(update).block());
        assertThat(exception.getMessage()).isEqualTo("Chat is null, because bot was blocked");
    }
}
