package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.links.LinkRepository;
import edu.java.bot.links.LinkUtils;

public class UntrackCommand implements Command {
    private final LinkRepository links;
    private static final int BEGIN_LINK_INDEX = 9;
    private static final String COMMAND = "/untrack";
    private static final String DESCRIPTION = "Прекращение отслеживания ссылки";
    private static final String HANDLE_TEXT = "Прекратили отслеживание данной ссылки";
    private static final String NOT_LINKED_TEXT = "Вы не отслеживаете данную ссылку";
    private static final String INCORRECT_LINK_TEXT = "Вы указали неправильную ссылку, возможно вам поможет /help";
    private static final String NO_LINK_TEXT = "Укажите что перестать отслеживать. Пример /untrack ,,ваша_ссылка,,";

    public UntrackCommand(LinkRepository links) {
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
            if (LinkUtils.isCorrectUri(link)) {
                boolean isInUserLinks = links.isInUserLinks(user, link);
                if (isInUserLinks) {
                    links.deleteUserLink(user, link);
                    answerText = HANDLE_TEXT;
                } else {
                    answerText = NOT_LINKED_TEXT;
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
