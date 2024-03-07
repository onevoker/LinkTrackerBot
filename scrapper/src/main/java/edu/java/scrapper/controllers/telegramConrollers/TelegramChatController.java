package edu.java.scrapper.controllers.telegramConrollers;

import edu.java.scrapper.repositories.ChatIdRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Log4j2
@RequestMapping("/tg-chat")
@RequiredArgsConstructor
public class TelegramChatController {
    private final ChatIdRepository chatIdRepository;

    @PostMapping("/{id}")
    public void registerChat(@PathVariable long id) {
        chatIdRepository.registerChat(id);
    }

    @DeleteMapping("/{id}")
    public void unregisterChat(@PathVariable long id) {
        chatIdRepository.unregisterChat(id);
    }
}
