package edu.java.scrapper.domain.repositories;

import edu.java.scrapper.domain.models.Chat;
import java.util.List;

public interface ChatRepository {
    void add(Long id);

    List<Long> remove(Long id);

    List<Chat> findAll();
}
