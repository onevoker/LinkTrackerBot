package edu.java.bot.clients;

import edu.java.bot.dto.response.ApiErrorResponse;
import edu.java.bot.exceptions.ApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@RequiredArgsConstructor
public class ScrapperTelegramChatClient {
    private static final String TELEGRAM_CHAT_ENDPOINT_PATH = "/tg-chat/";
    private final WebClient scrapperWebClient;

    public void registerChat(long id) {
        scrapperWebClient.post()
            .uri(TELEGRAM_CHAT_ENDPOINT_PATH + id)
            .retrieve()
            .onStatus(
                HttpStatus.CONFLICT::equals,
                response -> response.bodyToMono(ApiErrorResponse.class).map(ApiException::new)
            )
            .bodyToMono(Void.class)
            .block();
    }

    public void unregisterChat(long id) {
        scrapperWebClient.delete()
            .uri(TELEGRAM_CHAT_ENDPOINT_PATH + id)
            .retrieve()
            .onStatus(
                HttpStatus.NOT_FOUND::equals,
                response -> response.bodyToMono(ApiErrorResponse.class).map(ApiException::new)
            )
            .bodyToMono(Void.class)
            .block();
    }
}
