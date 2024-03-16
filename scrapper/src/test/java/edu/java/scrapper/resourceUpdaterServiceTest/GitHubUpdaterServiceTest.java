package edu.java.scrapper.resourceUpdaterServiceTest;

import com.github.tomakehurst.wiremock.client.MappingBuilder;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import edu.java.scrapper.IntegrationTest;
import edu.java.scrapper.clients.GitHubClient;
import edu.java.scrapper.domain.jdbc.jdbcRepositories.JdbcChatLinkRepository;
import edu.java.scrapper.domain.jdbc.jdbcRepositories.JdbcChatRepository;
import edu.java.scrapper.domain.jdbc.jdbcRepositories.JdbcGitHubResponseRepository;
import edu.java.scrapper.domain.jdbc.jdbcRepositories.JdbcLinkRepository;
import edu.java.scrapper.domain.models.ChatLink;
import edu.java.scrapper.domain.models.Link;
import edu.java.scrapper.domain.repositories.ChatLinkRepository;
import edu.java.scrapper.domain.repositories.ChatRepository;
import edu.java.scrapper.domain.repositories.GitHubResponseRepository;
import edu.java.scrapper.domain.repositories.LinkRepository;
import edu.java.scrapper.dto.gitHubDto.RepositoryResponse;
import edu.java.scrapper.dto.request.LinkUpdateRequest;
import edu.java.scrapper.scheduler.updaterWorkers.resorceUpdaterService.GitHubUpdaterService;
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
    JdbcGitHubResponseRepository.class,
    JdbcLinkRepository.class,
    JdbcChatLinkRepository.class,
    JdbcChatRepository.class
})
@EnableAutoConfiguration(exclude = LiquibaseAutoConfiguration.class)
@WireMockTest(httpPort = 8080)
@ExtendWith(WireMockExtension.class)
public class GitHubUpdaterServiceTest extends IntegrationTest {
    @Autowired
    private GitHubResponseRepository gitHubResponseRepository;
    @Autowired
    private LinkRepository linkRepository;
    @Autowired
    private ChatLinkRepository chatLinkRepository;
    @Autowired
    private ChatRepository chatRepository;
    private GitHubUpdaterService gitHubUpdaterService;
    private static final Long CHAT_ID = 10L;
    static final String WIRE_MOCK_URL = "http://localhost:8080/repos/";
    private static final String BODY = """
        {
            "id": 755139175,
            "name": "LinkTrackerBot",
            "owner": {
                "login": "onevoker",
                "id": 146644496
            },
            "html_url": "https://github.com/onevoker/LinkTrackerBot",
            "pushed_at": "2024-03-14T13:59:57Z"
        }""";
    private static final String UPDATED_BODY = """
        {
            "id": 755139175,
            "name": "LinkTrackerBot",
            "owner": {
                "login": "onevoker",
                "id": 146644496
            },
            "html_url": "https://github.com/onevoker/LinkTrackerBot",
            "pushed_at": "2028-03-14T13:59:57Z"
        }""";
    private static final URI URL = URI.create("https://github.com/onevoker/LinkTrackerBot");
    private final Link link =
        new Link(URL, OffsetDateTime.of(
            2024, 3, 11, 12, 13, 20, 0, ZoneOffset.UTC
        ), OffsetDateTime.of(
            2024, 3, 15, 21, 10, 40, 0, ZoneOffset.UTC
        ));
    private WebClient webClient;
    private Long linkId;

    @BeforeEach
    void setUp() {
        webClient = WebClient.builder().baseUrl(WIRE_MOCK_URL).build();
        stubFor(
            prepareStub(BODY)
        );
        GitHubClient gitHubClient = new GitHubClient(webClient);

        chatRepository.add(CHAT_ID);
        linkRepository.add(link);
        linkId = linkRepository.findAll().getFirst().getId();
        chatLinkRepository.add(new ChatLink(CHAT_ID, linkId));

        gitHubUpdaterService = new GitHubUpdaterService(
            gitHubResponseRepository,
            linkRepository,
            chatLinkRepository,
                gitHubClient
        );
    }

    @Test
    @Transactional
    @Rollback
    void getUpdatesTestBeforeProcessing() {
        List<RepositoryResponse> repositoriesBeforeTest = gitHubResponseRepository.findAll();
        assertThat(repositoriesBeforeTest.isEmpty()).isTrue();
    }

    @Test
    @Transactional
    @Rollback
    void getUpdatesTest() {
        List<Link> neededToCheckLinks = List.of(linkRepository.findAll().getFirst());
        List<LinkUpdateRequest> noThingToUpdate = gitHubUpdaterService.getUpdates(neededToCheckLinks);

        assertThat(noThingToUpdate.isEmpty()).isTrue();

        // changing date
        stubFor(
            prepareStub(UPDATED_BODY)
        );

        List<RepositoryResponse> repoAfterTest = gitHubResponseRepository.findAll();
        List<LinkUpdateRequest> res = gitHubUpdaterService.getUpdates(List.of(linkRepository.findAll().getFirst()));

        assertThat(repoAfterTest.isEmpty()).isFalse();
        assertThat(res.getFirst()).isEqualTo(new LinkUpdateRequest(
            URL,
            "Появилось обновление",
            List.of(CHAT_ID)
        ));
    }

    private MappingBuilder prepareStub(String body) {
        return get(urlPathMatching("/repos/onevoker/LinkTrackerBot"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(body));
    }
}
