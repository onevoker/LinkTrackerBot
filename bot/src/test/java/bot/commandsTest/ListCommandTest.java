package bot.commandsTest;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.commands.Command;
import edu.java.bot.commands.ListCommand;
import edu.java.bot.links.Link;
import edu.java.bot.links.LinkRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
public class ListCommandTest {
    @Mock
    private Message message;
    @Mock
    private Chat chat;
    @Mock
    private Update update;
    private final LinkRepository links = new LinkRepository();
    @InjectMocks
    private Command listCommand = new ListCommand(links);
    private static final User USER = new User(1L);

    @Test
    void testCommand() {
        String nameOfCommand = listCommand.command();
        String expected = "/list";
        assertThat(nameOfCommand).isEqualTo(expected);
    }

    @Test
    void testHandle() {
        String strLink = "https://github.com/onevoker";
        Link link = new Link(USER.id(), strLink);
        links.addUserLink(link);

        doReturn(message).when(update).message();
        doReturn(USER).when(message).from();
        doReturn(chat).when(message).chat();
        doReturn(-1L).when(chat).id();

        String expectedHandleText = "Список ваших отслеживаемых ссылок:\n1. https://github.com/onevoker\n";
        SendMessage result = listCommand.handle(update);
        SendMessage expected = new SendMessage(-1L, expectedHandleText);

        assertThat(result.toWebhookResponse()).isEqualTo(expected.toWebhookResponse());
    }
}


