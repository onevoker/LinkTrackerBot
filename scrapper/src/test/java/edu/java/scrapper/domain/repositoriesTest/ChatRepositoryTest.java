package edu.java.scrapper.domain.repositoriesTest;

import edu.java.scrapper.IntegrationTest;
import edu.java.scrapper.controllers.exceptions.ChatAlreadyRegisteredException;
import edu.java.scrapper.domain.models.Chat;
import edu.java.scrapper.domain.repositories.interfaces.ChatRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@RequiredArgsConstructor
public class ChatRepositoryTest extends IntegrationTest {
    private final ChatRepository chatRepository;
    private static final Long CHAT_ID = 14L;

    public void addTest() {
        chatRepository.add(CHAT_ID);

        assertThat(chatRepository.findAll().size()).isEqualTo(1);
        var exception = assertThrows(ChatAlreadyRegisteredException.class, () -> chatRepository.add(CHAT_ID));

        assertThat(exception.getMessage()).isEqualTo("Вы уже были зарегестрированы раньше");
    }

    public void removeTest() {
        chatRepository.add(CHAT_ID);

        chatRepository.remove(CHAT_ID);

        assertThat(chatRepository.findAll().size()).isEqualTo(0);
    }

    public void findAllTest() {
        chatRepository.add(1L);
        chatRepository.add(CHAT_ID);

        List<Chat> expected = List.of(new Chat(1L), new Chat(CHAT_ID));

        assertThat(chatRepository.findAll()).usingRecursiveComparison().ignoringCollectionOrder().isEqualTo(expected);
    }
}
