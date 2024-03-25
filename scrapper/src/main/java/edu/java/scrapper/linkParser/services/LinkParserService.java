package edu.java.scrapper.linkParser.services;

import edu.java.scrapper.linkParser.dto.LinkData;
import java.net.URI;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public interface LinkParserService {
    LinkData getLinkData(URI url);

    default String urlMatcher(URI url, String regex) {
        String link = url.toString();
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(link);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return "";
    }
}
