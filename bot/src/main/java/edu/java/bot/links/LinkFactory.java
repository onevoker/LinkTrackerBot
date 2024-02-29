package edu.java.bot.links;

import edu.java.bot.exceptions.InvalidLinkException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LinkFactory {
    private final LinkValidatorService linkValidatorService;

    public Link createLink(Long userId, String stringLink) {
        boolean isCorrect = linkValidatorService.isCorrectUri(stringLink);
        if (isCorrect) {
            return new Link(userId, normalizeLink(stringLink));
        }
        throw new InvalidLinkException();
    }

    private String normalizeLink(String link) {
        return link.replaceAll("/+$", "");
    }
}
