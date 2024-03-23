package edu.java.scrapper.domain.repositories.jpa;

import edu.java.scrapper.controllers.exceptions.ChatAlreadyRegisteredException;
import edu.java.scrapper.domain.ModelsMapper;
import edu.java.scrapper.domain.models.Chat;
import edu.java.scrapper.domain.repositories.interfaces.ChatRepository;
import edu.java.scrapper.domain.repositories.jpa.entities.ChatEntity;
import edu.java.scrapper.domain.repositories.jpa.repos.JpaChatEntityRepository;
import edu.java.scrapper.domain.repositories.jpa.repos.JpaChatLinkEntityRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
public class JpaChatRepository implements ChatRepository {
    private final ModelsMapper mapper;
    private final JpaChatEntityRepository chatEntityRepository;
    private final JpaChatLinkEntityRepository chatLinkEntityRepository;

    @Override
    public void add(Long id) {
        if (chatEntityRepository.existsById(id)) {
            throw new ChatAlreadyRegisteredException("Вы уже были зарегестрированы раньше");
        }
        chatEntityRepository.save(new ChatEntity(id));
    }

    @Transactional
    @Override
    public int remove(Long id) {
        if (chatEntityRepository.existsById(id)) {
            chatLinkEntityRepository.deleteByChatId(id);
            chatEntityRepository.deleteById(id);
            return 1;
        } else {
            return 0;
        }
    }

    @Override
    public List<Chat> findAll() {
        return chatEntityRepository.findAll().stream()
            .map(mapper::getChat)
            .collect(Collectors.toList());
    }
}
