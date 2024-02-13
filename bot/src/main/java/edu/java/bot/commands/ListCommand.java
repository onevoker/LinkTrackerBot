package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.links.UserLinks;
import java.net.URI;
import java.util.Set;

public class ListCommand implements Command {
    private final UserLinks links;
    private static final String COMMAND = "/list";
    private static final String DESCRIPTION = "Список ссылок";
    private static final String HANDLE_TEXT = "Список ваших отслеживаемых ссылок:\n";

    public ListCommand(UserLinks links) {
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
        StringBuilder text;
        Message message = update.message();
        long chatId = message.chat().id();
        Set<URI> userLinks = links.getUserLinks(message.from());
        if (userLinks == null || userLinks.isEmpty()) {
            text = new StringBuilder("Вы не отслеживаете ни одной ссылки(((");
            return new SendMessage(chatId, text.toString());
        }
        text = new StringBuilder(HANDLE_TEXT);
        int i = 0;

        for (URI link : userLinks) {
            text.append("%s. ".formatted(++i)).append(link.toString()).append("\n");
        }

        return new SendMessage(chatId, text.toString());
    }
}
