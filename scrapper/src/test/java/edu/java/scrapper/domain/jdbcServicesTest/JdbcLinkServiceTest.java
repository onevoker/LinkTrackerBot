package edu.java.scrapper.domain.jdbcServicesTest;

import edu.java.scrapper.IntegrationTest;
import edu.java.scrapper.controllers.exceptions.LinkWasTrackedException;
import edu.java.scrapper.domain.jdbc.jdbcRepositories.JdbcChatLinkRepository;
import edu.java.scrapper.domain.jdbc.jdbcRepositories.JdbcChatRepository;
import edu.java.scrapper.domain.jdbc.jdbcRepositories.JdbcLinkRepository;
import edu.java.scrapper.domain.jdbc.jdbcServices.JdbcLinkService;
import edu.java.scrapper.domain.repositories.ChatLinkRepository;
import edu.java.scrapper.domain.repositories.ChatRepository;
import edu.java.scrapper.domain.repositories.LinkRepository;
import edu.java.scrapper.domain.services.LinkService;
import edu.java.scrapper.dto.response.LinkResponse;
import edu.java.scrapper.dto.response.ListLinksResponse;
import edu.java.scrapper.linkWorkers.LinkResponseFactory;
import edu.java.scrapper.linkWorkers.LinkResponseValidatorService;
import java.net.URI;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(classes = {
    JdbcChatRepository.class,
    JdbcLinkRepository.class,
    JdbcChatLinkRepository.class,
    LinkResponseValidatorService.class
})
@EnableAutoConfiguration(exclude = LiquibaseAutoConfiguration.class)
public class JdbcLinkServiceTest extends IntegrationTest {
    @Autowired
    private ChatRepository chatRepository;
    @Autowired
    private LinkRepository linkRepository;
    @Autowired
    private ChatLinkRepository chatLinkRepository;
    @Autowired
    private LinkResponseValidatorService linkResponseValidatorService;
    private static final Long CHAT_ID = 14L;
    private static final URI URL = URI.create("https://github.com/onevoker/LinkTrackerBot");

    private LinkService linkService;

    @BeforeEach
    void setUp() {
        chatRepository.add(CHAT_ID);
        LinkResponseFactory linkResponseFactory = new LinkResponseFactory(linkResponseValidatorService);
        linkService = new JdbcLinkService(linkRepository, chatLinkRepository, linkResponseFactory);
    }

    @Test
    @Transactional
    @Rollback
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
    @Transactional
    @Rollback
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
    @Transactional
    @Rollback
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
