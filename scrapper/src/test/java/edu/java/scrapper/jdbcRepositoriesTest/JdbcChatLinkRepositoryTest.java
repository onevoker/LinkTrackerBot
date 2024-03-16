package edu.java.scrapper.jdbcRepositoriesTest;

import edu.java.scrapper.IntegrationTest;
import edu.java.scrapper.controllers.exceptions.LinkWasTrackedException;
import edu.java.scrapper.domain.jdbc.jdbcRepositories.JdbcChatLinkRepository;
import edu.java.scrapper.domain.jdbc.jdbcRepositories.JdbcChatRepository;
import edu.java.scrapper.domain.jdbc.jdbcRepositories.JdbcLinkRepository;
import edu.java.scrapper.domain.models.ChatLink;
import edu.java.scrapper.domain.models.Link;
import java.net.URI;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import edu.java.scrapper.domain.repositories.ChatLinkRepository;
import edu.java.scrapper.domain.repositories.ChatRepository;
import edu.java.scrapper.domain.repositories.LinkRepository;
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

@SpringBootTest(classes = {JdbcChatLinkRepository.class, JdbcChatRepository.class, JdbcLinkRepository.class})
@EnableAutoConfiguration(exclude = LiquibaseAutoConfiguration.class)
public class JdbcChatLinkRepositoryTest extends IntegrationTest {
    @Autowired
    private ChatLinkRepository chatLinkRepository;
    @Autowired
    private ChatRepository chatRepository;
    @Autowired
    private LinkRepository linkRepository;

    private static final long CHAT_ID = 14L;
    private static final Link LINK =
        new Link(
            URI.create("https://github.com/onevoker/LinkTrackerBot"),
            OffsetDateTime.of(2024, 3, 13, 1, 42, 0, 0, ZoneOffset.UTC)
        );

    @BeforeEach
    void setUpRepos() {
        chatRepository.add(CHAT_ID);
        linkRepository.add(LINK);
    }

    @Test
    @Transactional
    @Rollback
    void addTest() {
        long linkId = linkRepository.findAll().getFirst().getId();
        ChatLink chatLink = new ChatLink(CHAT_ID, linkId);

        chatLinkRepository.add(chatLink);

        assertThat(chatLinkRepository.findAll().size()).isEqualTo(1);
        var exception = assertThrows(LinkWasTrackedException.class, () -> chatLinkRepository.add(chatLink));
        assertThat(exception.getMessage()).isEqualTo("Ссылка уже добавлена, для просмотра ссылок введите /list");
    }

    @Test
    @Transactional
    @Rollback
    void removeTest() {
        long linkId = linkRepository.findAll().getFirst().getId();
        ChatLink chatLink = new ChatLink(CHAT_ID, linkId);
        chatLinkRepository.add(chatLink);

        int removedSize = chatLinkRepository.remove(chatLink);

        assertThat(removedSize).isEqualTo(1);
        assertThat(chatLinkRepository.findAll().size()).isEqualTo(0);
    }

    @Test
    @Transactional
    @Rollback
    void findAllTest() {
        long linkId = linkRepository.findAll().getFirst().getId();
        ChatLink chatLink = new ChatLink(CHAT_ID, linkId);
        chatLinkRepository.add(chatLink);

        ChatLink result = chatLinkRepository.findAll().getFirst();

        assertAll(
            () -> assertThat(chatLink.getChatId()).isEqualTo(result.getChatId()),
            () -> assertThat(chatLink.getLinkId()).isEqualTo(result.getLinkId())
        );
    }

    @Test
    @Transactional
    @Rollback
    void findLinksByTgChatIdTest() {
        long linkId = linkRepository.findAll().getFirst().getId();
        ChatLink chatLink = new ChatLink(CHAT_ID, linkId);
        chatLinkRepository.add(chatLink);

        List<Link> result = chatLinkRepository.findLinksByTgChatId(CHAT_ID);
        Link expectedLink = LINK;
        expectedLink.setId(linkId);
        List<Link> expected = List.of(expectedLink);

        assertThat(result.size()).isEqualTo(1);
        assertThat(result.getFirst().getId()).isEqualTo(expectedLink.getId());
        assertThat(result.getFirst().getUrl()).isEqualTo(expected.getFirst().getUrl());
    }

    @Test
    @Transactional
    @Rollback
    void findTgChatIdsTest() {
        long linkId = linkRepository.findAll().getFirst().getId();
        ChatLink chatLink = new ChatLink(CHAT_ID, linkId);
        chatLinkRepository.add(chatLink);

        List<Long> result = chatLinkRepository.findTgChatIds(linkId);
        List<Long> expected = List.of(CHAT_ID);

        assertThat(result).isEqualTo(expected);
    }
}
