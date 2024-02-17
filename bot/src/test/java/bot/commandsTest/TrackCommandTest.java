package bot.commandsTest;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.commands.Command;
import edu.java.bot.commands.TrackCommand;
import edu.java.bot.links.LinksRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
public class TrackCommandTest {
    @Mock
    private Message message;
    @Mock
    private Chat chat;
    @Mock
    private Update update;
    private final LinksRepository links = new LinksRepository();
    @InjectMocks
    private Command trackCommand = new TrackCommand(links);
    private static final User USER = new User(1L);
    private static final String GIT_HUB = "https://github.com/onevoker";

    private void setUpMocksWithTrackCommandFromTelegram(String returnedTextFromMessage) {
        doReturn(message).when(update).message();
        doReturn(USER).when(message).from();
        doReturn(returnedTextFromMessage).when(message).text();
        doReturn(chat).when(message).chat();
        doReturn(-1L).when(chat).id();
    }

    @Test
    void testCommand() {
        String nameOfCommand = trackCommand.command();
        String expected = "/track";
        assertThat(nameOfCommand).isEqualTo(expected);
    }

    @Test
    void testTrackNothing() {
        setUpMocksWithTrackCommandFromTelegram("/track");

        String expectedHandleText = "Введите ссылку для отслеживания. Пример ввода: /track ,,ваша_ссылка,,";
        SendMessage result = trackCommand.handle(update);
        SendMessage expected = new SendMessage(-1L, expectedHandleText);

        assertThat(result.toWebhookResponse()).isEqualTo(expected.toWebhookResponse());
    }

    @Test
    void testTrackNotALink() {
        setUpMocksWithTrackCommandFromTelegram("/track man");

        String expectedHandleText = "Вы указали неправильную ссылку, возможно вам поможет /help";
        SendMessage result = trackCommand.handle(update);
        SendMessage expected = new SendMessage(-1L, expectedHandleText);

        assertThat(result.toWebhookResponse()).isEqualTo(expected.toWebhookResponse());
    }

    @Test
    void testTrackUncorrectLink() {
        setUpMocksWithTrackCommandFromTelegram("/track https://open.spotify.com/");

        String expectedHandleText = "Вы указали неправильную ссылку, возможно вам поможет /help";
        SendMessage result = trackCommand.handle(update);
        SendMessage expected = new SendMessage(-1L, expectedHandleText);

        assertThat(result.toWebhookResponse()).isEqualTo(expected.toWebhookResponse());
    }

    @Test
    void testTrackLinkOnce() {
        setUpMocksWithTrackCommandFromTelegram("/track " + GIT_HUB);

        String expectedHandleText = "Начали отслеживать данную ссылку";
        SendMessage result = trackCommand.handle(update);
        SendMessage expected = new SendMessage(-1L, expectedHandleText);

        assertThat(result.toWebhookResponse()).isEqualTo(expected.toWebhookResponse());
    }

    @Test
    void testTrackLinkTwice() {
        setUpMocksWithTrackCommandFromTelegram("/track " + GIT_HUB);

        String expectedHandleText = "Ссылка уже добавлена, для просмотра ссылок введите /list";
        SendMessage firstTracking = trackCommand.handle(update);
        SendMessage secondTracking = trackCommand.handle(update);
        SendMessage expected = new SendMessage(-1L, expectedHandleText);

        assertThat(secondTracking.toWebhookResponse()).isEqualTo(expected.toWebhookResponse());
    }
}
