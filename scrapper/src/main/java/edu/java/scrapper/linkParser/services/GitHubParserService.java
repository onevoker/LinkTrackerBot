package edu.java.scrapper.linkParser.services;

import edu.java.scrapper.configuration.resourcesConfig.ClientsConfig;
import edu.java.scrapper.linkParser.dto.GitHubLinkData;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GitHubParserService implements LinkParserService {
    private final ClientsConfig.GitHub gitHub;

    @Override
    public GitHubLinkData getLinkData(URI url) {
        return new GitHubLinkData(getGitHubOwner(url), getGitHubRepo(url));

    }

    private String getGitHubOwner(URI url) {
        return urlMatcher(url, gitHub.regexps().regexpForGitHubOwner());
    }

    private String getGitHubRepo(URI url) {
        return urlMatcher(url, gitHub.regexps().regexpForGitHubRepo());
    }
}
