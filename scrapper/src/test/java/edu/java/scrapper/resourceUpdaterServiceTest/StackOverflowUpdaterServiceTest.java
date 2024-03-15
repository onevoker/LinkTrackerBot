package edu.java.scrapper.resourceUpdaterServiceTest;

import com.github.tomakehurst.wiremock.client.MappingBuilder;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import edu.java.scrapper.IntegrationTest;
import edu.java.scrapper.clients.StackOverflowClient;
import edu.java.scrapper.domain.jdbc.jdbcRepositories.JdbcChatLinkRepository;
import edu.java.scrapper.domain.jdbc.jdbcRepositories.JdbcChatRepository;
import edu.java.scrapper.domain.jdbc.jdbcRepositories.JdbcLinkRepository;
import edu.java.scrapper.domain.jdbc.jdbcRepositories.JdbcQuestionResponseRepository;
import edu.java.scrapper.domain.models.ChatLink;
import edu.java.scrapper.domain.models.Link;
import edu.java.scrapper.domain.repositories.ChatLinkRepository;
import edu.java.scrapper.domain.repositories.ChatRepository;
import edu.java.scrapper.domain.repositories.LinkRepository;
import edu.java.scrapper.domain.repositories.QuestionResponseRepository;
import edu.java.scrapper.dto.request.LinkUpdateRequest;
import edu.java.scrapper.dto.stackOverflowDto.Item;
import edu.java.scrapper.scheduler.updaterWorkers.resorceUpdaterService.StackOverflowUpdaterService;
import java.net.URI;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathMatching;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(classes = {
    JdbcQuestionResponseRepository.class,
    JdbcLinkRepository.class,
    JdbcChatLinkRepository.class,
    JdbcChatRepository.class
})
@EnableAutoConfiguration(exclude = LiquibaseAutoConfiguration.class)
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
    private StackOverflowClient stackOverflowClient;
    private StackOverflowUpdaterService stackOverflowUpdaterService;
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
                    "tags": [
                        "html",
                        "django",
                        "python-3.x",
                        "django-views",
                        "django-quiz"
                    ],
                    "owner": {
                       "account_id": 15676071,
                       "reputation": 125,
                       "user_id": 11311304,
                       "user_type": "registered",
                        "profile_image": "https://www.gravatar.com/avatar/d741f6e44e9e33b3fc044e9c328c487e?s=256&d=identicon&r=PG&f=y&so-version=2",
                       "display_name": "omkar more",
                        "link": "https://stackoverflow.com/users/11311304/omkar-more"
                    },
                    "is_answered": false,
                    "view_count": 137,
                    "answer_count": 1,
                    "score": -1,
                    "last_activity_date": 1610103022,
                    "creation_date": 1589270501,
                    "last_edit_date": 1589270864,
                    "question_id": 61746598,
                    "content_license": "CC BY-SA 4.0",
                    "link": "https://stackoverflow.com/questions/61746598/how-can-i-ask-questions-randomly-to-different-user-in-django",
                    "title": "How can I ask questions randomly to different user in django?"
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
                    "tags": [
                        "html",
                        "django",
                        "python-3.x",
                        "django-views",
                        "django-quiz"
                    ],
                    "owner": {
                       "account_id": 15676071,
                       "reputation": 125,
                       "user_id": 11311304,
                       "user_type": "registered",
                        "profile_image": "https://www.gravatar.com/avatar/d741f6e44e9e33b3fc044e9c328c487e?s=256&d=identicon&r=PG&f=y&so-version=2",
                       "display_name": "omkar more",
                        "link": "https://stackoverflow.com/users/11311304/omkar-more"
                    },
                    "is_answered": false,
                    "view_count": 137,
                    "answer_count": 1,
                    "score": -1,
                    "last_activity_date": 1610103022,
                    "creation_date": 1589270501,
                    "last_edit_date": 1600000000,
                    "question_id": 61746598,
                    "content_license": "CC BY-SA 4.0",
                    "link": "https://stackoverflow.com/questions/61746598/how-can-i-ask-questions-randomly-to-different-user-in-django",
                    "title": "How can I ask questions randomly to different user in django?"
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
    private WebClient webClient;
    private Long linkId;

    @BeforeEach
    void setUp() {
        webClient = WebClient.builder().baseUrl(WIRE_MOCK_URL).build();
        stubFor(
            prepareStub(BODY)
        );
        stackOverflowClient = new StackOverflowClient(webClient);

        chatRepository.add(CHAT_ID);
        linkRepository.add(link);
        linkId = linkRepository.findAll().getFirst().getId();
        chatLinkRepository.add(new ChatLink(CHAT_ID, linkId));

        stackOverflowUpdaterService = new StackOverflowUpdaterService(
            questionResponseRepository,
            linkRepository,
            chatLinkRepository,
            stackOverflowClient
        );
    }

    @Test
    @Transactional
    @Rollback
    void getUpdatesTestBeforeProcessing() {
        List<Item> repositoriesBeforeTest = questionResponseRepository.findAll();
        assertThat(repositoriesBeforeTest.isEmpty()).isTrue();
    }

    @Test
    @Transactional
    @Rollback
    void getUpdatesTest() {
        List<Link> neededToCheckLinks = List.of(linkRepository.findAll().getFirst());
        List<LinkUpdateRequest> noThingToUpdate = stackOverflowUpdaterService.getUpdates(neededToCheckLinks);

        assertThat(noThingToUpdate.isEmpty()).isTrue();

        // меняем дату
        stubFor(
            prepareStub(UPDATED_BODY)
        );

        List<Item> repoAfterTest = questionResponseRepository.findAll();
        List<LinkUpdateRequest> res =
            stackOverflowUpdaterService.getUpdates(List.of(linkRepository.findAll().getFirst()));

        assertThat(repoAfterTest.isEmpty()).isFalse();
        assertThat(res.getFirst()).isEqualTo(new LinkUpdateRequest(
            url,
            "Появилось обновление",
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
