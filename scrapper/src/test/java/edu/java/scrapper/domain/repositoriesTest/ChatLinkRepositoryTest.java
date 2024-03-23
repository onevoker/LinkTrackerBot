package edu.java.scrapper.domain.repositoriesTest;

import edu.java.scrapper.IntegrationTest;
import edu.java.scrapper.controllers.exceptions.LinkWasTrackedException;
import edu.java.scrapper.domain.models.ChatLink;
import edu.java.scrapper.domain.models.Link;
import edu.java.scrapper.domain.repositories.interfaces.ChatLinkRepository;
import edu.java.scrapper.domain.repositories.interfaces.ChatRepository;
import edu.java.scrapper.domain.repositories.interfaces.LinkRepository;
import java.net.URI;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ChatLinkRepositoryTest extends IntegrationTest {
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
            OffsetDateTime.of(2024, 3, 13, 1, 42, 0, 0, ZoneOffset.UTC),
            OffsetDateTime.of(2024, 3, 13, 1, 42, 0, 0, ZoneOffset.UTC)
        );

    @BeforeEach
    void setUpRepos() {
        chatRepository.add(CHAT_ID);
        linkRepository.add(LINK);
    }

    @Test
    @Transactional
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
    void findTgChatIdsTest() {
        long linkId = linkRepository.findAll().getFirst().getId();
        ChatLink chatLink = new ChatLink(CHAT_ID, linkId);
        chatLinkRepository.add(chatLink);

        List<Long> result = chatLinkRepository.findTgChatIds(linkId);
        List<Long> expected = List.of(CHAT_ID);

        assertThat(result).isEqualTo(expected);
    }
}
