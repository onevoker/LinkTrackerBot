package edu.java.scrapper.linkWorkers;

import java.net.URI;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LinkParserUtil {
    private static final String REGEX_FOR_OWNER = "https://github\\.com/(.*?)/";
    private static final String REGEX_FOR_REPO = "https://github\\.com/.*?/(.*)";
    private static final String REGEX_FOR_QUESTION_ID = "https://stackoverflow\\.com/questions/(\\d+)/([\\w-]+)";

    private LinkParserUtil() {
    }

    public static String getGitHubOwner(URI url) {
        return urlMatcher(url, REGEX_FOR_OWNER);
    }

    public static String getGitHubRepo(URI url) {
        return urlMatcher(url, REGEX_FOR_REPO);
    }

    public static long getQuestionId(URI url) {
        String res = urlMatcher(url, REGEX_FOR_QUESTION_ID);
        return res.isEmpty() ? 0L : Long.parseLong(res);
    }

    private static String urlMatcher(URI url, String regex) {
        String link = url.toString();
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(link);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return "";
    }
}
