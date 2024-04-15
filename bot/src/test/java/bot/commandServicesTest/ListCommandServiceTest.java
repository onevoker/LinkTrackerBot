package bot.commandServicesTest;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.clients.ScrapperLinkClient;
import edu.java.bot.commandServices.ListCommandService;
import edu.java.bot.dto.response.LinkResponse;
import edu.java.bot.dto.response.ListLinksResponse;
import java.net.URI;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
public class ListCommandServiceTest {
    private static final long CHAT_ID = 1L;
    @Mock
    private Message message;
    @Mock
    private Chat chat;
    @Mock
    private Update update;
    @Mock
    private ScrapperLinkClient linkClient;
    @InjectMocks
    private ListCommandService listCommand;

    @Test
    void testHandle() {
        String link = "https://github.com/onevoker";
        List<LinkResponse> list = List.of(new LinkResponse(CHAT_ID, URI.create(link)));
        ListLinksResponse listLinksResponse = new ListLinksResponse(list, list.size());

        doReturn(message).when(update).message();
        doReturn(chat).when(message).chat();
        doReturn(CHAT_ID).when(chat).id();
        doReturn(Mono.just(listLinksResponse)).when(linkClient).getTrackedLinks(CHAT_ID);
        this.listCommand = new ListCommandService(linkClient);

        String expectedHandleText = "Список ваших отслеживаемых ссылок:\n1. https://github.com/onevoker\n";
        SendMessage result = listCommand.handle(update).block();
        SendMessage expected = new SendMessage(CHAT_ID, expectedHandleText);

        assertThat(result.toWebhookResponse()).isEqualTo(expected.toWebhookResponse());
    }
}
