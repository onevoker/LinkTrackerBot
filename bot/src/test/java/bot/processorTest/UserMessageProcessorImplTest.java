package bot.processorTest;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.commandServices.Command;
import edu.java.bot.commandServices.HelpCommandService;
import edu.java.bot.commandServices.ListCommandService;
import edu.java.bot.commandServices.StartCommandService;
import edu.java.bot.commandServices.TrackCommandService;
import edu.java.bot.commandServices.UntrackCommandService;
import edu.java.bot.exceptions.BlockedChatException;
import edu.java.bot.links.LinkFactory;
import edu.java.bot.links.LinkValidatorService;
import edu.java.bot.processor.UserMessageProcessorImpl;
import edu.java.bot.repositories.LinkRepository;
import edu.java.bot.repositories.UserRepository;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
public class UserMessageProcessorImplTest {
    private static final List<String> supportedDomains = List.of(
        "github.com",
        "stackoverflow.com"
    );
    @Mock
    private Message message;
    @Mock
    private Update update;

    @Mock
    private Chat chat;

    @InjectMocks
    private UserMessageProcessorImpl messageProcessor;
    private static final String GIT_HUB = "https://github.com/onevoker";
    private static final User USER = new User(1L);

    private void setUpMocksWithMessageFromTelegram(String messageFromTelegram) {
        doReturn(message).when(update).message();
        doReturn(messageFromTelegram).when(message).text();
    }

    @BeforeEach
    public void setUp() {
        LinkValidatorService linkValidatorService = new LinkValidatorService(supportedDomains);
        LinkFactory linkFactory = new LinkFactory(linkValidatorService);
        LinkRepository linkRepository = new LinkRepository();
        UserRepository userRepository = new UserRepository();

        Command helpCommand = new HelpCommandService();
        Command listCommand = new ListCommandService(linkRepository);
        Command startCommand = new StartCommandService(userRepository);
        Command trackCommand = new TrackCommandService(linkRepository, linkFactory);
        Command untrackCommand = new UntrackCommandService(linkRepository, linkFactory);
        List<Command> commands = List.of(helpCommand, listCommand, startCommand, trackCommand, untrackCommand);

        this.messageProcessor =
            new UserMessageProcessorImpl(commands);
    }

    @Test
    void testProcessUnknownCommand() {
        setUpMocksWithMessageFromTelegram("привет");
        doReturn(chat).when(message).chat();
        doReturn(-1L).when(chat).id();

        String unknownCommandText =
            "Мне не известна эта команда, для получения доступных команд воспользуйтесь командой /help";
        SendMessage result = messageProcessor.process(update);
        SendMessage expected = new SendMessage(-1L, unknownCommandText).parseMode(ParseMode.Markdown);

        assertThat(result.toWebhookResponse()).isEqualTo(expected.toWebhookResponse());
    }

    @Test
    void testProcessTrackCommand() {
        setUpMocksWithMessageFromTelegram("/track " + GIT_HUB);
        doReturn(chat).when(message).chat();
        doReturn(-1L).when(chat).id();
        doReturn(USER).when(message).from();

        String expectedHandleText = "Начали отслеживать данную ссылку";
        SendMessage result = messageProcessor.process(update);
        SendMessage expected = new SendMessage(-1L, expectedHandleText).parseMode(ParseMode.HTML);

        assertThat(result.toWebhookResponse()).isEqualTo(expected.toWebhookResponse());
    }

    @Test
    void testProcessUntrackCommand() {
        setUpMocksWithMessageFromTelegram("/untrack " + GIT_HUB);
        doReturn(chat).when(message).chat();
        doReturn(-1L).when(chat).id();
        doReturn(USER).when(message).from();

        String expectedHandleText = "Вы не отслеживаете данную ссылку";
        SendMessage result = messageProcessor.process(update);
        SendMessage expected = new SendMessage(-1L, expectedHandleText).parseMode(ParseMode.HTML);

        assertThat(result.toWebhookResponse()).isEqualTo(expected.toWebhookResponse());
    }

    @Test
    void testProcessStartCommand() {
        setUpMocksWithMessageFromTelegram("/start");
        doReturn(chat).when(message).chat();
        doReturn(-1L).when(chat).id();
        doReturn(USER).when(message).from();

        String expectedHandleText = "Начинаем регистрацию...\nДля получения списка команд используйте /help";
        SendMessage result = messageProcessor.process(update);
        SendMessage expected = new SendMessage(-1L, expectedHandleText).parseMode(ParseMode.HTML);

        assertThat(result.toWebhookResponse()).isEqualTo(expected.toWebhookResponse());
    }

    @Test
    void testProcessListCommand() {
        setUpMocksWithMessageFromTelegram("/list");
        doReturn(chat).when(message).chat();
        doReturn(-1L).when(chat).id();
        doReturn(USER).when(message).from();

        String expectedHandleText = "Вы не отслеживаете ни одной ссылки(((";
        SendMessage result = messageProcessor.process(update);
        SendMessage expected = new SendMessage(-1L, expectedHandleText).parseMode(ParseMode.HTML);

        assertThat(result.toWebhookResponse()).isEqualTo(expected.toWebhookResponse());
    }

    @Test
    void testProcessHelpCommand() {
        setUpMocksWithMessageFromTelegram("/help");
        doReturn(chat).when(message).chat();
        doReturn(-1L).when(chat).id();

        String expectedHandleText =
            "На данный момент поддерживается отслеживание ссылок с таких ресурсов как github и stackoverflow\n\n"
                + "Доступные команды:\n"
                + "/start -- зарегистрировать пользователя\n"
                + "/help -- сообщение с подробным описанием команд\n"
                + "/track ,,ссылка,, -- начать отслеживание ссылки\n"
                + "/untrack ,,ссылка,, -- прекратить отслеживание ссылки\n"
                + "/list -- показать список отслеживаемых ссылок\n";
        SendMessage result = messageProcessor.process(update);
        SendMessage expected = new SendMessage(-1L, expectedHandleText).parseMode(ParseMode.HTML);

        assertThat(result.toWebhookResponse()).isEqualTo(expected.toWebhookResponse());
    }

    @Test
    void testProcessNull() {
        setUpMocksWithMessageFromTelegram(null);
        var exception = assertThrows(BlockedChatException.class, () -> messageProcessor.process(update));
        assertThat(exception.getMessage()).isEqualTo("Chat is null, because bot was blocked");
    }
}
