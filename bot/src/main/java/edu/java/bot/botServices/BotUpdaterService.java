package edu.java.bot.botServices;

import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.dto.request.LinkUpdateRequest;
import edu.java.bot.linkTrackerBot.Bot;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BotUpdaterService {
    private final Bot bot;

    public void sendUpdates(LinkUpdateRequest updateRequest) {
        List<Long> tgChatIds = updateRequest.tgChatIds();
        String url = updateRequest.url().toString();
        String description = updateRequest.description();

        for (Long chatId : tgChatIds) {
            SendMessage sendMessage = new SendMessage(chatId, description + ":\n" + url);
            bot.execute(sendMessage);
        }
    }

}
