package bot.commandsTest;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.commands.Command;
import edu.java.bot.commands.TrackCommand;
import edu.java.bot.links.UserLinks;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.doReturn;

public class TrackCommandTest {
    @ExtendWith(MockitoExtension.class)

    @Mock
    private Message message;
    @Mock
    private Chat chat;
    @Mock
    private Update update;
    private final UserLinks links = new UserLinks();
    @InjectMocks
    private Command trackCommand = new TrackCommand(links);
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
        String nameOfCommand = trackCommand.command();
        String expected = "/track";
        assertThat(nameOfCommand).isEqualTo(expected);
    }

    @Test
    void testDescription() {
        String description = trackCommand.description();
        String expectedDescription = "Отслеживание ссылки";

        assertThat(description).isEqualTo(expectedDescription);

    }

    @Test
    void testTrackNothing() {
        setUpTest("/track");

        String expectedHandleText = "Введите ссылку для отслеживания. Пример ввода: /track ,,ваша_ссылка,,";
        SendMessage result = trackCommand.handle(update);
        SendMessage expected = new SendMessage(-14L, expectedHandleText);

        assertThat(result.toWebhookResponse()).isEqualTo(expected.toWebhookResponse());
    }

    @Test
    void testTrackNotALink() {
        setUpTest("/track man");

        String expectedHandleText = "Вы указали неправильную ссылку, возможно вам поможет /help";
        SendMessage result = trackCommand.handle(update);
        SendMessage expected = new SendMessage(-14L, expectedHandleText);

        assertThat(result.toWebhookResponse()).isEqualTo(expected.toWebhookResponse());
    }

    @Test
    void testTrackUncorrectLink() {
        setUpTest("/track https://open.spotify.com/");

        String expectedHandleText = "Вы указали неправильную ссылку, возможно вам поможет /help";
        SendMessage result = trackCommand.handle(update);
        SendMessage expected = new SendMessage(-14L, expectedHandleText);

        assertThat(result.toWebhookResponse()).isEqualTo(expected.toWebhookResponse());
    }

    @Test
    void testTrackLinkOnce() {
        setUpTest("/track " + GIT_HUB);

        String expectedHandleText = "Начали отслеживать данную ссылку";
        SendMessage result = trackCommand.handle(update);
        SendMessage expected = new SendMessage(-14L, expectedHandleText);

        assertThat(result.toWebhookResponse()).isEqualTo(expected.toWebhookResponse());
    }

    @Test
    void testTrackLinkTwice() {
        setUpTest("/track " + GIT_HUB);

        String expectedHandleText = "Ссылка уже добавлена, для просмотра ссылок введите /list";
        SendMessage firstTracking = trackCommand.handle(update);
        SendMessage secondTracking = trackCommand.handle(update);
        SendMessage expected = new SendMessage(-14L, expectedHandleText);

        assertThat(secondTracking.toWebhookResponse()).isEqualTo(expected.toWebhookResponse());
    }
}
