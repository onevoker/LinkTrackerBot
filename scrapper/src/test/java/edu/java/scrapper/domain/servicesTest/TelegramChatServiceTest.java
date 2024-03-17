package edu.java.scrapper.domain.servicesTest;

import edu.java.scrapper.IntegrationTest;
import edu.java.scrapper.domain.repositories.interfaces.ChatRepository;
import edu.java.scrapper.domain.services.interfaces.ChatService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class TelegramChatServiceTest extends IntegrationTest {
    @Autowired
    private ChatRepository chatRepository;
    @Autowired
    private ChatService chatService;
    private static final Long CHAT_ID = 14L;

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
