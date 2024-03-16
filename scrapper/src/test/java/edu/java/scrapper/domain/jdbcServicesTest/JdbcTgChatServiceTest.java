package edu.java.scrapper.domain.jdbcServicesTest;

import edu.java.scrapper.IntegrationTest;
import edu.java.scrapper.domain.jdbc.jdbcRepositories.JdbcChatLinkRepository;
import edu.java.scrapper.domain.jdbc.jdbcRepositories.JdbcChatRepository;
import edu.java.scrapper.domain.jdbc.jdbcRepositories.JdbcLinkRepository;
import edu.java.scrapper.domain.jdbc.jdbcServices.JdbcTgChatService;
import edu.java.scrapper.domain.repositories.ChatLinkRepository;
import edu.java.scrapper.domain.repositories.ChatRepository;
import edu.java.scrapper.domain.repositories.LinkRepository;
import edu.java.scrapper.domain.services.TgChatService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(classes = {
    JdbcChatRepository.class,
    JdbcLinkRepository.class,
    JdbcChatLinkRepository.class
})
@EnableAutoConfiguration(exclude = LiquibaseAutoConfiguration.class)
public class JdbcTgChatServiceTest extends IntegrationTest {
    @Autowired
    private ChatRepository chatRepository;
    @Autowired
    private LinkRepository linkRepository;
    @Autowired
    private ChatLinkRepository chatLinkRepository;
    private static final Long CHAT_ID = 14L;
    private TgChatService chatService;

    @BeforeEach
    void setUp() {
        chatService = new JdbcTgChatService(chatRepository, linkRepository, chatLinkRepository);
    }

    @Test
    @Transactional
    @Rollback
    void registerTest() {
        chatService.register(CHAT_ID);
        assertThat(chatRepository.findAll().size()).isEqualTo(1);
    }

    @Test
    @Transactional
    @Rollback
    void unregisterTest() {
        chatService.register(CHAT_ID);
        chatService.unregister(CHAT_ID);

        assertThat(chatRepository.findAll().isEmpty()).isTrue();
    }
}
