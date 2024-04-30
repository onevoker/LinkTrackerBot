package bot.retryTest;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import edu.java.bot.configuration.ApplicationConfig;
import edu.java.bot.configuration.RetryConfig;
import edu.java.bot.retry.BackOffType;
import edu.java.bot.retry.retryes.LinearRetry;
import io.github.resilience4j.reactor.retry.RetryOperator;
import io.github.resilience4j.retry.Retry;
import java.time.Duration;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
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
import static org.springframework.http.HttpStatus.BAD_GATEWAY;
import static org.springframework.http.HttpStatus.GATEWAY_TIMEOUT;
import static org.springframework.http.HttpStatus.INSUFFICIENT_STORAGE;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.SERVICE_UNAVAILABLE;
import static org.springframework.http.HttpStatus.TOO_MANY_REQUESTS;

@WireMockTest(httpPort = 1212)
public class LinearRetryTest {
    private static final String WIRE_MOCK_URL = "http://localhost:1212";
    private static final String ENDPOINT = "/test";
    private static final int RETRY_COUNT = 3;
    private static final int STEP = 2;
    private static final Set<HttpStatus> HTTP_STATUSES = Set.of(
        INTERNAL_SERVER_ERROR,
        TOO_MANY_REQUESTS,
        BAD_GATEWAY,
        INSUFFICIENT_STORAGE,
        SERVICE_UNAVAILABLE,
        GATEWAY_TIMEOUT
    );
    private final ApplicationConfig.RetrySettings retrySettings =
        new ApplicationConfig.RetrySettings(
            BackOffType.LINEAR,
            RETRY_COUNT,
            Duration.ofSeconds(STEP),
            HTTP_STATUSES
        );
    private final RetryConfig retryConfig = new RetryConfig(retrySettings, List.of(new LinearRetry()));
    private final Retry constantRetry = retryConfig.retry();
    private WebClient webClient;

    @BeforeEach
    void setUp() {
        webClient = WebClient.builder().baseUrl(WIRE_MOCK_URL).build();
    }

    @ParameterizedTest
    @MethodSource("httpStatusCodes")
    void testRetry(int httpStatusCode) {
        stubFor(
            get(urlPathMatching(ENDPOINT))
                .willReturn(aResponse()
                    .withStatus(httpStatusCode)
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

    private static Stream<Arguments> httpStatusCodes() {
        return Stream.of(
            Arguments.of(INTERNAL_SERVER_ERROR.value()),
            Arguments.of(TOO_MANY_REQUESTS.value()),
            Arguments.of(BAD_GATEWAY.value()),
            Arguments.of(INSUFFICIENT_STORAGE.value()),
            Arguments.of(SERVICE_UNAVAILABLE.value()),
            Arguments.of(GATEWAY_TIMEOUT.value())
        );
    }
}
