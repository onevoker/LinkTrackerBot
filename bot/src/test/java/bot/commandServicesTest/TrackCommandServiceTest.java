package bot.commandServicesTest;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.clients.ScrapperLinkClient;
import edu.java.bot.commandServices.TrackCommandService;
import edu.java.bot.configuration.ApplicationConfig;
import edu.java.bot.dto.request.AddLinkRequest;
import edu.java.bot.dto.response.LinkResponse;
import edu.java.bot.linkValidators.LinkResponseFactory;
import edu.java.bot.linkValidators.LinkResponseValidatorService;
import edu.java.bot.retry.BackOfType;
import java.net.URI;
import java.time.Duration;
import java.util.Collections;
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
    private static final ApplicationConfig applicationConfig = new ApplicationConfig(
        null,
        null,
        List.of("https://github\\.com/[^/]+/[^/]+/?", "https://stackoverflow\\.com/questions/\\d+/[^/]+/?"),
        Duration.ofSeconds(15),
        new ApplicationConfig.RetrySettings(BackOfType.CONSTANT, 3, Duration.ofSeconds(3), Collections.emptySet()),
        null,
        new ApplicationConfig.Kafka(
            "updates",
            "bot",
            "localhost:9092",
            "edu.java.scrapper.dto.response.LinkUpdateResponse:edu.java.bot.dto.response.LinkUpdateResponse",
            "badResponse"
        ),
        null
    );

    private static final LinkResponseValidatorService linkValidatorService =
        new LinkResponseValidatorService(applicationConfig);
    private static final LinkResponseFactory linkFactory = new LinkResponseFactory(linkValidatorService);
    private static final URI GIT_HUB = URI.create("https://github.com/onevoker/LinkTrackerBot");

    @Test
    void testTrackNothing() {
        String command = "/track";
        doReturn(message).when(update).message();
        doReturn(command).when(message).text();
        doReturn(chat).when(message).chat();
        doReturn(CHAT_ID).when(chat).id();

        String expectedHandleText = "Введите ссылку для отслеживания. Пример ввода: /track ,,ваша_ссылка,,";
        SendMessage result = trackCommandService.handle(update).block();
        SendMessage expected = new SendMessage(CHAT_ID, expectedHandleText);

        assertThat(result.toWebhookResponse()).isEqualTo(expected.toWebhookResponse());
    }

    @Test
    void testTrackUncorrectLink() {
        String command = "/track man";

        doReturn(message).when(update).message();
        doReturn(command).when(message).text();
        doReturn(chat).when(message).chat();
        doReturn(CHAT_ID).when(chat).id();
        trackCommandService = new TrackCommandService(linkClient, linkFactory);

        String expectedHandleText = "Вы указали неправильную ссылку, возможно вам поможет /help";
        SendMessage result = trackCommandService.handle(update).block();
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
        doReturn(Mono.just(response)).when(linkClient).trackLink(CHAT_ID, request);
        trackCommandService = new TrackCommandService(linkClient, linkFactory);

        String expectedHandleText = "Начали отслеживать данную ссылку: " + GIT_HUB;
        SendMessage result = trackCommandService.handle(update).block();
        SendMessage expected = new SendMessage(CHAT_ID, expectedHandleText);

        assertThat(result.toWebhookResponse()).isEqualTo(expected.toWebhookResponse());
    }
}
