package edu.java.scrapper.domain.repositories.jpa;

import edu.java.scrapper.domain.modelsMapper.ModelsMapper;
import edu.java.scrapper.domain.repositories.interfaces.QuestionResponseRepository;
import edu.java.scrapper.domain.repositories.jpa.entities.ItemEntity;
import edu.java.scrapper.domain.repositories.jpa.repos.JpaQuestionResponseEntityRepository;
import edu.java.scrapper.dto.stackOverflowDto.Item;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JpaQuestionResponseRepository implements QuestionResponseRepository {
    private final JpaQuestionResponseEntityRepository questionResponseEntityRepository;
    private final ModelsMapper mapper;

    @Override
    public void add(Item responseItem, Long linkId) {
        ItemEntity itemEntity = mapper.getItemEntity(responseItem, linkId);
        questionResponseEntityRepository.save(itemEntity);
    }

    @Override
    public List<Item> findAll() {
        return questionResponseEntityRepository.findAll().stream()
            .map(mapper::getItem)
            .collect(Collectors.toList());
    }

    @Override
    public List<Item> findByLinkId(Long linkId) {
        return questionResponseEntityRepository.findByLinkId(linkId).stream()
            .map(mapper::getItem)
            .collect(Collectors.toList());
    }

    @Override
    public void update(Item responseItem, Long linkId) {
        Optional<ItemEntity> byId = questionResponseEntityRepository.findById(responseItem.getQuestionId());
        if (byId.isPresent()) {
            ItemEntity itemEntity = byId.get();
            itemEntity.setAnswered(responseItem.getAnswered());
            itemEntity.setAnswerCount(responseItem.getAnswerCount());
            itemEntity.setLastActivityDate(responseItem.getLastActivityDate());
            questionResponseEntityRepository.save(itemEntity);
        }
    }
}
