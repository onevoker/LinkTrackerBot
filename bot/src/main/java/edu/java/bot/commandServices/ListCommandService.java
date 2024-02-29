package edu.java.bot.commandServices;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.links.Link;
import edu.java.bot.repositories.LinkRepository;
import java.util.Collection;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ListCommandService implements CommandService {
    private final LinkRepository linkRepository;
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
    public SendMessage handle(Update update) {
        Message message = update.message();
        long chatId = message.chat().id();
        Set<Link> userLinks = linkRepository.getUserLinks(message.from());

        if (userLinks == null || userLinks.isEmpty()) {
            return new SendMessage(chatId, NOT_LINKED_MESSAGE);
        }

        String text = HANDLE_TEXT + getLinksListText(userLinks);
        return new SendMessage(chatId, text);
    }

    private String getLinksListText(Collection<Link> userLinks) {
        StringBuilder text = new StringBuilder();
        int i = 0;

        for (Link link : userLinks) {
            text.append("%s. ".formatted(++i)).append(link.stringLink()).append("\n");
        }

        return text.toString();
    }
}
