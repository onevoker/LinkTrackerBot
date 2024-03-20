package edu.java.scrapper.linkWorkersTest;

import edu.java.scrapper.linkParser.dto.GitHubLinkData;
import edu.java.scrapper.linkParser.services.GitHubParserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.net.URI;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest
public class GitHubParserServiceTest {
    @Autowired
    private GitHubParserService gitHubParserService;
    private static final String preparedRepo = "https://github.com/%s/%s";
    @Test
    void testParseGitHub() {
        String owner = "onevoker";
        String repo = "LinkTrackerBot";
        URI url = URI.create(preparedRepo.formatted(owner, repo));
        GitHubLinkData linkRepoData = gitHubParserService.getLinkData(url);
        String parsedOwner = linkRepoData.owner();
        String parsedRepo = linkRepoData.repo();

        assertAll(
            () -> assertThat(parsedOwner).isEqualTo(owner),
            () -> assertThat(parsedRepo).isEqualTo(repo)
        );
    }
}
