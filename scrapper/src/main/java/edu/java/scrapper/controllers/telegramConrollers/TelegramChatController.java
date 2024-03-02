package edu.java.scrapper.controllers.telegramConrollers;

import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Log4j2
@RequestMapping("/tg-chat")
public class TelegramChatController {
    @PostMapping("/{id}")
    public void registerChat(@PathVariable int id) {
        log.info("registered");
    }

    @DeleteMapping("/{id}")
    public void deleteChat(@PathVariable int id) {
        log.info("deleted");
    }
}
