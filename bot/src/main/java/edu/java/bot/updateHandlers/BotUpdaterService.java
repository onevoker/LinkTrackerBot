package edu.java.bot.updateHandlers;

import com.pengrad.telegrambot.request.SendMessage;
import dto.response.LinkUpdateResponse;
import edu.java.bot.linkTrackerBot.Bot;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BotUpdaterService {
    private final Bot bot;

    public void sendUpdate(LinkUpdateResponse update) {
        List<Long> tgChatIds = update.tgChatIds();
        String url = update.url().toString();
        String description = update.description();

        for (Long chatId : tgChatIds) {
            SendMessage sendMessage = new SendMessage(chatId, description + ":\n" + url);
            bot.execute(sendMessage);
        }
    }

}
