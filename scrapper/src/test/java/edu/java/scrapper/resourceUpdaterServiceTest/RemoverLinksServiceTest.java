package edu.java.scrapper.resourceUpdaterServiceTest;

import edu.java.scrapper.IntegrationTest;
import edu.java.scrapper.domain.models.ChatLink;
import edu.java.scrapper.domain.models.Link;
import edu.java.scrapper.domain.repositories.interfaces.ChatLinkRepository;
import edu.java.scrapper.domain.repositories.interfaces.ChatRepository;
import edu.java.scrapper.domain.repositories.interfaces.LinkRepository;
import edu.java.scrapper.dto.response.LinkUpdateResponse;
import edu.java.scrapper.scheduler.updaterWorkers.resourceUpdaterService.RemoverLinksService;
import java.net.URI;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest
public class RemoverLinksServiceTest extends IntegrationTest {
    @Autowired
    private ChatLinkRepository chatLinkRepository;
    @Autowired
    private LinkRepository linkRepository;
    @Autowired
    private ChatRepository chatRepository;
    @Autowired
    private RemoverLinksService removerLinksService;

    private static final Long CHAT_ID = 1L;
    private static final URI URL = URI.create("https://github.com/onevoker/LinkTrackerBot");
    private final Link link =
        new Link(URL, OffsetDateTime.of(
            2024, 3, 11, 12, 13, 20, 0, ZoneOffset.UTC
        ), OffsetDateTime.of(
            2024, 3, 15, 21, 10, 40, 0, ZoneOffset.UTC
        ));
    private long linkId;

    @BeforeEach
    void setUp() {
        chatRepository.add(CHAT_ID);
        linkRepository.add(link);
        linkId = linkRepository.findAll().getFirst().getId();
        chatLinkRepository.add(new ChatLink(CHAT_ID, linkId));
    }

    @Test
    @Transactional
    void testRemoveLinkInDatabaseAndGetResponse() {
        LinkUpdateResponse response = removerLinksService.removeLinkInDatabaseAndGetResponse(linkId, URL);

        String expectedDescription = "Вы перестали ослеживать данную ссылку, так как она была удалена автором";
        LinkUpdateResponse expectedResponse = new LinkUpdateResponse(URL, expectedDescription, List.of(CHAT_ID));
        var emptyList = Collections.emptyList();

        assertAll(
            () -> assertThat(linkRepository.findAll()).isEqualTo(emptyList),
            () -> assertThat(chatLinkRepository.findAll()).isEqualTo(emptyList),
            () -> assertThat(chatRepository.findAll()).isNotEqualTo(emptyList),
            () -> assertThat(response).isEqualTo(expectedResponse)
        );
    }
}
