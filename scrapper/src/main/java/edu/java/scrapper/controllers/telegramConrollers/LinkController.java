package edu.java.scrapper.controllers.telegramConrollers;

import edu.java.scrapper.domain.services.interfaces.LinkService;
import edu.java.scrapper.dto.request.AddLinkRequest;
import edu.java.scrapper.dto.request.RemoveLinkRequest;
import edu.java.scrapper.dto.response.LinkResponse;
import edu.java.scrapper.dto.response.ListLinksResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/links")
@RequiredArgsConstructor
public class LinkController {
    private final LinkService linkService;

    @GetMapping
    public ListLinksResponse getTrackedLinks(@RequestHeader("Tg-Chat-Id") @Positive long chatId) {
        return linkService.listAll(chatId);
    }

    @PostMapping
    public LinkResponse trackLink(
        @RequestHeader("Tg-Chat-Id") @Positive long chatId,
        @RequestBody @Valid AddLinkRequest addLinkRequest
    ) {
        return linkService.add(chatId, addLinkRequest.url());
    }

    @DeleteMapping
    public LinkResponse untrackLink(
        @RequestHeader("Tg-Chat-Id") @Positive long chatId,
        @RequestBody @Valid RemoveLinkRequest removeLinkRequest
    ) {
        return linkService.remove(chatId, removeLinkRequest.url());
    }
}
