package edu.java.bot.commandServices;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.exceptions.InvalidLinkException;
import edu.java.bot.links.Link;
import edu.java.bot.links.LinkFactory;
import edu.java.bot.repositories.LinkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TrackCommandService implements CommandService {
    private final LinkRepository linkRepository;
    private final LinkFactory linkFactory;
    private static final int BEGIN_LINK_INDEX = 7;
    private static final String COMMAND = "/track";
    private static final String DESCRIPTION = "Отслеживание ссылки";
    private static final String HANDLE_TEXT = "Начали отслеживать данную ссылку";
    private static final String ALREADY_LINKED_TEXT = "Ссылка уже добавлена, для просмотра ссылок введите /list";
    private static final String INCORRECT_LINK_TEXT = "Вы указали неправильную ссылку, возможно вам поможет /help";
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
        User user = message.from();
        long chatId = message.chat().id();
        String answerText = getAnswerText(user, message);

        return new SendMessage(chatId, answerText);
    }

    private String getAnswerText(User user, Message message) {
        Long userID = user.id();
        String answerText;
        try {
            String strLink = message.text().substring(BEGIN_LINK_INDEX);
            try {
                Link link = linkFactory.createLink(userID, strLink);
                boolean isInUserLinks = linkRepository.isInUserLinks(link);
                if (isInUserLinks) {
                    answerText = ALREADY_LINKED_TEXT;
                } else {
                    linkRepository.addUserLink(link);
                    answerText = HANDLE_TEXT;
                }
            } catch (InvalidLinkException exception) {
                answerText = INCORRECT_LINK_TEXT;
            }
        } catch (IndexOutOfBoundsException exception) {
            answerText = NO_LINK_TEXT;
        }

        return answerText;
    }
}