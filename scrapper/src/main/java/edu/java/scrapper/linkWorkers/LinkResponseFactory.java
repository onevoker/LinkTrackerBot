package edu.java.scrapper.linkWorkers;

import edu.java.scrapper.controllers.exceptions.InvalidLinkResponseException;
import edu.java.scrapper.dto.response.LinkResponse;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LinkResponseFactory {
    private final LinkResponseValidatorService linkResponseValidatorService;

    public LinkResponse createLink(long chatId, URI url) {
        boolean isCorrect = linkResponseValidatorService.isCorrectUri(url);
        if (isCorrect) {
            URI normalizedUrl = normalizeUrl(url.toString());
            return new LinkResponse(chatId, normalizedUrl);
        }
        throw new InvalidLinkResponseException("Вы указали неправильную ссылку, возможно вам поможет /help");
    }

    public URI normalizeUrl(String link) {
        String normalizedUrl = link.replaceAll("/+$", "");
        return URI.create(normalizedUrl);
    }
}
