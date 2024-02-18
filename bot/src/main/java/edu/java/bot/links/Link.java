package edu.java.bot.links;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;
import java.util.logging.Level;
import lombok.extern.java.Log;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

@Log
public record Link(Long userId, String stringLink) {
    private static final String GITHUB_DOMAIN = "github.com";
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
            log.log(Level.INFO, "Проверяем ссылку...");
            RestTemplate restTemplate = new RestTemplate();
            log.log(Level.INFO, "Создаем запрос");
            ResponseEntity<String> response = restTemplate.getForEntity(link, String.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                log.log(Level.INFO, "Статус код корректный");
                return isValidResource(link);
            }
            log.log(Level.INFO, "Статус код не корректный");
            return false;
        } catch (Exception e) {
            log.log(Level.INFO, "Сработал catch " + e.getMessage());
            return false;
        }
    }

    private boolean isValidResource(String link) {
        try {
            URI uri = new URI(link);
            return Objects.equals(uri.getHost(), GITHUB_DOMAIN) || Objects.equals(uri.getHost(), STACK_OVERFLOW_DOMAIN);
        } catch (URISyntaxException e) {
            return false;
        }
    }

    private String normalizeLink(String link) {
        return link.replaceAll("/+$", "");
    }
}
