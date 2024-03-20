package edu.java.scrapper.linkWorkersTest;

import edu.java.scrapper.linkParser.services.StackOverflowParserService;
import java.net.URI;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class StackOverflowParserServiceTest {
    @Autowired
    private StackOverflowParserService stackOverflowParserService;
    private static final String preparedQuestion =
        "https://stackoverflow.com/questions/%s/python-functional-method-of-checking-that-all-elements-in-a-list-are-equal";

    @Test
    void testGetQuestionId() {
        long questionId = 78013649;
        URI url = URI.create(preparedQuestion.formatted(questionId));
        Long parsedQuestionId = stackOverflowParserService.getLinkData(url).questionId();
        assertThat(parsedQuestionId).isEqualTo(questionId);
    }
}
