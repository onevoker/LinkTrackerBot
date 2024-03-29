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

@SpringBootTest(properties = "app.database-access-type=jdbc")
@Transactional
public class JdbcTest {
    @Autowired
    private ChatLinkRepository jdbcChatLinkRepository;
    @Autowired
    private ChatRepository jdbcChatRepository;
    @Autowired
    private GitHubResponseRepository jdbcGitHubResponseRepository;
    @Autowired
    private LinkRepository jdbcLinkRepository;
    @Autowired
    private QuestionResponseRepository jdbcQuestionResponseRepository;

    @Nested
    class JdbcChatLinkRepositoryTest {
        private final ChatLinkRepositoryTest jdbcChatLinkRepositoryTest =
            new ChatLinkRepositoryTest(jdbcChatLinkRepository, jdbcChatRepository, jdbcLinkRepository);

        @Test
        void addTest() {
            jdbcChatLinkRepositoryTest.addTest();
        }

        @Test
        void removeTest() {
            jdbcChatLinkRepositoryTest.removeTest();
        }

        @Test
        void findAllTest() {
            jdbcChatLinkRepositoryTest.findAllTest();
        }

        @Test
        void findLinksByTgChatIdTest() {
            jdbcChatLinkRepositoryTest.findLinksByTgChatIdTest();
        }

        @Test
        void findTgChatIdsTest() {
            jdbcChatLinkRepositoryTest.findTgChatIdsTest();
        }
    }

    @Nested
    class JdbcChatRepositoryTest {
        private final ChatRepositoryTest jdbcChatRepositoryTest = new ChatRepositoryTest(jdbcChatRepository);

        @Test
        void addTest() {
            jdbcChatRepositoryTest.addTest();
        }

        @Test
        void removeTest() {
            jdbcChatRepositoryTest.removeTest();
        }

        @Test
        void findAllTest() {
            jdbcChatRepositoryTest.findAllTest();
        }
    }

    @Nested
    class JdbcGitHubResponseRepositoryTest {
        private final GitHubResponseRepositoryTest jdbcGitHubResponseRepositoryTest =
            new GitHubResponseRepositoryTest(jdbcGitHubResponseRepository, jdbcLinkRepository);

        @Test
        void addAndFindAllTest() {
            jdbcGitHubResponseRepositoryTest.addAndFindAllTest();
        }

        @Test
        void findByLinkIdTest() {
            jdbcGitHubResponseRepositoryTest.findByLinkIdTest();
        }

        @Test
        void findAllTest() {
            jdbcGitHubResponseRepositoryTest.updateTest();
        }
    }

    @Nested
    class JdbcLinkRepositoryTest {
        private final LinkRepositoryTest jdbcLinkRepositoryTest = new LinkRepositoryTest(jdbcLinkRepository);

        @Test
        void addTest() {
            jdbcLinkRepositoryTest.addTest();
        }

        @Test
        void removeTest() {
            jdbcLinkRepositoryTest.removeTest();
        }

        @Test
        void findAllTest() {
            jdbcLinkRepositoryTest.findAllTest();
        }

        @Test
        void findByUrlTest() {
            jdbcLinkRepositoryTest.findByUrlTest();
        }
    }

    @Nested
    class JdbcQuestionResponseRepositoryTest {
        private final QuestionResponseRepositoryTest jdbcQuestionResponseRepositoryTest =
            new QuestionResponseRepositoryTest(jdbcQuestionResponseRepository, jdbcLinkRepository);

        @Test
        void addAndFindAllTest() {
            jdbcQuestionResponseRepositoryTest.addAndFindAllTest();
        }

        @Test
        void findByLinkIdTest() {
            jdbcQuestionResponseRepositoryTest.findByLinkIdTest();
        }

        @Test
        void findAllTest() {
            jdbcQuestionResponseRepositoryTest.updateTest();
        }
    }
}
