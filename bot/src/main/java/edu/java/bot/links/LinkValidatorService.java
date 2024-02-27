package edu.java.bot.links;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class LinkValidatorService {
    private final List<String> supportedDomains;

    public boolean isCorrectUri(String link) {
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
            return supportedDomains.stream()
                .anyMatch(domain -> domain.equals(uri.getHost()));
        } catch (URISyntaxException e) {
            return false;
        }
    }
}
