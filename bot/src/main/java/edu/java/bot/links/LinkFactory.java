package edu.java.bot.links;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LinkFactory {
    private final LinkValidatorService linkValidatorService;

    public Link createLink(Long userId, String stringLink) {
        return new Link(userId, normalizeLink(stringLink), linkValidatorService);
    }

    private String normalizeLink(String link) {
        return link.replaceAll("/+$", "");
    }

}
