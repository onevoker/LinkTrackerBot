package edu.java.scrapper.retryTest;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import edu.java.scrapper.configuration.resourcesConfig.ResourcesConfig;
import edu.java.scrapper.retry.BackOffType;
import edu.java.scrapper.retry.RetryFactory;
import edu.java.scrapper.retry.retries.ConstantRetry;
import io.github.resilience4j.reactor.retry.RetryOperator;
import io.github.resilience4j.retry.Retry;
import java.time.Duration;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathMatching;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;
import static org.junit.Assert.assertThrows;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.TOO_MANY_REQUESTS;

@WireMockTest(httpPort = 1212)
public class ConstantRetryTest {
    private static final String WIRE_MOCK_URL = "http://localhost:1212";
    private static final String ENDPOINT = "/test";
    private static final int INTERNAL_SERVER_ERROR_STATUS = INTERNAL_SERVER_ERROR.value();
    private static final int RETRY_COUNT = 3;
    private static final int STEP = 2;
    private static final Set<HttpStatus> HTTP_STATUSES = Set.of(
        INTERNAL_SERVER_ERROR,
        TOO_MANY_REQUESTS
    );
    private final ResourcesConfig.RetrySettings retrySettings =
        new ResourcesConfig.RetrySettings(
            BackOffType.CONSTANT,
            RETRY_COUNT,
            Duration.ofSeconds(STEP),
            HTTP_STATUSES
        );
    private final RetryFactory retryFactory = new RetryFactory(List.of(new ConstantRetry()));
    private final Retry constantRetry = retryFactory.createRetry(retrySettings);
    private WebClient webClient;

    @BeforeEach
    void setUp() {
        webClient = WebClient.builder().baseUrl(WIRE_MOCK_URL).build();
    }

    @Test
    void testRetry() {
        stubFor(
            get(urlPathMatching(ENDPOINT))
                .willReturn(aResponse()
                    .withStatus(INTERNAL_SERVER_ERROR_STATUS)
                )
        );

        assertThrows(
            WebClientResponseException.class,
            () -> webClient.get()
                .uri(ENDPOINT)
                .retrieve()
                .bodyToMono(Void.class)
                .transformDeferred(RetryOperator.of(constantRetry))
                .block()
        );

        verify(RETRY_COUNT, getRequestedFor(urlEqualTo(ENDPOINT)));
    }
}
