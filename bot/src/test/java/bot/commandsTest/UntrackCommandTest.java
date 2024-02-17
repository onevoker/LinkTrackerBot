package bot.commandsTest;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.commands.Command;
import edu.java.bot.commands.UntrackCommand;
import edu.java.bot.links.LinksRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.doReturn;

public class UntrackCommandTest {
    @ExtendWith(MockitoExtension.class)

    @Mock
    private Message message;
    @Mock
    private Chat chat;
    @Mock
    private Update update;
    private final LinksRepository links = new LinksRepository();
    @InjectMocks
    private Command untrackCommand = new UntrackCommand(links);
    private static final User USER = new User(-10L);
    private static final String GIT_HUB = "https://github.com/onevoker";

    private void setUpTest(String returnedTextFromMessage) {
        doReturn(message).when(update).message();
        doReturn(USER).when(message).from();
        doReturn(returnedTextFromMessage).when(message).text();
        doReturn(chat).when(message).chat();
        doReturn(-14L).when(chat).id();
    }

    @Test
    void testCommand() {
        String nameOfCommand = untrackCommand.command();
        String expected = "/untrack";
        assertThat(nameOfCommand).isEqualTo(expected);
    }

    @Test
    void testDescription() {
        String description = untrackCommand.description();
        String expectedDescription = "Прекращение отслеживания ссылки";

        assertThat(description).isEqualTo(expectedDescription);

    }

    @Test
    void testUntrackNothing() {
        setUpTest("/untrack");

        String expectedHandleText = "Укажите что перестать отслеживать. Пример /untrack ,,ваша_ссылка,,";
        SendMessage result = untrackCommand.handle(update);
        SendMessage expected = new SendMessage(-14L, expectedHandleText);

        assertThat(result.toWebhookResponse()).isEqualTo(expected.toWebhookResponse());
    }
    @Test
    void testUntrackNotALink() {
        setUpTest("/untrack man");

        String expectedHandleText = "Вы указали неправильную ссылку, возможно вам поможет /help";
        SendMessage result = untrackCommand.handle(update);
        SendMessage expected = new SendMessage(-14L, expectedHandleText);

        assertThat(result.toWebhookResponse()).isEqualTo(expected.toWebhookResponse());
    }

    @Test
    void testUntrackUncorrectLink() {
        setUpTest("/untrack https://open.spotify.com/");

        String expectedHandleText = "Вы указали неправильную ссылку, возможно вам поможет /help";
        SendMessage result = untrackCommand.handle(update);
        SendMessage expected = new SendMessage(-14L, expectedHandleText);

        assertThat(result.toWebhookResponse()).isEqualTo(expected.toWebhookResponse());
    }

    @Test
    void testUntrackNotTrackedLink() {
        setUpTest("/untrack " + GIT_HUB);

        String expectedHandleText = "Вы не отслеживаете данную ссылку";
        SendMessage result = untrackCommand.handle(update);
        SendMessage expected = new SendMessage(-14L, expectedHandleText);

        assertThat(result.toWebhookResponse()).isEqualTo(expected.toWebhookResponse());
    }

    @Test
    void testUntrackLink() {
        links.addUserLink(USER, GIT_HUB);
        setUpTest("/untrack " + GIT_HUB);

        String expectedHandleText = "Прекратили отслеживание данной ссылки";
        SendMessage result = untrackCommand.handle(update);
        SendMessage expected = new SendMessage(-14L, expectedHandleText);

        assertThat(result.toWebhookResponse()).isEqualTo(expected.toWebhookResponse());
    }
}
