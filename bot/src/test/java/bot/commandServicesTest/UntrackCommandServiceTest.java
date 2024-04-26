package bot.commandServicesTest;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.clients.ScrapperLinkClient;
import edu.java.bot.commandServices.UntrackCommandService;
import edu.java.bot.configuration.ApplicationConfig;
import edu.java.bot.dto.request.RemoveLinkRequest;
import edu.java.bot.dto.response.ApiErrorResponse;
import edu.java.bot.dto.response.LinkResponse;
import edu.java.bot.exceptions.ApiException;
import edu.java.bot.linkValidators.LinkResponseFactory;
import edu.java.bot.linkValidators.LinkResponseValidatorService;
import edu.java.bot.retry.BackOffType;
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
    private static final ApplicationConfig applicationConfig = new ApplicationConfig(
        null,
        null,
        List.of("https://github\\.com/[^/]+/[^/]+/?", "https://stackoverflow\\.com/questions/\\d+/[^/]+/?"),
        Duration.ofSeconds(15),
        new ApplicationConfig.RetrySettings(BackOffType.CONSTANT, 3, Duration.ofSeconds(3), Collections.emptySet()),
        null,
        new ApplicationConfig.KafkaSettings(
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
    void testUntrackNothing() {
        String command = "/untrack";
        doReturn(message).when(update).message();
        doReturn(command).when(message).text();
        doReturn(chat).when(message).chat();
        doReturn(CHAT_ID).when(chat).id();

        String expectedHandleText = "Укажите что перестать отслеживать. Пример /untrack ,,ваша_ссылка,,";
        SendMessage result = untrackCommandService.handle(update).block();
        SendMessage expected = new SendMessage(CHAT_ID, expectedHandleText);

        assertThat(result.toWebhookResponse()).isEqualTo(expected.toWebhookResponse());
    }

    @Test
    void testUntrackUncorrectLink() {
        String command = "/untrack man";

        doReturn(message).when(update).message();
        doReturn(command).when(message).text();
        doReturn(chat).when(message).chat();
        doReturn(CHAT_ID).when(chat).id();
        untrackCommandService = new UntrackCommandService(linkClient, linkFactory);

        String expectedHandleText = "Вы указали неправильную ссылку, возможно вам поможет /help";
        SendMessage result = untrackCommandService.handle(update).block();
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
        doReturn(Mono.error(exception)).when(linkClient).untrackLink(CHAT_ID, request);
        untrackCommandService = new UntrackCommandService(linkClient, linkFactory);

        String expectedHandleText = "Вы не отслеживаете данную ссылку";
        SendMessage result = untrackCommandService.handle(update).block();
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
        doReturn(Mono.just(linkResponse)).when(linkClient).untrackLink(CHAT_ID, request);
        untrackCommandService = new UntrackCommandService(linkClient, linkFactory);

        String expectedHandleText = "Прекратили отслеживание данной ссылки: " + GIT_HUB;
        SendMessage result = untrackCommandService.handle(update).block();
        SendMessage expected = new SendMessage(CHAT_ID, expectedHandleText);

        assertThat(result.toWebhookResponse()).isEqualTo(expected.toWebhookResponse());
    }
}
