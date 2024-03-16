package edu.java.bot.controllers;

import edu.java.bot.botServices.BotUpdaterService;
import edu.java.bot.dto.request.LinkUpdateRequest;
import jakarta.validation.Valid;
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
    private final BotUpdaterService botUpdaterService;

    @PostMapping
    public void sendUpdate(@RequestBody @Valid LinkUpdateRequest updateRequest) {
        botUpdaterService.sendUpdates(updateRequest);
    }
}
