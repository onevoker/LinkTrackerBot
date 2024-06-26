package edu.java.scrapper.clientTest;

import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import edu.java.scrapper.clients.StackOverflowClient;
import edu.java.scrapper.dto.stackOverflowDto.Item;
import edu.java.scrapper.dto.stackOverflowDto.QuestionResponse;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryConfig;
import java.time.Duration;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
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

@WireMockTest(httpPort = 8800)
@ExtendWith(WireMockExtension.class)
public class StackOverFlowClientTest {

    static final String WIRE_MOCK_URL = "http://localhost:8800/2.3/questions/";
    private static final long QUESTION_ID = 61746598L;
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
    public void testStackOverFlowClient() {
        stubFor(
            get(urlPathMatching("/2.3/questions/" + QUESTION_ID))
                .willReturn(aResponse()
                    .withStatus(200)
                    .withHeader("Content-Type", "application/json")
                    .withBody(BODY))
        );
        StackOverflowClient stackOverflowClient = new StackOverflowClient(webClient, retry);

        QuestionResponse response = stackOverflowClient.fetchQuestion(QUESTION_ID);
        List<Item> actualItems = response.items();
        boolean expectedIsAnswered = false;
        long expectedQuestionId = 61746598L;
        long expectedAnswerCount = 1L;
        OffsetDateTime expectedLastEditDate = Instant.ofEpochSecond(1610103022).atOffset(ZoneOffset.UTC);
        Item expectedItem = new Item(expectedIsAnswered, expectedQuestionId, expectedAnswerCount, expectedLastEditDate);

        assertThat(actualItems.getFirst()).isEqualTo(expectedItem);
    }
}
