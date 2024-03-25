package edu.java.scrapper.domain.repositories.interfaces;

import edu.java.scrapper.dto.stackOverflowDto.Item;
import java.util.List;

public interface QuestionResponseRepository {
    void add(Item responseItem, Long linkId);

    List<Item> findAll();

    List<Item> findByLinkId(Long linkId);

    void update(Item responseItem, Long linkId);

}
