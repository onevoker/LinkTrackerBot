package edu.java.scrapper.repositoriesTest;

import edu.java.scrapper.controllers.exceptions.ChatAlreadyRegisteredException;
import edu.java.scrapper.controllers.exceptions.ChatNotFoundException;
import edu.java.scrapper.repositories.ChatIdRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ChatIdRepositoryTest {
    private static final long CHAT_ID = 1L;
    private ChatIdRepository chatIdRepository;

    @BeforeEach
    void setUp() {
        chatIdRepository = new ChatIdRepository();
    }

    @Test
    void testRegister() {
        assertDoesNotThrow(() -> chatIdRepository.registerChat(CHAT_ID));
    }

    @Test
    void testIsNotRegistered() {
        chatIdRepository.registerChat(CHAT_ID);

        var exception =
            assertThrows(ChatAlreadyRegisteredException.class, () -> chatIdRepository.registerChat(CHAT_ID));
        assertThat(exception.getMessage()).isEqualTo("Вы уже были зарегестрированы раньше");
    }

    @Test
    void testUnregistered() {
        chatIdRepository.registerChat(CHAT_ID);
        assertDoesNotThrow(() -> chatIdRepository.unregisterChat(CHAT_ID));
    }

    @Test
    void testUnregisterNotRegistered() {
        var exception =
            assertThrows(ChatNotFoundException.class, () -> chatIdRepository.unregisterChat(CHAT_ID));
        assertThat(exception.getMessage()).isEqualTo("Вы не были зарегестрированы");
    }
}
