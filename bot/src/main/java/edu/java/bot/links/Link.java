package edu.java.bot.links;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public record Link(Long userId, String stringLink) {
    private static final String GIT_HUB_DOMAIN = "github.com";
    private static final String STACK_OVERFLOW_DOMAIN = "stackoverflow.com";

    public Link(Long userId, String stringLink) {
        this.userId = userId;
        if (isCorrectUri(stringLink)) {
            this.stringLink = normalizeLink(stringLink);
        } else {
            throw new InvalidLinkException();
        }
    }

    private boolean isCorrectUri(String link) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.getForEntity(link, String.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                return isValidResource(link);
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isValidResource(String link) {
        try {
            URI uri = new URI(link);
            return Objects.equals(uri.getHost(), GIT_HUB_DOMAIN)
                || Objects.equals(uri.getHost(), STACK_OVERFLOW_DOMAIN);
        } catch (URISyntaxException e) {
            return false;
        }
    }

    private String normalizeLink(String link) {
        return link.replaceAll("/+$", "");
    }
}
