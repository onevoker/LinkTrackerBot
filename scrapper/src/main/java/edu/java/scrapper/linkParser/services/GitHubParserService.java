package edu.java.scrapper.linkParser.services;

import edu.java.scrapper.configuration.ApplicationConfig;
import edu.java.scrapper.linkParser.dto.GitHubLinkData;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GitHubParserService implements LinkParserService {
    private final ApplicationConfig.GitHubRegexp gitHubRegexp;

    @Override
    public GitHubLinkData getLinkData(URI url) {
        return new GitHubLinkData(getGitHubOwner(url), getGitHubRepo(url));

    }

    private String getGitHubOwner(URI url) {
        return urlMatcher(url, gitHubRegexp.regexpForGitHubOwner());
    }

    private String getGitHubRepo(URI url) {
        return urlMatcher(url, gitHubRegexp.regexpForGitHubRepo());
    }
}
