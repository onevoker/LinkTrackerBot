package edu.java.scrapper.domain.repositoriesTest;

import edu.java.scrapper.IntegrationTest;
import edu.java.scrapper.controllers.exceptions.ChatAlreadyRegisteredException;
import edu.java.scrapper.domain.models.Chat;
import edu.java.scrapper.domain.repositories.interfaces.ChatRepository;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ChatRepositoryTest extends IntegrationTest {
    @Autowired
    private ChatRepository chatRepository;
    private static final Long CHAT_ID = 14L;

    @Test
    @Transactional
    @Rollback
    void addTest() {
        chatRepository.add(CHAT_ID);
        assertThat(chatRepository.findAll().size()).isEqualTo(1);
        var exception = assertThrows(ChatAlreadyRegisteredException.class, () -> chatRepository.add(CHAT_ID));
        assertThat(exception.getMessage()).isEqualTo("Вы уже были зарегестрированы раньше");
    }

    @Test
    @Transactional
    @Rollback
    void removeTest() {
        chatRepository.add(CHAT_ID);
        chatRepository.remove(CHAT_ID);

        assertThat(chatRepository.findAll().size()).isEqualTo(0);
    }

    @Test
    @Transactional
    @Rollback
    void findAllTest() {
        chatRepository.add(1L);
        chatRepository.add(CHAT_ID);

        List<Chat> expected = List.of(new Chat(1L), new Chat(CHAT_ID));

        assertThat(chatRepository.findAll()).usingRecursiveComparison().ignoringCollectionOrder().isEqualTo(expected);
    }
}
