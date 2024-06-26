package edu.java.bot.commandServices;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.clients.ScrapperLinkClient;
import edu.java.bot.exceptions.ApiException;
import edu.java.bot.exceptions.InvalidLinkException;
import edu.java.bot.linkValidators.LinkResponseFactory;
import edu.java.dto.request.RemoveLinkRequest;
import edu.java.dto.response.LinkResponse;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UntrackCommandService implements CommandService {
    private final ScrapperLinkClient linkClient;
    private final LinkResponseFactory linkResponseFactory;
    private static final int BEGIN_LINK_INDEX = 9;
    private static final String COMMAND = "/untrack";
    private static final String DESCRIPTION = "Прекращение отслеживания ссылки";
    private static final String HANDLE_TEXT = "Прекратили отслеживание данной ссылки: ";
    private static final String INCORRECT_LINK_TEXT = "Вы указали неправильную ссылку, возможно вам поможет /help";
    private static final String NO_LINK_TEXT = "Укажите что перестать отслеживать. Пример /untrack ,,ваша_ссылка,,";

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

        return getAnswerText(message)
            .map(answerText -> new SendMessage(chatId, answerText));
    }

    private Mono<String> getAnswerText(Message message) {
        long chatId = message.chat().id();
        Mono<String> answerText;
        try {
            String link = message.text().substring(BEGIN_LINK_INDEX);
            try {
                URI url = URI.create(link);
                LinkResponse response = linkResponseFactory.createLink(chatId, url);
                RemoveLinkRequest removeLinkRequest = new RemoveLinkRequest(response.url());

                answerText = linkClient.untrackLink(chatId, removeLinkRequest)
                    .map(linkResponse -> HANDLE_TEXT + linkResponse.url())
                    .onErrorResume(ApiException.class, exception -> Mono.just(exception.getMessage()));
            } catch (IllegalArgumentException exception) {
                answerText = Mono.just(INCORRECT_LINK_TEXT);
            } catch (InvalidLinkException exception) {
                answerText = Mono.just(exception.getMessage());
            }
        } catch (IndexOutOfBoundsException exception) {
            answerText = Mono.just(NO_LINK_TEXT);
        }

        return answerText;
    }
}
