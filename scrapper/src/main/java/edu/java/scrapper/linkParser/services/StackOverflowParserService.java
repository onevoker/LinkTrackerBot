package edu.java.scrapper.linkParser.services;

import edu.java.scrapper.configuration.clientsConfig.ClientsConfig;
import edu.java.scrapper.linkParser.dto.StackOverflowLinkQuestionData;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StackOverflowParserService implements LinkParserService {
    private final ClientsConfig.StackOverflow stackOverflow;

    @Override
    public StackOverflowLinkQuestionData getLinkData(URI url) {
        return new StackOverflowLinkQuestionData(getQuestionId(url));
    }

    private long getQuestionId(URI url) {
        String res = urlMatcher(url, stackOverflow.regexps().regexpForQuestionId());
        return res.isEmpty() ? 0L : Long.parseLong(res);
    }

}
