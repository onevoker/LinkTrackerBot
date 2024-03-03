package bot.commandServicesTest;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.clients.ScrapperLinkClient;
import edu.java.bot.commandServices.UntrackCommandService;
import edu.java.bot.dto.request.RemoveLinkRequest;
import edu.java.bot.dto.response.ApiErrorResponse;
import edu.java.bot.dto.response.LinkResponse;
import edu.java.bot.exceptions.ApiException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.net.URI;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

@ExtendWith(MockitoExtension.class)
public class UntrackCommandServiceTest {
    private static final Long CHAT_ID = 1L;
    @Mock
    private Message message;
    @Mock
    private Chat chat;
    @Mock
    private Update update;
    @Mock
    private ScrapperLinkClient linkClient;
    @InjectMocks
    private UntrackCommandService untrackCommandService;
    private static final URI GIT_HUB = URI.create("https://github.com/onevoker/LinkTrackerBot");


    @Test
    void testUntrackNothing() {
        String command = "/untrack";
        doReturn(message).when(update).message();
        doReturn(command).when(message).text();
        doReturn(chat).when(message).chat();
        doReturn(CHAT_ID).when(chat).id();

        String expectedHandleText = "Укажите что перестать отслеживать. Пример /untrack ,,ваша_ссылка,,";
        SendMessage result = untrackCommandService.handle(update);
        SendMessage expected = new SendMessage(CHAT_ID, expectedHandleText);

        assertThat(result.toWebhookResponse()).isEqualTo(expected.toWebhookResponse());
    }

    @Test
    void testUntrackUncorrectLink() {
        String command = "/untrack man";
        ApiErrorResponse errorResponse = ApiErrorResponse.builder()
            .exceptionMessage("Вы указали неправильную ссылку, возможно вам поможет /help")
            .build();
        ApiException exception = new ApiException(errorResponse);
        RemoveLinkRequest request = new RemoveLinkRequest(URI.create("man"));

        doReturn(message).when(update).message();
        doReturn(command).when(message).text();
        doReturn(chat).when(message).chat();
        doReturn(CHAT_ID).when(chat).id();
        doThrow(exception).when(linkClient).untrackLink(CHAT_ID, request);
        untrackCommandService = new UntrackCommandService(linkClient);

        String expectedHandleText = "Вы указали неправильную ссылку, возможно вам поможет /help";
        SendMessage result = untrackCommandService.handle(update);
        SendMessage expected = new SendMessage(CHAT_ID, expectedHandleText);

        assertThat(result.toWebhookResponse()).isEqualTo(expected.toWebhookResponse());
    }

    @Test
    void testUntrackNotTrackedLink() {
        String command = "/untrack " + GIT_HUB;
        ApiErrorResponse errorResponse = ApiErrorResponse.builder()
            .exceptionMessage("Вы не отслеживаете данную ссылку")
            .build();
        ApiException exception = new ApiException(errorResponse);
        RemoveLinkRequest request = new RemoveLinkRequest(GIT_HUB);

        doReturn(message).when(update).message();
        doReturn(command).when(message).text();
        doReturn(chat).when(message).chat();
        doReturn(CHAT_ID).when(chat).id();
        doThrow(exception).when(linkClient).untrackLink(CHAT_ID, request);
        untrackCommandService = new UntrackCommandService(linkClient);

        String expectedHandleText = "Вы не отслеживаете данную ссылку";
        SendMessage result = untrackCommandService.handle(update);
        SendMessage expected = new SendMessage(CHAT_ID, expectedHandleText);

        assertThat(result.toWebhookResponse()).isEqualTo(expected.toWebhookResponse());
    }

    @Test
    void testUntrackLink() {
        String command = "/untrack " + GIT_HUB;
        RemoveLinkRequest request = new RemoveLinkRequest(GIT_HUB);
        LinkResponse linkResponse = new LinkResponse(CHAT_ID, GIT_HUB);

        doReturn(message).when(update).message();
        doReturn(command).when(message).text();
        doReturn(chat).when(message).chat();
        doReturn(CHAT_ID).when(chat).id();
        doReturn(linkResponse).when(linkClient).untrackLink(CHAT_ID, request);
        untrackCommandService = new UntrackCommandService(linkClient);

        String expectedHandleText = "Прекратили отслеживание данной ссылки: " + GIT_HUB;
        SendMessage result = untrackCommandService.handle(update);
        SendMessage expected = new SendMessage(CHAT_ID, expectedHandleText);

        assertThat(result.toWebhookResponse()).isEqualTo(expected.toWebhookResponse());
    }
}
