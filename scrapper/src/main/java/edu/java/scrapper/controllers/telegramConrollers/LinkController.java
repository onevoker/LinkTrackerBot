package edu.java.scrapper.controllers.telegramConrollers;

import edu.java.scrapper.dto.request.AddLinkRequest;
import edu.java.scrapper.dto.request.RemoveLinkRequest;
import edu.java.scrapper.dto.response.LinkResponse;
import edu.java.scrapper.dto.response.ListLinksResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import java.net.URI;
import java.util.List;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/links")
public class LinkController {
    @GetMapping
    public ListLinksResponse getTrackedLinks(@RequestHeader("Tg-Chat-Id") @Positive int chatId) {
        URI url = URI.create("https://github.com/onevoker/repos/LinkTrackerBot");
        LinkResponse linkResponse = new LinkResponse(chatId, url);

        return new ListLinksResponse(List.of(linkResponse), 1);
    }

    @PostMapping
    public LinkResponse trackLink(
        @RequestHeader("Tg-Chat-Id") @Positive int chatId,
        @RequestBody @Valid AddLinkRequest addLinkRequest
    ) {
        URI link = addLinkRequest.url();

        return new LinkResponse(chatId, link);
    }

    @DeleteMapping
    public LinkResponse untrackLink(
        @RequestHeader("Tg-Chat-Id") @Positive int chatId,
        @RequestBody @Valid RemoveLinkRequest removeLinkRequest
    ) {
        return new LinkResponse(chatId, removeLinkRequest.url());
    }
}
