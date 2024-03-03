package bot.commandServicesTest;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.clients.ScrapperLinkClient;
import edu.java.bot.commandServices.TrackCommandService;
import edu.java.bot.dto.request.AddLinkRequest;
import edu.java.bot.dto.response.ApiErrorResponse;
import edu.java.bot.dto.response.LinkResponse;
import edu.java.bot.exceptions.ApiException;
import java.net.URI;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

@ExtendWith(MockitoExtension.class)
public class TrackCommandServiceTest {
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
    private TrackCommandService trackCommandService;
    private static final URI GIT_HUB = URI.create("https://github.com/onevoker/LinkTrackerBot");

    @Test
    void testTrackNothing() {
        String command = "/track";
        doReturn(message).when(update).message();
        doReturn(command).when(message).text();
        doReturn(chat).when(message).chat();
        doReturn(CHAT_ID).when(chat).id();

        String expectedHandleText = "Введите ссылку для отслеживания. Пример ввода: /track ,,ваша_ссылка,,";
        SendMessage result = trackCommandService.handle(update);
        SendMessage expected = new SendMessage(CHAT_ID, expectedHandleText);

        assertThat(result.toWebhookResponse()).isEqualTo(expected.toWebhookResponse());
    }

    @Test
    void testTrackUncorrectLink() {
        String command = "/track man";
        ApiErrorResponse errorResponse = ApiErrorResponse.builder()
            .exceptionMessage("Вы указали неправильную ссылку, возможно вам поможет /help")
            .build();
        ApiException exception = new ApiException(errorResponse);
        AddLinkRequest request = new AddLinkRequest(URI.create("man"));

        doReturn(message).when(update).message();
        doReturn(command).when(message).text();
        doReturn(chat).when(message).chat();
        doReturn(CHAT_ID).when(chat).id();
        doThrow(exception).when(linkClient).trackLink(CHAT_ID, request);
        trackCommandService = new TrackCommandService(linkClient);

        String expectedHandleText = "Вы указали неправильную ссылку, возможно вам поможет /help";
        SendMessage result = trackCommandService.handle(update);
        SendMessage expected = new SendMessage(CHAT_ID, expectedHandleText);

        assertThat(result.toWebhookResponse()).isEqualTo(expected.toWebhookResponse());
    }

    @Test
    void testTrackLink() {
        String command = "/track " + GIT_HUB;
        AddLinkRequest request = new AddLinkRequest(GIT_HUB);
        LinkResponse response = new LinkResponse(CHAT_ID, GIT_HUB);

        doReturn(message).when(update).message();
        doReturn(command).when(message).text();
        doReturn(chat).when(message).chat();
        doReturn(CHAT_ID).when(chat).id();
        doReturn(response).when(linkClient).trackLink(CHAT_ID, request);
        trackCommandService = new TrackCommandService(linkClient);

        String expectedHandleText = "Начали отслеживать данную ссылку: " + GIT_HUB;
        SendMessage result = trackCommandService.handle(update);
        SendMessage expected = new SendMessage(CHAT_ID, expectedHandleText);

        assertThat(result.toWebhookResponse()).isEqualTo(expected.toWebhookResponse());
    }
}
