package edu.java.bot.linkValidators;

import edu.java.bot.dto.response.LinkResponse;
import edu.java.bot.exceptions.InvalidLinkException;
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
        throw new InvalidLinkException("Вы указали неправильную ссылку, возможно вам поможет /help");
    }

    private URI normalizeUrl(String link) {
        String normalizedUrl = link.replaceAll("/+$", "");
        return URI.create(normalizedUrl);
    }
}
