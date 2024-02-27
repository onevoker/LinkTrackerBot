package bot.commandsTest;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.commands.UntrackCommand;
import edu.java.bot.configuration.ApplicationConfig;
import edu.java.bot.links.Link;
import edu.java.bot.links.LinkFactory;
import edu.java.bot.links.LinkValidatorService;
import edu.java.bot.repositories.LinkRepository;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
public class UntrackCommandTest {
    private static final List<String> supportedDomains = List.of(
        "github.com",
        "stackoverflow.com"
    );
    private final LinkRepository linkRepository = new LinkRepository();
    @Mock
    private Message message;
    @Mock
    private Chat chat;
    @Mock
    private Update update;
    @InjectMocks
    private UntrackCommand untrackCommand;
    private static final User USER = new User(1L);
    private static final String GIT_HUB = "https://github.com/onevoker";
    private static final Link GIT_HUB_LINK = new Link(USER.id(), GIT_HUB);

    private void setUpMocksWithUntrackCommandFromTelegram(String messageFromTelegram) {
        doReturn(message).when(update).message();
        doReturn(USER).when(message).from();
        doReturn(messageFromTelegram).when(message).text();
        doReturn(chat).when(message).chat();
        doReturn(-1L).when(chat).id();
    }

    @BeforeEach
    public void setUp() {
        LinkValidatorService linkValidatorService = new LinkValidatorService(supportedDomains);
        LinkFactory linkFactory = new LinkFactory(linkValidatorService);

        this.untrackCommand = new UntrackCommand(linkRepository, linkFactory);
    }

    @Test
    void testUntrackNothing() {
        setUpMocksWithUntrackCommandFromTelegram("/untrack");

        String expectedHandleText = "Укажите что перестать отслеживать. Пример /untrack ,,ваша_ссылка,,";
        SendMessage result = untrackCommand.handle(update);
        SendMessage expected = new SendMessage(-1L, expectedHandleText);

        assertThat(result.toWebhookResponse()).isEqualTo(expected.toWebhookResponse());
    }

    @Test
    void testUntrackNotALink() {
        setUpMocksWithUntrackCommandFromTelegram("/untrack man");

        String expectedHandleText = "Вы указали неправильную ссылку, возможно вам поможет /help";
        SendMessage result = untrackCommand.handle(update);
        SendMessage expected = new SendMessage(-1L, expectedHandleText);

        assertThat(result.toWebhookResponse()).isEqualTo(expected.toWebhookResponse());
    }

    @Test
    void testUntrackUncorrectLink() {
        setUpMocksWithUntrackCommandFromTelegram("/untrack https://open.spotify.com/");

        String expectedHandleText = "Вы указали неправильную ссылку, возможно вам поможет /help";
        SendMessage result = untrackCommand.handle(update);
        SendMessage expected = new SendMessage(-1L, expectedHandleText);

        assertThat(result.toWebhookResponse()).isEqualTo(expected.toWebhookResponse());
    }

    @Test
    void testUntrackNotTrackedLink() {
        setUpMocksWithUntrackCommandFromTelegram("/untrack " + GIT_HUB);

        String expectedHandleText = "Вы не отслеживаете данную ссылку";
        SendMessage result = untrackCommand.handle(update);
        SendMessage expected = new SendMessage(-1L, expectedHandleText);

        assertThat(result.toWebhookResponse()).isEqualTo(expected.toWebhookResponse());
    }

    @Test
    void testUntrackLink() {
        linkRepository.addUserLink(GIT_HUB_LINK);
        setUpMocksWithUntrackCommandFromTelegram("/untrack " + GIT_HUB);

        String expectedHandleText = "Прекратили отслеживание данной ссылки";
        SendMessage result = untrackCommand.handle(update);
        SendMessage expected = new SendMessage(-1L, expectedHandleText);

        assertThat(result.toWebhookResponse()).isEqualTo(expected.toWebhookResponse());
    }
}
