package edu.java.bot.commandServices;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.clients.ScrapperLinkClient;
import edu.java.bot.exceptions.ApiException;
import edu.java.dto.response.LinkResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ListCommandService implements CommandService {
    private final ScrapperLinkClient linkClient;
    private static final String COMMAND = "/list";
    private static final String DESCRIPTION = "Список ссылок";
    private static final String HANDLE_TEXT = "Список ваших отслеживаемых ссылок:\n";
    private static final String NOT_LINKED_MESSAGE = "Вы не отслеживаете ни одной ссылки(((";

    @Override
    public String command() {
        return COMMAND;
    }

    @Override
    public String description() {
        return DESCRIPTION;
    }

    @Override
    public Mono<SendMessage> handle(Update update) {
        Message message = update.message();
        long chatId = message.chat().id();

        return linkClient.getTrackedLinks(chatId)
            .flatMap(userLinks -> {
                    List<LinkResponse> linkResponseList = userLinks.links();
                    if (linkResponseList == null || linkResponseList.isEmpty()) {
                        return Mono.just(new SendMessage(chatId, NOT_LINKED_MESSAGE));
                    }
                    String text = HANDLE_TEXT + getLinksListText(linkResponseList);
                    return Mono.just(new SendMessage(chatId, text));
                }
            )
            .onErrorResume(ApiException.class, exception -> Mono.just(new SendMessage(chatId, exception.getMessage())));
    }

    private String getLinksListText(List<LinkResponse> linkResponseList) {
        StringBuilder text = new StringBuilder();
        int i = 0;

        for (LinkResponse link : linkResponseList) {
            text.append("%s. ".formatted(++i)).append(link.url().toString()).append("\n");
        }

        return text.toString();
    }
}
