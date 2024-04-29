package edu.java.scrapper.resourceUpdaterServiceTest;

import com.github.tomakehurst.wiremock.client.MappingBuilder;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import dto.response.LinkUpdateResponse;
import edu.java.scrapper.IntegrationTest;
import edu.java.scrapper.clients.StackOverflowClient;
import edu.java.scrapper.configuration.resourcesConfig.ClientsConfig;
import edu.java.scrapper.domain.models.ChatLink;
import edu.java.scrapper.domain.models.Link;
import edu.java.scrapper.domain.repositories.interfaces.ChatLinkRepository;
import edu.java.scrapper.domain.repositories.interfaces.ChatRepository;
import edu.java.scrapper.domain.repositories.interfaces.LinkRepository;
import edu.java.scrapper.domain.repositories.interfaces.QuestionResponseRepository;
import edu.java.scrapper.dto.stackOverflowDto.Item;
import edu.java.scrapper.linkParser.services.StackOverflowParserService;
import edu.java.scrapper.scheduler.updaterWorkers.resourceUpdaterService.RemoverLinksService;
import edu.java.scrapper.scheduler.updaterWorkers.resourceUpdaterService.StackOverflowUpdaterService;
import io.github.resilience4j.retry.Retry;
import java.net.URI;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathMatching;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@WireMockTest(httpPort = 8080)
@ExtendWith(WireMockExtension.class)
public class StackOverflowUpdaterServiceTest extends IntegrationTest {
    @Autowired
    private QuestionResponseRepository questionResponseRepository;
    @Autowired
    private LinkRepository linkRepository;
    @Autowired
    private ChatLinkRepository chatLinkRepository;
    @Autowired
    private ChatRepository chatRepository;
    @Autowired
    private RemoverLinksService removerLinksService;
    @Autowired
    private Retry stackOverflowRetry;
    private StackOverflowUpdaterService stackOverflowUpdaterService;
    @Autowired
    private ClientsConfig.StackOverflow stackOverflow;
    @Autowired
    private StackOverflowParserService linkParserService;
    private static final Long CHAT_ID = 10L;
    static final String WIRE_MOCK_URL = "http://localhost:8080/2.3/questions/";
    private static final long QUESTION_ID = 61746598L;
    private static final URI url = URI.create(
        "https://stackoverflow.com/questions/%s/python-functional-method-of-checking-that-all-elements-in-a-list-are-equal"
            .formatted(QUESTION_ID)
    );
    private static final String BODY = """
        {
            "items": [
                {
                    "is_answered": false,
                    "answer_count": 1,
                    "last_activity_date": 1610103022,
                    "creation_date": 1589270501,
                    "last_edit_date": 1589270864,
                    "question_id": 61746598
                }
            ],
            "has_more": false,
            "quota_max": 300,
            "quota_remaining": 282
        }""";

    private static final String UPDATED_BODY = """
        {
            "items": [
                {
                    "is_answered": true,
                    "answer_count": 3,
                    "last_activity_date": 1800000000,
                    "creation_date": 1589270501,
                    "last_edit_date": 1589270864,
                    "question_id": 61746598
                }
            ],
            "has_more": false,
            "quota_max": 300,
            "quota_remaining": 282
        }""";
    private final Link link =
        new Link(
            url,
            OffsetDateTime.of(
                2024, 3, 11, 12, 13, 20, 0, ZoneOffset.UTC
            ),
            OffsetDateTime.of(
                2024, 3, 15, 21, 10, 40, 0, ZoneOffset.UTC
            )
        );

    @BeforeEach
    void setUp() {
        WebClient webClient = WebClient.builder().baseUrl(WIRE_MOCK_URL).build();
        stubFor(
            prepareStub(BODY)
        );
        StackOverflowClient stackOverflowClient = new StackOverflowClient(webClient, stackOverflowRetry);

        chatRepository.add(CHAT_ID);
        linkRepository.add(link);
        Long linkId = linkRepository.findAll().getFirst().getId();
        chatLinkRepository.add(new ChatLink(CHAT_ID, linkId));

        stackOverflowUpdaterService = new StackOverflowUpdaterService(
            stackOverflow,
            linkParserService,
            questionResponseRepository,
            linkRepository,
            chatLinkRepository,
            stackOverflowClient,
            removerLinksService
        );
    }

    @Test
    @Transactional
    void getUpdatesTestBeforeProcessing() {
        List<Item> repositoriesBeforeTest = questionResponseRepository.findAll();
        assertThat(repositoriesBeforeTest.isEmpty()).isTrue();
    }

    @Test
    @Transactional
    void getUpdatesTest() {
        Link neededToCheckLink = linkRepository.findAll().getFirst();
        LinkUpdateResponse noThingToUpdate =
            stackOverflowUpdaterService.getLinkUpdateResponse(neededToCheckLink);

        assertThat(noThingToUpdate).isNull();

        // changing date, answer_count and is_answered
        stubFor(
            prepareStub(UPDATED_BODY)
        );

        List<Item> repoAfterTest = questionResponseRepository.findAll();
        LinkUpdateResponse res =
            stackOverflowUpdaterService.getLinkUpdateResponse(linkRepository.findAll().getFirst());

        assertThat(repoAfterTest.isEmpty()).isFalse();
        assertThat(res).isEqualTo(new LinkUpdateResponse(
            url,
            "Появилось обновление\nБыл добавлен ответ на вопрос\nНа вопрос ответили",
            List.of(CHAT_ID)
        ));
    }

    private MappingBuilder prepareStub(String body) {
        return get(urlPathMatching("/2.3/questions/" + QUESTION_ID))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(body));
    }
}
