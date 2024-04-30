package edu.java.scrapper.resourceUpdaterServiceTest;

import com.github.tomakehurst.wiremock.client.MappingBuilder;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import edu.java.dto.response.LinkUpdateResponse;
import edu.java.scrapper.IntegrationTest;
import edu.java.scrapper.clients.GitHubClient;
import edu.java.scrapper.configuration.clientsConfig.ClientsConfig;
import edu.java.scrapper.domain.models.ChatLink;
import edu.java.scrapper.domain.models.Link;
import edu.java.scrapper.domain.repositories.interfaces.ChatLinkRepository;
import edu.java.scrapper.domain.repositories.interfaces.ChatRepository;
import edu.java.scrapper.domain.repositories.interfaces.GitHubResponseRepository;
import edu.java.scrapper.domain.repositories.interfaces.LinkRepository;
import edu.java.scrapper.dto.gitHubDto.RepositoryResponse;
import edu.java.scrapper.linkParser.services.GitHubParserService;
import edu.java.scrapper.scheduler.updaterWorkers.resourceUpdaterService.GitHubUpdaterService;
import edu.java.scrapper.scheduler.updaterWorkers.resourceUpdaterService.RemoverLinksService;
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
public class GitHubUpdaterServiceTest extends IntegrationTest {
    @Autowired
    private GitHubResponseRepository gitHubResponseRepository;
    @Autowired
    private LinkRepository linkRepository;
    @Autowired
    private ChatLinkRepository chatLinkRepository;
    @Autowired
    private ChatRepository chatRepository;
    @Autowired
    private RemoverLinksService removerLinksService;
    @Autowired
    private Retry gitHubRetry;
    private GitHubUpdaterService gitHubUpdaterService;
    @Autowired
    private ClientsConfig.GitHub gitHub;
    @Autowired
    private GitHubParserService linkParserService;
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
            "pushed_at": "2025-03-14T13:59:57Z"
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

    @BeforeEach
    void setUp() {
        WebClient webClient = WebClient.builder().baseUrl(WIRE_MOCK_URL).build();
        stubFor(
            prepareStub(BODY)
        );
        GitHubClient gitHubClient = new GitHubClient(webClient, gitHubRetry);

        chatRepository.add(CHAT_ID);
        linkRepository.add(link);
        Long linkId = linkRepository.findAll().getFirst().getId();
        chatLinkRepository.add(new ChatLink(CHAT_ID, linkId));

        gitHubUpdaterService = new GitHubUpdaterService(
            gitHub,
            linkParserService,
            gitHubResponseRepository,
            linkRepository,
            chatLinkRepository,
            gitHubClient,
            removerLinksService
        );
    }

    @Test
    @Transactional
    void getUpdatesTestBeforeProcessing() {
        List<RepositoryResponse> repositoriesBeforeTest = gitHubResponseRepository.findAll();
        assertThat(repositoriesBeforeTest.isEmpty()).isTrue();
    }

    @Test
    @Transactional
    void getUpdatesTest() {
        Link neededToCheckLink = linkRepository.findAll().getFirst();

        LinkUpdateResponse neededToUpdate = gitHubUpdaterService.getLinkUpdateResponse(neededToCheckLink);

        assertThat(neededToUpdate.description()).isEqualTo("Появилось обновление");

        // changing date
        stubFor(
            prepareStub(UPDATED_BODY)
        );

        List<RepositoryResponse> repoAfterTest = gitHubResponseRepository.findAll();
        LinkUpdateResponse res =
            gitHubUpdaterService.getLinkUpdateResponse(linkRepository.findAll().getFirst());

        assertThat(repoAfterTest.isEmpty()).isFalse();
        assertThat(res).isEqualTo(new LinkUpdateResponse(
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
