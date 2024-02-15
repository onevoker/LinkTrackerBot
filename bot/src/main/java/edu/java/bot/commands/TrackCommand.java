package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.links.Link;
import edu.java.bot.links.UserLinks;

public class TrackCommand implements Command {
    private final UserLinks links;
    private static final int BEGIN_LINK_INDEX = 7;
    private static final String COMMAND = "/track";
    private static final String DESCRIPTION = "Отслеживание ссылки";
    private static final String HANDLE_TEXT = "Начали отслеживать данную ссылку";
    private static final String ALREADY_LINKED_TEXT = "Ссылка уже добавлена, для просмотра ссылок введите /list";
    private static final String INCORRECT_LINK_TEXT = "Вы указали неправильную ссылку, возможно вам поможет /help";
    private static final String NO_LINK_TEXT = "Введите ссылку для отслеживания. Пример ввода: /track ,,ваша_ссылка,,";

    public TrackCommand(UserLinks links) {
        this.links = links;
    }

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
        String answerText;
        try {
            String link = message.text().substring(BEGIN_LINK_INDEX);
            if (Link.isCorrectUri(link)) {
                boolean isInUserLinks = links.isInUserLinks(user, link);
                if (isInUserLinks) {
                    answerText = ALREADY_LINKED_TEXT;
                } else {
                    links.addUserLink(user, link);
                    answerText = HANDLE_TEXT;
                }
            } else {
                answerText = INCORRECT_LINK_TEXT;
            }
        } catch (Exception exception) {
            answerText = NO_LINK_TEXT;
        }

        return answerText;
    }
}