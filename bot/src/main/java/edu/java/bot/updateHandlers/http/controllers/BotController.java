package edu.java.bot.updateHandlers.http.controllers;

import edu.java.bot.dto.response.LinkUpdateResponse;
import edu.java.bot.updateHandlers.BotUpdaterService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/updates")
@RequiredArgsConstructor
public class BotController {
    private final BotUpdaterService botUpdaterService;

    @PostMapping
    public void sendUpdate(@RequestBody @Valid LinkUpdateResponse update) {
        botUpdaterService.sendUpdate(update);
    }
}
