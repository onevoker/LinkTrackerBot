package edu.java.scrapper.linkWorkersTest;

import edu.java.scrapper.controllers.exceptions.InvalidLinkResponseException;
import edu.java.scrapper.dto.response.LinkResponse;
import edu.java.scrapper.linkWorkers.LinkResponseFactory;
import edu.java.scrapper.linkWorkers.LinkResponseValidatorService;
import java.net.URI;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class LinkResponseFactoryTest {
    private static final int USER_ID = 1;
    private static LinkResponseFactory linkFactory;

    @BeforeAll
    public static void setUp() {
        LinkResponseValidatorService linkValidatorService = new LinkResponseValidatorService();
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
        var exception = assertThrows(InvalidLinkResponseException.class, () -> linkFactory.createLink(USER_ID, url));
        assertThat(exception.getMessage()).isEqualTo("Вы указали неправильную ссылку, возможно вам поможет /help");
    }
}
