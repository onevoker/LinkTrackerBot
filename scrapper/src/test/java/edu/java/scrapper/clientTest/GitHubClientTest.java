package edu.java.scrapper.clientTest;

import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import edu.java.scrapper.clients.GitHubClient;
import edu.java.scrapper.dto.gitHubDto.RepositoryResponse;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryConfig;
import java.time.Duration;
import java.time.OffsetDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathMatching;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@WireMockTest(httpPort = 8800)
@ExtendWith(WireMockExtension.class)
public class GitHubClientTest {
    static final String WIRE_MOCK_URL = "http://localhost:8800/repos/";
    private static final String BODY = """
        {
            "id": 755139175,
            "name": "LinkTrackerBot",
            "owner": {
                "login": "onevoker",
                "id": 146644496
            },
            "html_url": "https://github.com/onevoker/LinkTrackerBot",
            "pushed_at": "2024-02-09T13:59:57Z"
        }""";

    private static final int retryCount = 3;
    private static final RetryConfig config = RetryConfig.<WebClientResponseException>custom()
        .maxAttempts(retryCount)
        .waitDuration(Duration.ofSeconds(3))
        .retryOnException(e -> e instanceof WebClientResponseException)
        .build();
    private static final Retry retry = Retry.of("TestRetry", config);
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
        GitHubClient gitHubClient = new GitHubClient(webClient, retry);

        RepositoryResponse response = gitHubClient.fetchRepository("onevoker", "LinkTrackerBot");
        long expectedId = 755139175L;
        OffsetDateTime expectedUpdatedAt = OffsetDateTime.parse("2024-02-09T13:59:57Z");

        assertAll(
            () -> assertThat(response.getId()).isEqualTo(expectedId),
            () -> assertThat(response.getPushedAt()).isEqualTo(expectedUpdatedAt)
        );
    }
}
