package edu.java.scrapper.linkWorkersTest;

import edu.java.scrapper.linkWorkers.LinkParserUtil;
import java.net.URI;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class LinkParserUtilTest {
    private static final String preparedRepo = "https://github.com/%s/%s";
    private static final String preparedQuestion =
        "https://stackoverflow.com/questions/%s/python-functional-method-of-checking-that-all-elements-in-a-list-are-equal";

    @Test
    void testParseGitHub() {
        String owner = "onevoker";
        String repo = "LinkTrackerBot";
        URI url = URI.create(preparedRepo.formatted(owner, repo));
        String parsedOwner = LinkParserUtil.getGitHubOwner(url);
        String parsedRepo = LinkParserUtil.getGitHubRepo(url);

        assertAll(
            () -> assertThat(parsedOwner).isEqualTo(owner),
            () -> assertThat(parsedRepo).isEqualTo(repo)
        );
    }

    @Test
    void testGetQuestionId() {
        long questionId = 78013649;
        URI url = URI.create(preparedQuestion.formatted(questionId));
        Long parsedQuestionId = LinkParserUtil.getQuestionId(url);
        assertThat(parsedQuestionId).isEqualTo(questionId);
    }
}
