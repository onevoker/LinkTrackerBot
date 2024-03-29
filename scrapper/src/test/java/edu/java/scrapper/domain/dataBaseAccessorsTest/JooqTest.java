package edu.java.scrapper.domain.dataBaseAccessorsTest;

import edu.java.scrapper.domain.repositories.interfaces.ChatLinkRepository;
import edu.java.scrapper.domain.repositories.interfaces.ChatRepository;
import edu.java.scrapper.domain.repositories.interfaces.GitHubResponseRepository;
import edu.java.scrapper.domain.repositories.interfaces.LinkRepository;
import edu.java.scrapper.domain.repositories.interfaces.QuestionResponseRepository;
import edu.java.scrapper.domain.repositoriesTest.ChatLinkRepositoryTest;
import edu.java.scrapper.domain.repositoriesTest.ChatRepositoryTest;
import edu.java.scrapper.domain.repositoriesTest.GitHubResponseRepositoryTest;
import edu.java.scrapper.domain.repositoriesTest.LinkRepositoryTest;
import edu.java.scrapper.domain.repositoriesTest.QuestionResponseRepositoryTest;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest(properties = {"file= ./scrapper/src/main/resources/application.yml", "app.database-access-type=jooq"})
@Transactional
public class JooqTest {
    @Autowired
    private ChatLinkRepository jooqChatLinkRepository;
    @Autowired
    private ChatRepository jooqChatRepository;
    @Autowired
    private GitHubResponseRepository jooqGitHubResponseRepository;
    @Autowired
    private LinkRepository jooqLinkRepository;
    @Autowired
    private QuestionResponseRepository jooqQuestionResponseRepository;

    @Nested
    class JooqChatLinkRepositoryTest {
        private final ChatLinkRepositoryTest jooqChatLinkRepositoryTest =
            new ChatLinkRepositoryTest(jooqChatLinkRepository, jooqChatRepository, jooqLinkRepository);

        @Test
        void addTest() {
            jooqChatLinkRepositoryTest.addTest();
        }

        @Test
        void removeTest() {
            jooqChatLinkRepositoryTest.removeTest();
        }

        @Test
        void findAllTest() {
            jooqChatLinkRepositoryTest.findAllTest();
        }

        @Test
        void findLinksByTgChatIdTest() {
            jooqChatLinkRepositoryTest.findLinksByTgChatIdTest();
        }

        @Test
        void findTgChatIdsTest() {
            jooqChatLinkRepositoryTest.findTgChatIdsTest();
        }
    }

    @Nested
    class JooqChatRepositoryTest {
        private final ChatRepositoryTest jooqChatRepositoryTest = new ChatRepositoryTest(jooqChatRepository);

        @Test
        void addTest() {
            jooqChatRepositoryTest.addTest();
        }

        @Test
        void removeTest() {
            jooqChatRepositoryTest.removeTest();
        }

        @Test
        void findAllTest() {
            jooqChatRepositoryTest.findAllTest();
        }
    }

    @Nested
    class JooqGitHubResponseRepositoryTest {
        private final GitHubResponseRepositoryTest jooqGitHubResponseRepositoryTest =
            new GitHubResponseRepositoryTest(jooqGitHubResponseRepository, jooqLinkRepository);

        @Test
        void addAndFindAllTest() {
            jooqGitHubResponseRepositoryTest.addAndFindAllTest();
        }

        @Test
        void findByLinkIdTest() {
            jooqGitHubResponseRepositoryTest.findByLinkIdTest();
        }

        @Test
        void findAllTest() {
            jooqGitHubResponseRepositoryTest.updateTest();
        }
    }

    @Nested
    class JooqLinkRepositoryTest {
        private final LinkRepositoryTest jooqLinkRepositoryTest = new LinkRepositoryTest(jooqLinkRepository);

        @Test
        void addTest() {
            jooqLinkRepositoryTest.addTest();
        }

        @Test
        void removeTest() {
            jooqLinkRepositoryTest.removeTest();
        }

        @Test
        void findAllTest() {
            jooqLinkRepositoryTest.findAllTest();
        }

        @Test
        void findByUrlTest() {
            jooqLinkRepositoryTest.findByUrlTest();
        }
    }

    @Nested
    class JooqQuestionResponseRepositoryTest {
        private final QuestionResponseRepositoryTest jooqQuestionResponseRepositoryTest =
            new QuestionResponseRepositoryTest(jooqQuestionResponseRepository, jooqLinkRepository);

        @Test
        void addAndFindAllTest() {
            jooqQuestionResponseRepositoryTest.addAndFindAllTest();
        }

        @Test
        void findByLinkIdTest() {
            jooqQuestionResponseRepositoryTest.findByLinkIdTest();
        }

        @Test
        void findAllTest() {
            jooqQuestionResponseRepositoryTest.updateTest();
        }
    }
}

