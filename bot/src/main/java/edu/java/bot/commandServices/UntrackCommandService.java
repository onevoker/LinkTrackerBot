package edu.java.bot.commandServices;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.clients.ScrapperLinkClient;
import edu.java.bot.dto.request.RemoveLinkRequest;
import edu.java.bot.dto.response.LinkResponse;
import edu.java.bot.exceptions.ApiException;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UntrackCommandService implements CommandService {
    private final ScrapperLinkClient linkClient;
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
                RemoveLinkRequest removeLinkRequest = new RemoveLinkRequest(url);
                try {
                    LinkResponse linkResponse = linkClient.untrackLink(chatId, removeLinkRequest);
                    answerText = HANDLE_TEXT + linkResponse.url();
                } catch (ApiException exception) {
                    answerText = exception.getMessage();
                }
            } catch (IllegalArgumentException exception) {
                answerText = INCORRECT_LINK_TEXT;
            }
        } catch (IndexOutOfBoundsException exception) {
            answerText = NO_LINK_TEXT;
        }

        return answerText;
    }
}
