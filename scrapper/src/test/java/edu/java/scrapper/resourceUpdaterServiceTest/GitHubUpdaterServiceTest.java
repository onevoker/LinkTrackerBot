package edu.java.scrapper.resourceUpdaterServiceTest;

import com.github.tomakehurst.wiremock.client.MappingBuilder;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import edu.java.scrapper.IntegrationTest;
import edu.java.scrapper.clients.GitHubClient;
import edu.java.scrapper.configuration.ApplicationConfig;
import edu.java.scrapper.domain.models.ChatLink;
import edu.java.scrapper.domain.models.Link;
import edu.java.scrapper.domain.repositories.interfaces.ChatLinkRepository;
import edu.java.scrapper.domain.repositories.interfaces.ChatRepository;
import edu.java.scrapper.domain.repositories.interfaces.GitHubResponseRepository;
import edu.java.scrapper.domain.repositories.interfaces.LinkRepository;
import edu.java.scrapper.dto.gitHubDto.RepositoryResponse;
import edu.java.scrapper.dto.request.LinkUpdateRequest;
import edu.java.scrapper.linkParser.services.GitHubParserService;
import edu.java.scrapper.scheduler.updaterWorkers.resorceUpdaterService.GitHubUpdaterService;
import java.net.URI;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathMatching;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

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
    private static final ApplicationConfig.GitHubRegexp regexp = new ApplicationConfig.GitHubRegexp(
        "https://github\\.com/(.*?)/",
        "https://github\\.com/.*?/(.*)"
    );
    private static final GitHubParserService LINK_PARSER_SERVICE = new GitHubParserService(regexp);
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
        GitHubClient gitHubClient = new GitHubClient(webClient);

        chatRepository.add(CHAT_ID);
        linkRepository.add(link);
        Long linkId = linkRepository.findAll().getFirst().getId();
        chatLinkRepository.add(new ChatLink(CHAT_ID, linkId));

        gitHubUpdaterService = new GitHubUpdaterService(
            LINK_PARSER_SERVICE,
            gitHubResponseRepository,
            linkRepository,
            chatLinkRepository,
            gitHubClient
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
        List<Link> neededToCheckLinks = List.of(linkRepository.findAll().getFirst());
        List<LinkUpdateRequest> neededToUpdate = gitHubUpdaterService.getListLinkUpdateRequests(neededToCheckLinks);

        assertThat(neededToUpdate.getFirst().description()).isEqualTo("Появилось обновление");

        // changing date
        stubFor(
            prepareStub(UPDATED_BODY)
        );

        List<RepositoryResponse> repoAfterTest = gitHubResponseRepository.findAll();
        List<LinkUpdateRequest> res =
            gitHubUpdaterService.getListLinkUpdateRequests(List.of(linkRepository.findAll().getFirst()));

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
