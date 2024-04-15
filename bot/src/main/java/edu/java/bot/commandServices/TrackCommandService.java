package edu.java.bot.commandServices;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.clients.ScrapperLinkClient;
import edu.java.bot.dto.request.AddLinkRequest;
import edu.java.bot.dto.response.LinkResponse;
import edu.java.bot.exceptions.ApiException;
import edu.java.bot.exceptions.InvalidLinkException;
import edu.java.bot.linkValidators.LinkResponseFactory;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class TrackCommandService implements CommandService {
    private final ScrapperLinkClient linkClient;
    private final LinkResponseFactory linkResponseFactory;
    private static final int BEGIN_LINK_INDEX = 7;
    private static final String COMMAND = "/track";
    private static final String DESCRIPTION = "Отслеживание ссылки";
    private static final String HANDLE_TEXT = "Начали отслеживать данную ссылку: ";
    private static final String INVALID_LINK_TEXT = "Вы указали неправильную ссылку, возможно вам поможет /help";
    private static final String NO_LINK_TEXT = "Введите ссылку для отслеживания. Пример ввода: /track ,,ваша_ссылка,,";

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
                AddLinkRequest addLinkRequest = new AddLinkRequest(response.url());

                answerText = linkClient.trackLink(chatId, addLinkRequest)
                    .map(linkResponse -> HANDLE_TEXT + linkResponse.url())
                    .onErrorResume(ApiException.class, exception -> Mono.just(exception.getMessage()));
            } catch (IllegalArgumentException exception) {
                answerText = Mono.just(INVALID_LINK_TEXT);
            } catch (InvalidLinkException exception) {
                answerText = Mono.just(exception.getMessage());
            }
        } catch (IndexOutOfBoundsException exception) {
            answerText = Mono.just(NO_LINK_TEXT);
        }

        return answerText;
    }
}
