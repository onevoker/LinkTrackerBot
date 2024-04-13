package edu.java.scrapper.controllers.telegramConrollers;

import edu.java.scrapper.domain.services.interfaces.ChatService;
import io.micrometer.core.instrument.Counter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tg-chat")
@RequiredArgsConstructor
public class TelegramChatController {
    private final ChatService chatService;
    private final Counter messagesProcessedCounter;

    @PostMapping("/{id}")
    public void registerChat(@PathVariable long id) {
        messagesProcessedCounter.increment();
        chatService.register(id);
    }

    @DeleteMapping("/{id}")
    public void unregisterChat(@PathVariable long id) {
        messagesProcessedCounter.increment();
        chatService.unregister(id);
    }
}
