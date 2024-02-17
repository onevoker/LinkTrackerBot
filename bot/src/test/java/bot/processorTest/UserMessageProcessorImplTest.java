package bot.processorTest;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.commands.Command;
import edu.java.bot.commands.HelpCommand;
import edu.java.bot.commands.ListCommand;
import edu.java.bot.commands.StartCommand;
import edu.java.bot.commands.TrackCommand;
import edu.java.bot.commands.UntrackCommand;
import edu.java.bot.links.LinksRepository;
import edu.java.bot.processor.UserMessageProcessorImpl;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.doReturn;

public class UserMessageProcessorImplTest {
    @ExtendWith(MockitoExtension.class)

    @Mock
    private Message message;
    @Mock
    private Update update;

    @Mock
    private Chat chat;

    @InjectMocks
    private UserMessageProcessorImpl messageProcessor;
    private static final String GIT_HUB = "https://github.com/onevoker";
    private static final User USER = new User(-10L);

    private void setUpTest(String returnedTextFromMessage) {
        doReturn(message).when(update).message();
        doReturn(returnedTextFromMessage).when(message).text();
        doReturn(chat).when(message).chat();
        doReturn(-14L).when(chat).id();
    }

    @Test
    void testCommands() {
        LinksRepository links = new LinksRepository();

        List<? extends Command> result = messageProcessor.commands();
        List<? extends Command> expected = List.of(
            new HelpCommand(),
            new ListCommand(links),
            new StartCommand(links),
            new TrackCommand(links),
            new UntrackCommand(links)
        );

        assertThat(result).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    void testProcessUnknownCommand() {
        setUpTest("привет");

        String unknownCommandText =
            "Мне не известна эта команда, для получения доступных команд воспользуйтесь командой /help";
        SendMessage result = messageProcessor.process(update);
        SendMessage expected = new SendMessage(-14L, unknownCommandText).parseMode(ParseMode.Markdown);

        assertThat(result.toWebhookResponse()).isEqualTo(expected.toWebhookResponse());
    }

    @Test
    void testProcessTrackCommand() {
        setUpTest("/track " + GIT_HUB);
        doReturn(USER).when(message).from();

        String expectedHandleText = "Начали отслеживать данную ссылку";
        SendMessage result = messageProcessor.process(update);
        SendMessage expected = new SendMessage(-14L, expectedHandleText).parseMode(ParseMode.HTML);

        assertThat(result.toWebhookResponse()).isEqualTo(expected.toWebhookResponse());
    }

    @Test
    void testProcessUntrackCommand() {
        setUpTest("/untrack " + GIT_HUB);
        doReturn(USER).when(message).from();

        String expectedHandleText = "Вы не отслеживаете данную ссылку";
        SendMessage result = messageProcessor.process(update);
        SendMessage expected = new SendMessage(-14L, expectedHandleText).parseMode(ParseMode.HTML);

        assertThat(result.toWebhookResponse()).isEqualTo(expected.toWebhookResponse());
    }

    @Test
    void testProcessStartCommand() {
        setUpTest("/start");
        doReturn(USER).when(message).from();

        String expectedHandleText = "Начинаем регистрацию...\nДля получения списка команд используйте /help";
        SendMessage result = messageProcessor.process(update);
        SendMessage expected = new SendMessage(-14L, expectedHandleText).parseMode(ParseMode.HTML);

        assertThat(result.toWebhookResponse()).isEqualTo(expected.toWebhookResponse());
    }

    @Test
    void testProcessListCommand() {
        setUpTest("/list");
        doReturn(USER).when(message).from();

        String expectedHandleText = "Вы не отслеживаете ни одной ссылки(((";
        SendMessage result = messageProcessor.process(update);
        SendMessage expected = new SendMessage(-14L, expectedHandleText).parseMode(ParseMode.HTML);

        assertThat(result.toWebhookResponse()).isEqualTo(expected.toWebhookResponse());
    }

    @Test
    void testProcessHelpCommand() {
        setUpTest("/help");

        String expectedHandleText =
            "На данный момент поддерживается отслеживание ссылок с таких ресурсов как github и stackoverflow\n\n"
                + "Доступные команды:\n"
                + "/start -- зарегистрировать пользователя\n"
                + "/help -- сообщение с подробным описанием команд\n"
                + "/track ,,ссылка,, -- начать отслеживание ссылки\n"
                + "/untrack ,,ссылка,, -- прекратить отслеживание ссылки\n"
                + "/list -- показать список отслеживаемых ссылок\n";
        SendMessage result = messageProcessor.process(update);
        SendMessage expected = new SendMessage(-14L, expectedHandleText).parseMode(ParseMode.HTML);

        assertThat(result.toWebhookResponse()).isEqualTo(expected.toWebhookResponse());
    }
}
