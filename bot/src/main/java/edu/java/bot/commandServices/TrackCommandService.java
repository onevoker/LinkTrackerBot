package edu.java.bot.commandServices;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.clients.ScrapperLinkClient;
import edu.java.bot.dto.request.AddLinkRequest;
import edu.java.bot.dto.response.LinkResponse;
import edu.java.bot.exceptions.ApiException;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TrackCommandService implements CommandService {
    private final ScrapperLinkClient linkClient;
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
    public SendMessage handle(Update update) {
        Message message = update.message();
        long chatId = message.chat().id();
        String answerText = getAnswerText(message);

        return new SendMessage(chatId, answerText);
    }

    private String getAnswerText(Message message) {
        long chatId = message.chat().id();
        String answerText;
        try {
            String link = message.text().substring(BEGIN_LINK_INDEX);
            try {
                URI url = URI.create(link);
                AddLinkRequest addLinkRequest = new AddLinkRequest(url);
                try {
                    LinkResponse linkResponse = linkClient.trackLink(chatId, addLinkRequest);
                    answerText = HANDLE_TEXT + linkResponse.url();
                } catch (ApiException exception) {
                    answerText = exception.getMessage();
                }
            } catch (IllegalArgumentException exception) {
                answerText = INVALID_LINK_TEXT;
            }
        } catch (IndexOutOfBoundsException exception) {
            answerText = NO_LINK_TEXT;
        }

        return answerText;
    }
}
