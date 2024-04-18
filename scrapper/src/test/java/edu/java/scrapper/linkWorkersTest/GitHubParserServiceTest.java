package edu.java.scrapper.linkWorkersTest;

import edu.java.scrapper.configuration.resourcesConfig.ClientsConfig;
import edu.java.scrapper.linkParser.dto.GitHubLinkData;
import edu.java.scrapper.linkParser.services.GitHubParserService;
import java.net.URI;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class GitHubParserServiceTest {
    private static final ClientsConfig.GitHub gitHub = new ClientsConfig.GitHub(
        null,
        0,
        null,
        new ClientsConfig.GitHubRegexps(
            "https://github\\.com/(.*?)/",
            "https://github\\.com/.*?/(.*)"
        ),
        null
    );
    private final GitHubParserService gitHubParserService = new GitHubParserService(gitHub);
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
