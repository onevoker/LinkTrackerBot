package edu.java.scrapper.configuration.updateSenderConfig.http;

import edu.java.scrapper.senderUpdates.UpdateSender;
import edu.java.scrapper.senderUpdates.http.BotClient;
import io.github.resilience4j.retry.Retry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@ConditionalOnProperty(prefix = "app", name = "use-queue", havingValue = "false")
public class HttpConfig {
    @Autowired
    private WebClient botWebClient;
    @Autowired
    private Retry retry;

    @Bean
    public UpdateSender botClient() {
        return new BotClient(botWebClient, retry);
    }
}
