package edu.java.scrapper.domain.jdbcServicesTest;

import edu.java.scrapper.IntegrationTest;
import edu.java.scrapper.domain.repositories.jdbc.JdbcChatLinkRepository;
import edu.java.scrapper.domain.repositories.jdbc.JdbcChatRepository;
import edu.java.scrapper.domain.repositories.jdbc.JdbcLinkRepository;
import edu.java.scrapper.domain.services.TelegramChatService;
import edu.java.scrapper.domain.repositories.interfaces.ChatLinkRepository;
import edu.java.scrapper.domain.repositories.interfaces.ChatRepository;
import edu.java.scrapper.domain.repositories.interfaces.LinkRepository;
import edu.java.scrapper.domain.services.interfaces.ChatService;
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
    private ChatService chatService;

    @BeforeEach
    void setUp() {
        chatService = new TelegramChatService(chatRepository, linkRepository, chatLinkRepository);
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
