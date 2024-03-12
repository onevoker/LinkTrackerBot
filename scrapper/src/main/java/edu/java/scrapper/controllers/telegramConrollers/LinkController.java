package edu.java.scrapper.controllers.telegramConrollers;

import edu.java.scrapper.dto.request.AddLinkRequest;
import edu.java.scrapper.dto.request.RemoveLinkRequest;
import edu.java.scrapper.dto.response.LinkResponse;
import edu.java.scrapper.dto.response.ListLinksResponse;
import edu.java.scrapper.linkWorkers.LinkResponseFactory;
import edu.java.scrapper.repositories.LinkResponseRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import java.net.URI;
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
    private final LinkResponseRepository linkResponseRepository;
    private final LinkResponseFactory linkFactory;

    @GetMapping
    public ListLinksResponse getTrackedLinks(@RequestHeader("Tg-Chat-Id") @Positive long chatId) {
        return linkResponseRepository.getUserLinks(chatId);
    }

    @PostMapping
    public LinkResponse trackLink(
        @RequestHeader("Tg-Chat-Id") @Positive long chatId,
        @RequestBody @Valid AddLinkRequest addLinkRequest
    ) {
        URI url = addLinkRequest.url();
        LinkResponse linkResponse = linkFactory.createLink(chatId, url);
        linkResponseRepository.addUserLink(linkResponse);

        return linkResponse;
    }

    @DeleteMapping
    public LinkResponse untrackLink(
        @RequestHeader("Tg-Chat-Id") @Positive long chatId,
        @RequestBody @Valid RemoveLinkRequest removeLinkRequest
    ) {
        URI url = removeLinkRequest.url();
        LinkResponse linkResponse = linkFactory.createLink(chatId, url);
        linkResponseRepository.deleteUserLink(linkResponse);

        return new LinkResponse(chatId, removeLinkRequest.url());
    }
}
