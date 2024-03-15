package edu.java.bot.controllers;

import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.dto.request.LinkUpdateRequest;
import edu.java.bot.linkTrackerBot.LinkTrackerBot;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Log4j2
@RequestMapping("/updates")
@RequiredArgsConstructor
public class BotController {
    private final LinkTrackerBot bot;

    @PostMapping
    public void sendUpdate(@RequestBody @Valid LinkUpdateRequest updateRequest) {
        List<Long> tgChatIds = updateRequest.tgChatIds();
        String url = updateRequest.url().toString();
        String description = updateRequest.description();
        for (Long chatId : tgChatIds) {
            SendMessage sendMessage = new SendMessage(chatId, description + ":\n" + url);
            bot.execute(sendMessage);
        }
    }
}
