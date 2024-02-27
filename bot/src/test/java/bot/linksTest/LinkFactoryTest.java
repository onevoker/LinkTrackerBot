package bot.linksTest;

import edu.java.bot.exceptions.InvalidLinkException;
import edu.java.bot.links.Link;
import edu.java.bot.links.LinkFactory;
import edu.java.bot.links.LinkValidatorService;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class LinkFactoryTest {
    private static final List<String> supportedDomains = List.of(
        "github.com",
        "stackoverflow.com"
    );
    private static final Long userId = 1L;
    private static LinkFactory linkFactory;

    @BeforeAll
    public static void setUp() {
        LinkValidatorService linkValidatorService = new LinkValidatorService(supportedDomains);
        linkFactory = new LinkFactory(linkValidatorService);
    }

    public static Stream<Arguments> getCorrectUri() {
        return Stream.of(
            Arguments.of("https://github.com/onevoker"),
            Arguments.of("https://github.com/onevoker/LinkTrackerBot"),
            Arguments.of("https://github.com/iskanred/iu-devops-course"),
            Arguments.of("https://github.com/onevoker")
        );
    }

    public static Stream<Arguments> getUncorrectUri() {
        return Stream.of(
            Arguments.of("https://open.spotify.com"),
            Arguments.of("https://www.youtube.com/"),
            Arguments.of("https://github.com/32149085901951"),
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
        Link link = linkFactory.createLink(userId, strLink);
        assertThat(link.stringLink()).isEqualTo(strLink);
    }

    @ParameterizedTest
    @MethodSource("getUncorrectUri")
    void testUnCorrectUri(String strLink) {
        var exception = assertThrows(InvalidLinkException.class, () -> linkFactory.createLink(userId, strLink));
        assertThat(exception.getMessage()).isEqualTo("Invalid link");
    }
}
