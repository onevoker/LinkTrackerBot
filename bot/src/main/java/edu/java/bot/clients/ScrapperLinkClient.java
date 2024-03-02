package edu.java.bot.clients;

import edu.java.bot.dto.request.AddLinkRequest;
import edu.java.bot.dto.request.RemoveLinkRequest;
import edu.java.bot.dto.response.LinkResponse;
import edu.java.bot.dto.response.ListLinksResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@RequiredArgsConstructor
public class ScrapperLinkClient {
    private final WebClient scrapperWebClient;
    private static final String LINK_ENDPOINT_PATH = "/tg-chat/";
    private static final String LINK_HEADER = "Tg-Chat-Id";

    public ListLinksResponse getTrackedLinks(int chatId) {
        return scrapperWebClient.get()
            .uri(LINK_ENDPOINT_PATH)
            .header(LINK_HEADER, String.valueOf(chatId))
            .retrieve()
            .bodyToMono(ListLinksResponse.class)
            .block();
    }

    public LinkResponse trackLink(AddLinkRequest addLinkRequest, int chatId) {
        return scrapperWebClient.post()
            .uri(LINK_ENDPOINT_PATH)
            .header(LINK_HEADER, String.valueOf(chatId))
            .body(BodyInserters.fromValue(addLinkRequest))
            .retrieve()
            .bodyToMono(LinkResponse.class)
            .block();
    }

    public LinkResponse untrackLink(int chatId, RemoveLinkRequest removeLinkRequest) {
        return scrapperWebClient.method(HttpMethod.DELETE)
            .uri(LINK_ENDPOINT_PATH)
            .header(LINK_HEADER, String.valueOf(chatId))
            .body(BodyInserters.fromValue(removeLinkRequest))
            .exchangeToMono(clientResponse -> clientResponse.bodyToMono(LinkResponse.class))
            .block();
    }
}
