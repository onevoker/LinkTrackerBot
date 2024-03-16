package edu.java.scrapper.domain.jdbcRepositoriesTest;

import edu.java.scrapper.IntegrationTest;
import edu.java.scrapper.domain.repositories.jdbc.JdbcLinkRepository;
import edu.java.scrapper.domain.repositories.jdbc.JdbcQuestionResponseRepository;
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
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(classes = {JdbcQuestionResponseRepository.class, JdbcLinkRepository.class})
@EnableAutoConfiguration(exclude = LiquibaseAutoConfiguration.class)
public class JdbcQuestionResponseRepositoryTest extends IntegrationTest {
    @Autowired
    private QuestionResponseRepository questionResponseRepository;
    @Autowired
    private LinkRepository linkRepository;

    private static final Link LINK =
        new Link(
            URI.create("https://github.com/onevoker"),
            OffsetDateTime.now().with(ZoneOffset.UTC),
            OffsetDateTime.now().with(ZoneOffset.UTC)
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
    @Rollback
    void addAndFindAllTest() {
        questionResponseRepository.add(QUESTION_ITEM, linkId);
        assertThat(questionResponseRepository.findAll().getFirst()).isEqualTo(QUESTION_ITEM);
    }

    @Test
    @Transactional
    @Rollback
    void findByLinkIdTest() {
        questionResponseRepository.add(QUESTION_ITEM, linkId);

        Item result = questionResponseRepository.findByLinkId(linkId).getFirst();

        assertThat(result).isEqualTo(QUESTION_ITEM);
    }

    @Test
    @Transactional
    @Rollback
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
