package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.links.Link;
import edu.java.bot.links.UserLinks;

public class UntrackCommand implements Command {
    private final UserLinks links;
    private static final int BEGIN_LINK_INDEX = 9;
    private static final String COMMAND = "/untrack";
    private static final String DESCRIPTION = "Прекращение отслеживания ссылки";
    private static final String HANDLE_TEXT = "Прекратили отслеживание данной ссылки";

    public UntrackCommand(UserLinks links) {
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
                    links.deleteUserLink(user, link);
                    answerText = HANDLE_TEXT;
                } else {
                    answerText = "Вы не отслеживаете данную ссылку";
                }
            } else {
                answerText = "Вы указали неправильную ссылку, возможно вам поможет /help";
            }
        } catch (Exception exception) {
            answerText = "Укажите что перестать отслеживать. Пример /untrack ,,ваша_ссылка,,";
        }

        return answerText;
    }
}
