package edu.java.scrapper.domain.repositories.jdbc;

import edu.java.scrapper.domain.repositories.interfaces.QuestionResponseRepository;
import edu.java.scrapper.dto.stackOverflowDto.Item;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
public class JdbcQuestionResponseRepository implements QuestionResponseRepository {
    private final JdbcTemplate jdbcTemplate;

    @Transactional
    @Override
    public void add(Item responseItem, Long linkId) {
        jdbcTemplate.update(
            "INSERT INTO question_response VALUES (?, ?, ?, ?, ?)",
            responseItem.getQuestionId(),
            linkId,
            responseItem.getAnswered(),
            responseItem.getAnswerCount(),
            responseItem.getLastActivityDate()
        );
    }

    @Transactional
    @Override
    public List<Item> findAll() {
        return jdbcTemplate.query(
            "SELECT * FROM question_response",
            new BeanPropertyRowMapper<>(Item.class)
        );
    }

    @Transactional
    @Override
    public List<Item> findByLinkId(Long linkId) {
        return jdbcTemplate.query(
            "SELECT * FROM question_response WHERE link_id = ?",
            new BeanPropertyRowMapper<>(Item.class),
            linkId
        );
    }

    @Transactional
    @Override
    public void update(Item responseItem, Long linkId) {
        jdbcTemplate.update(
            "UPDATE question_response SET answered = ?, answer_count = ?, last_activity_date = ? WHERE link_id = ?",
            responseItem.getAnswered(),
            responseItem.getAnswerCount(),
            responseItem.getLastActivityDate(),
            linkId
        );
    }
}
