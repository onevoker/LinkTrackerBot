package edu.java.clientTest;

import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import edu.java.gitHub.client.GitHubClient;
import edu.java.gitHub.dto.GitHubOwner;
import edu.java.gitHub.dto.RepositoryResponse;
import java.time.OffsetDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.web.reactive.function.client.WebClient;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathMatching;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@WireMockTest(httpPort = 8080)
@ExtendWith(WireMockExtension.class)
public class GitHubClientTest {
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
            "updated_at": "2024-02-09T13:59:57Z"
        }""";

    private WebClient webClient;

    @BeforeEach
    public void setUp() {
        webClient = WebClient.builder().baseUrl(WIRE_MOCK_URL).build();
    }

    @Test
    public void testGitHubClient() {
        stubFor(
            get(urlPathMatching("/repos/onevoker/LinkTrackerBot"))
                .willReturn(aResponse()
                    .withStatus(200)
                    .withHeader("Content-Type", "application/json")
                    .withBody(BODY))
        );
        GitHubClient gitHubClient = new GitHubClient(webClient);

        RepositoryResponse response = gitHubClient.fetchRepository("onevoker", "LinkTrackerBot").block();
        GitHubOwner owner = response.owner();
        long expectedId = 755139175L;
        String expectedName = "LinkTrackerBot";
        String expectedHtmlUrl = "https://github.com/onevoker/LinkTrackerBot";
        OffsetDateTime expectedUpdatedAt = OffsetDateTime.parse("2024-02-09T13:59:57Z");
        long expectedOwnerId = 146644496L;
        String expectedOwnerLogin = "onevoker";

        assertAll(
            () -> assertThat(response.id()).isEqualTo(expectedId),
            () -> assertThat(response.name()).isEqualTo(expectedName),
            () -> assertThat(response.htmlUrl()).isEqualTo(expectedHtmlUrl),
            () -> assertThat(response.updatedAt()).isEqualTo(expectedUpdatedAt),
            () -> assertThat(owner.id()).isEqualTo(expectedOwnerId),
            () -> assertThat(owner.login()).isEqualTo(expectedOwnerLogin)
        );
    }
}
