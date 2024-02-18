package edu.java.bot.links;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public record Link(Long userId, String stringLink) {
    private final static Logger LOGGER = LogManager.getLogger();

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
            LOGGER.debug("Проверяем ссылку...");
            RestTemplate restTemplate = new RestTemplate();
            LOGGER.debug("Создаем запрос");
            ResponseEntity<String> response = restTemplate.getForEntity(link, String.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                LOGGER.debug("Статус код корректный");
                return isValidResource(link);
            }
            LOGGER.debug("Статус код не корректный");
            return false;
        } catch (Exception e) {
            LOGGER.debug("Сработал catch, ошибка URI");
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
