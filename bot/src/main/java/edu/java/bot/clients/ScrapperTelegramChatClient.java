package edu.java.bot.clients;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@RequiredArgsConstructor
public class ScrapperTelegramChatClient {
    private static final String TELEGRAM_CHAT_ENDPOINT_PATH = "/tg-chat/";
    private final WebClient scrapperWebClient;

    public Void registerChat(int id) {
        return scrapperWebClient.post()
            .uri(TELEGRAM_CHAT_ENDPOINT_PATH + id)
            .retrieve()
            .bodyToMono(Void.class)
            .block();
    }

    public Void deleteChat(int id) {
        return scrapperWebClient.delete()
            .uri(TELEGRAM_CHAT_ENDPOINT_PATH + id)
            .retrieve()
            .bodyToMono(Void.class)
            .block();
    }
}
