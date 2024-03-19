package edu.java.scrapper.linkWorkersTest;

import edu.java.scrapper.linkWorkers.LinkParserService;
import edu.java.scrapper.linkWorkers.dto.GitHubLinkRepoData;
import java.net.URI;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest
public class LinkParserUtilTest {
    @Autowired
    private LinkParserService linkParserService;
    private static final String preparedRepo = "https://github.com/%s/%s";
    private static final String preparedQuestion =
        "https://stackoverflow.com/questions/%s/python-functional-method-of-checking-that-all-elements-in-a-list-are-equal";

    @Test
    void testParseGitHub() {
        String owner = "onevoker";
        String repo = "LinkTrackerBot";
        URI url = URI.create(preparedRepo.formatted(owner, repo));
        GitHubLinkRepoData linkRepoData = linkParserService.getGitHubLinkRepoData(url);
        String parsedOwner = linkRepoData.owner();
        String parsedRepo = linkRepoData.repo();

        assertAll(
            () -> assertThat(parsedOwner).isEqualTo(owner),
            () -> assertThat(parsedRepo).isEqualTo(repo)
        );
    }

    @Test
    void testGetQuestionId() {
        long questionId = 78013649;
        URI url = URI.create(preparedQuestion.formatted(questionId));
        Long parsedQuestionId = linkParserService.getStackOverflowLinkQuestionData(url).questionId();
        assertThat(parsedQuestionId).isEqualTo(questionId);
    }
}
