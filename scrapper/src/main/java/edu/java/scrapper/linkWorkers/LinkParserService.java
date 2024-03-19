package edu.java.scrapper.linkWorkers;

import edu.java.scrapper.linkWorkers.dto.GitHubLinkRepoData;
import edu.java.scrapper.linkWorkers.dto.StackOverflowLinkQuestionData;
import java.net.URI;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.stereotype.Service;

@Service
public class LinkParserService {
    private static final String REGEX_FOR_OWNER = "https://github\\.com/(.*?)/";
    private static final String REGEX_FOR_REPO = "https://github\\.com/.*?/(.*)";
    private static final String REGEX_FOR_QUESTION_ID = "https://stackoverflow\\.com/questions/(\\d+)/([\\w-]+)";

    public GitHubLinkRepoData getGitHubLinkRepoData(URI url) {
        return new GitHubLinkRepoData(getGitHubOwner(url), getGitHubRepo(url));
    }

    public StackOverflowLinkQuestionData getStackOverflowLinkQuestionData(URI url) {
        return new StackOverflowLinkQuestionData(getQuestionId(url));
    }

    private String getGitHubOwner(URI url) {
        return urlMatcher(url, REGEX_FOR_OWNER);
    }

    private String getGitHubRepo(URI url) {
        return urlMatcher(url, REGEX_FOR_REPO);
    }

    private long getQuestionId(URI url) {
        String res = urlMatcher(url, REGEX_FOR_QUESTION_ID);
        return res.isEmpty() ? 0L : Long.parseLong(res);
    }

    private String urlMatcher(URI url, String regex) {
        String link = url.toString();
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(link);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return "";
    }
}
