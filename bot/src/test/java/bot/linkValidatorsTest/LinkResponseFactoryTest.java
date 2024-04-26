package bot.linkValidatorsTest;

import edu.java.bot.configuration.ApplicationConfig;
import edu.java.bot.dto.response.LinkResponse;
import edu.java.bot.exceptions.InvalidLinkException;
import edu.java.bot.linkValidators.LinkResponseFactory;
import edu.java.bot.linkValidators.LinkResponseValidatorService;
import edu.java.bot.retry.BackOfType;
import java.net.URI;
import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.Assert.assertThrows;

public class LinkResponseFactoryTest {
    private static final ApplicationConfig applicationConfig = new ApplicationConfig(
        null,
        null,
        List.of("https://github\\.com/[^/]+/[^/]+/?", "https://stackoverflow\\.com/questions/\\d+/[^/]+/?"),
        Duration.ofSeconds(15),
        new ApplicationConfig.RetrySettings(BackOfType.CONSTANT, 3, Duration.ofSeconds(3), Collections.emptySet()),
        null,
        new ApplicationConfig.Kafka(
            "updates",
            "bot",
            "localhost:9092",
            "edu.java.scrapper.dto.response.LinkUpdateResponse:edu.java.bot.dto.response.LinkUpdateResponse",
            "badResponse"
        )
    );
    private static final int USER_ID = 1;
    private static LinkResponseFactory linkFactory;

    @BeforeAll
    public static void setUp() {
        LinkResponseValidatorService linkValidatorService = new LinkResponseValidatorService(applicationConfig);
        linkFactory = new LinkResponseFactory(linkValidatorService);
    }

    public static Stream<Arguments> getCorrectUri() {
        return Stream.of(
            Arguments.of("https://github.com/onevoker/Tkf"),
            Arguments.of("https://github.com/onevoker/LinkTrackerBot"),
            Arguments.of("https://github.com/iskanred/iu-devops-course")
        );
    }

    public static Stream<Arguments> getUncorrectUri() {
        return Stream.of(
            Arguments.of("https://open.spotify.com"),
            Arguments.of("https://www.youtube.com/"),
            Arguments.of("https://translate.yandex.ru/"),
            Arguments.of("link"),
            Arguments.of("14032005"),
            Arguments.of("github"),
            Arguments.of("github.com")
        );
    }

    @ParameterizedTest
    @MethodSource("getCorrectUri")
    void testCorrectUri(String strLink) {
        URI url = URI.create(strLink);
        LinkResponse link = linkFactory.createLink(USER_ID, url);
        assertThat(link.url().toString()).isEqualTo(strLink);
    }

    @ParameterizedTest
    @MethodSource("getUncorrectUri")
    void testUnCorrectUri(String strLink) {
        URI url = URI.create(strLink);
        var exception = assertThrows(InvalidLinkException.class, () -> linkFactory.createLink(USER_ID, url));
        assertThat(exception.getMessage()).isEqualTo("Вы указали неправильную ссылку, возможно вам поможет /help");
    }
}
