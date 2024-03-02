package edu.java.scrapper.clients;

import edu.java.scrapper.dto.request.LinkUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@RequiredArgsConstructor
public class BotClient {
    private final WebClient botWebClient;
    private static final String UPDATE_ENDPOINT = "/updates";

    public void sendUpdate(LinkUpdateRequest updateRequest) {
        botWebClient.post()
            .uri(UPDATE_ENDPOINT)
            .body(BodyInserters.fromValue(updateRequest))
            .retrieve()
            .bodyToMono(Void.class)
            .block();
    }
}
