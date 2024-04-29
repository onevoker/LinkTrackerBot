package edu.java.scrapper.domainTest.servicesTest;

import dto.response.LinkResponse;
import dto.response.ListLinksResponse;
import edu.java.scrapper.IntegrationTest;
import edu.java.scrapper.controllers.exceptions.LinkWasTrackedException;
import edu.java.scrapper.domain.repositories.interfaces.ChatLinkRepository;
import edu.java.scrapper.domain.repositories.interfaces.ChatRepository;
import edu.java.scrapper.domain.repositories.interfaces.LinkRepository;
import edu.java.scrapper.domain.services.LinkTrackerService;
import edu.java.scrapper.domain.services.interfaces.LinkService;
import java.net.URI;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
public class LinkTrackerServiceTest extends IntegrationTest {
    @Autowired
    private ChatRepository chatRepository;
    @Autowired
    private LinkRepository linkRepository;
    @Autowired
    private ChatLinkRepository chatLinkRepository;
    private static final Long CHAT_ID = 14L;
    private static final URI URL = URI.create("https://github.com/onevoker/LinkTrackerBot");

    private LinkService linkService;

    @BeforeEach
    void setUp() {
        chatRepository.add(CHAT_ID);
        linkService = new LinkTrackerService(linkRepository, chatLinkRepository);
    }

    @Test
    void addTest() {
        LinkResponse result = linkService.add(CHAT_ID, URL);

        assertAll(
            () -> assertThat(result).isEqualTo(new LinkResponse(CHAT_ID, URL)),
            () -> assertThat(linkRepository.findAll().size()).isEqualTo(1),
            () -> assertThat(chatLinkRepository.findAll().size()).isEqualTo(1),
            () -> {
                var exception = assertThrows(LinkWasTrackedException.class, () -> linkService.add(CHAT_ID, URL));
                assertThat(exception.getMessage()).isEqualTo("Ссылка уже добавлена, для просмотра ссылок введите /list");
            }
        );
    }

    @Test
    void removeTest() {
        linkService.add(CHAT_ID, URL);
        LinkResponse result = linkService.remove(CHAT_ID, URL);

        assertAll(
            () -> assertThat(result).isEqualTo(new LinkResponse(CHAT_ID, URL)),
            () -> assertThat(linkRepository.findAll().size()).isEqualTo(0),
            () -> assertThat(chatLinkRepository.findAll().size()).isEqualTo(0)
        );
    }

    @Test
    void listAllTest() {
        long newChatId = 15L;
        URI newUrl = URI.create("https://github.com/onevoker/Tkf");
        linkService.add(CHAT_ID, URL);
        chatRepository.add(newChatId);
        linkService.add(newChatId, URL);
        linkService.add(CHAT_ID, newUrl);

        ListLinksResponse result = linkService.listAll(CHAT_ID);

        assertAll(
            () -> assertThat(result.size()).isEqualTo(2),
            () -> assertThat(result.links().getFirst().url()).isEqualTo(URL),
            () -> assertThat(result.links().get(1).url()).isEqualTo(newUrl)
        );
    }
}
