package bot.commandsTest;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.commands.Command;
import edu.java.bot.commands.ListCommand;
import edu.java.bot.links.LinksRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.doReturn;

public class ListCommandTest {
    @ExtendWith(MockitoExtension.class)

    @Mock
    private Message message;
    @Mock
    private Chat chat;
    @Mock
    private Update update;
    private final LinksRepository links = new LinksRepository();
    @InjectMocks
    private Command listCommand = new ListCommand(links);
    private static final User USER = new User(-10L);

    private void setUpTest() {
        links.addUserLink(USER, "https://github.com/onevoker");

        doReturn(message).when(update).message();
        doReturn(USER).when(message).from();
        doReturn(chat).when(message).chat();
        doReturn(-14L).when(chat).id();
    }

    @Test
    void testCommand() {
        String nameOfCommand = listCommand.command();
        String expected = "/list";
        assertThat(nameOfCommand).isEqualTo(expected);
    }

    @Test
    void testDescription() {
        String description = listCommand.description();
        String expectedDescription = "Список ссылок";

        assertThat(description).isEqualTo(expectedDescription);

    }

    @Test
    void testHandle() {
        setUpTest();

        String expectedHandleText = "Список ваших отслеживаемых ссылок:\n1. https://github.com/onevoker\n";
        SendMessage result = listCommand.handle(update);
        SendMessage expected = new SendMessage(-14L, expectedHandleText);

        assertThat(result.toWebhookResponse()).isEqualTo(expected.toWebhookResponse());
    }
}


