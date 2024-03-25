package edu.java.scrapper.domain.repositoriesTest;

import edu.java.scrapper.IntegrationTest;
import edu.java.scrapper.domain.models.Link;
import edu.java.scrapper.domain.repositories.interfaces.LinkRepository;
import edu.java.scrapper.domain.repositories.interfaces.QuestionResponseRepository;
import edu.java.scrapper.dto.stackOverflowDto.Item;
import java.net.URI;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class QuestionResponseRepositoryTest extends IntegrationTest {
    @Autowired
    private QuestionResponseRepository questionResponseRepository;
    @Autowired
    private LinkRepository linkRepository;

    private static final Link LINK =
        new Link(
            URI.create(
                "https://stackoverflow.com/questions/78013649/python-functional-method-of-checking-that-all-elements-in-a-list-are-equal"),
            OffsetDateTime.now(ZoneOffset.UTC),
            OffsetDateTime.now(ZoneOffset.UTC)
        );
    private static final long QUESTION_ID = 1L;
    private static final boolean ANSWERED = true;
    private static final Item QUESTION_ITEM = new Item(ANSWERED, QUESTION_ID, 3, OffsetDateTime.of(
        2024, 3, 14, 12, 13, 20, 0, ZoneOffset.UTC)
    );
    private Long linkId;

    @BeforeEach
    void setUpRepos() {
        linkRepository.add(LINK);
        linkId = linkRepository.findAll().getFirst().getId();
    }

    @Test
    @Transactional
    void addAndFindAllTest() {
        questionResponseRepository.add(QUESTION_ITEM, linkId);

        Item result = questionResponseRepository.findAll().getFirst();

        assertAll(
            () -> assertThat(result.getQuestionId()).isEqualTo(QUESTION_ID),
            () -> assertThat(result.getAnswered()).isTrue()
        );
    }

    @Test
    @Transactional
    void findByLinkIdTest() {
        questionResponseRepository.add(QUESTION_ITEM, linkId);

        Item result = questionResponseRepository.findByLinkId(linkId).getFirst();

        assertAll(
            () -> assertThat(result.getQuestionId()).isEqualTo(QUESTION_ID),
            () -> assertThat(result.getAnswered()).isTrue()
        );
    }

    @Test
    @Transactional
    void updateTest() {
        questionResponseRepository.add(QUESTION_ITEM, linkId);
        long newAnswerCount = 5L;
        Item newResponse = QUESTION_ITEM;
        newResponse.setAnswerCount(newAnswerCount);
        questionResponseRepository.update(newResponse, linkId);

        Long answerCountInRepo = questionResponseRepository.findByLinkId(linkId).getFirst().getAnswerCount();

        assertThat(answerCountInRepo).isEqualTo(newAnswerCount);
    }
}
