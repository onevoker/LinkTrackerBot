package bot.liksTest;

import edu.java.bot.links.Link;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class LinkTest {
    public static Stream<Arguments> getCorrectUri() {
        return Stream.of(
            Arguments.of("https://github.com/onevoker"),
            Arguments.of("https://github.com/onevoker/LinkTrackerBot"),
            Arguments.of("https://stackoverflow.com/"),
            Arguments.of("https://stackoverflow.com/help"),
            Arguments.of("https://github.com/iskanred")
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
    void testCorrectUri(String link) {
        boolean isCorrect = Link.isCorrectUri(link);
        assertThat(isCorrect).isTrue();
    }

    @ParameterizedTest
    @MethodSource("getUncorrectUri")
    void testUnCorrectUri(String link) {
        boolean isCorrect = Link.isCorrectUri(link);
        assertThat(isCorrect).isFalse();
    }
}
