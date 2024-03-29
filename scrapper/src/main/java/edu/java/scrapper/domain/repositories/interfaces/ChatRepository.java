package edu.java.scrapper.domain.repositories.interfaces;

import edu.java.scrapper.domain.models.Chat;
import java.util.List;

public interface ChatRepository {
    void add(Long id);

    int remove(Long id);

    List<Chat> findAll();
}
