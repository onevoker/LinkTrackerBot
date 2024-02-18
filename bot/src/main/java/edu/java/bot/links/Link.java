package edu.java.bot.links;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Objects;
import lombok.SneakyThrows;

public record Link(Long userId, String stringLink) {
    private static final int AVAILABLE_RESPONSE_CODE = 200;
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
        try (HttpClient client = HttpClient.newHttpClient()) {
            HttpRequest request = HttpRequest.newBuilder(new URI(link)).build();
            HttpResponse<Void> response = client.send(request, HttpResponse.BodyHandlers.discarding());
            int statusCode = response.statusCode();

            if (statusCode != AVAILABLE_RESPONSE_CODE) {
                return false;
            }
            return isValidResource(link);
        } catch (IOException | InterruptedException | URISyntaxException | IllegalArgumentException e) {
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

    /**
     * Here used @SneakyThrows, because in constructor we checked is Uri correct
     */
    @SneakyThrows
    public URI getUriLink() {
        return new URI(stringLink);
    }
}
