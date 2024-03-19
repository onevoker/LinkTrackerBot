package edu.java.scrapper.domain.repositories.jooq;

import edu.java.scrapper.domain.repositories.interfaces.QuestionResponseRepository;
import edu.java.scrapper.dto.stackOverflowDto.Item;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;
import static edu.java.scrapper.domain.repositories.jooq.generated.Tables.QUESTION_RESPONSE;

@Repository
@RequiredArgsConstructor
public class JooqQuestionResponseRepository implements QuestionResponseRepository {
    private final DSLContext dsl;

    @Override
    public void add(Item responseItem, Long linkId) {
        dsl.insertInto(
                QUESTION_RESPONSE,
                QUESTION_RESPONSE.QUESTION_ID,
                QUESTION_RESPONSE.LINK_ID,
                QUESTION_RESPONSE.ANSWERED,
                QUESTION_RESPONSE.ANSWER_COUNT,
                QUESTION_RESPONSE.LAST_ACTIVITY_DATE
            )
            .values(
                responseItem.getQuestionId(),
                linkId,
                responseItem.getAnswered(),
                responseItem.getAnswerCount(),
                responseItem.getLastActivityDate()
            )
            .execute();
    }

    @Override
    public List<Item> findAll() {
        return dsl.selectFrom(QUESTION_RESPONSE)
            .fetchInto(Item.class);
    }

    @Override
    public List<Item> findByLinkId(Long linkId) {
        return dsl.selectFrom(QUESTION_RESPONSE)
            .where(QUESTION_RESPONSE.LINK_ID.eq(linkId))
            .fetchInto(Item.class);
    }

    @Override
    public void update(Item responseItem, Long linkId) {
        dsl.update(QUESTION_RESPONSE)
            .set(QUESTION_RESPONSE.ANSWERED, responseItem.getAnswered())
            .set(QUESTION_RESPONSE.ANSWER_COUNT, responseItem.getAnswerCount())
            .set(QUESTION_RESPONSE.LAST_ACTIVITY_DATE, responseItem.getLastActivityDate())
            .where(QUESTION_RESPONSE.LINK_ID.eq(linkId))
            .execute();
    }
}
