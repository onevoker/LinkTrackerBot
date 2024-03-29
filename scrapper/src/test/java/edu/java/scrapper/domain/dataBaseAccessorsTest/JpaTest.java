package edu.java.scrapper.domain.dataBaseAccessorsTest;

import edu.java.scrapper.domain.repositories.jpa.JpaChatLinkRepository;
import edu.java.scrapper.domain.repositories.jpa.JpaChatRepository;
import edu.java.scrapper.domain.repositories.jpa.JpaGitHubResponseRepository;
import edu.java.scrapper.domain.repositories.jpa.JpaLinkRepository;
import edu.java.scrapper.domain.repositories.jpa.JpaQuestionResponseRepository;
import edu.java.scrapper.domain.repositoriesTest.ChatLinkRepositoryTest;
import edu.java.scrapper.domain.repositoriesTest.ChatRepositoryTest;
import edu.java.scrapper.domain.repositoriesTest.GitHubResponseRepositoryTest;
import edu.java.scrapper.domain.repositoriesTest.LinkRepositoryTest;
import edu.java.scrapper.domain.repositoriesTest.QuestionResponseRepositoryTest;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

@TestPropertySource(properties = "app.database-access-type=jpa")
@SpringBootTest
@Transactional
public class JpaTest {
    @Autowired
    private JpaChatLinkRepository jpaChatLinkRepository;
    @Autowired
    private JpaChatRepository jpaChatRepository;
    @Autowired
    private JpaGitHubResponseRepository jpaGitHubResponseRepository;
    @Autowired
    private JpaLinkRepository jpaLinkRepository;
    @Autowired
    private JpaQuestionResponseRepository jpaQuestionResponseRepository;

    @Nested
    class JpaChatLinkRepositoryTest {
        private final ChatLinkRepositoryTest jpaChatLinkRepositoryTest =
            new ChatLinkRepositoryTest(jpaChatLinkRepository, jpaChatRepository, jpaLinkRepository);

        @Test
        void addTest() {
            jpaChatLinkRepositoryTest.addTest();
        }

        @Test
        void removeTest() {
            jpaChatLinkRepositoryTest.removeTest();
        }

        @Test
        void findAllTest() {
            jpaChatLinkRepositoryTest.findAllTest();
        }

        @Test
        void findLinksByTgChatIdTest() {
            jpaChatLinkRepositoryTest.findLinksByTgChatIdTest();
        }

        @Test
        void findTgChatIdsTest() {
            jpaChatLinkRepositoryTest.findTgChatIdsTest();
        }
    }

    @Nested
    class JpaChatRepositoryTest {
        private final ChatRepositoryTest jpaChatRepositoryTest = new ChatRepositoryTest(jpaChatRepository);

        @Test
        void addTest() {
            jpaChatRepositoryTest.addTest();
        }

        @Test
        void removeTest() {
            jpaChatRepositoryTest.removeTest();
        }

        @Test
        void findAllTest() {
            jpaChatRepositoryTest.findAllTest();
        }
    }

    @Nested
    class JpaGitHubResponseRepositoryTest {
        private final GitHubResponseRepositoryTest jpaGitHubResponseRepositoryTest =
            new GitHubResponseRepositoryTest(jpaGitHubResponseRepository, jpaLinkRepository);

        @Test
        void addAndFindAllTest() {
            jpaGitHubResponseRepositoryTest.addAndFindAllTest();
        }

        @Test
        void findByLinkIdTest() {
            jpaGitHubResponseRepositoryTest.findByLinkIdTest();
        }

        @Test
        void findAllTest() {
            jpaGitHubResponseRepositoryTest.updateTest();
        }
    }

    @Nested
    class JpaLinkRepositoryTest {
        private final LinkRepositoryTest jpaLinkRepositoryTest = new LinkRepositoryTest(jpaLinkRepository);

        @Test
        void addTest() {
            jpaLinkRepositoryTest.addTest();
        }

        @Test
        void removeTest() {
            jpaLinkRepositoryTest.removeTest();
        }

        @Test
        void findAllTest() {
            jpaLinkRepositoryTest.findAllTest();
        }

        @Test
        void findByUrlTest() {
            jpaLinkRepositoryTest.findByUrlTest();
        }
    }

    @Nested
    class JpaQuestionResponseRepositoryTest {
        private final QuestionResponseRepositoryTest jpaQuestionResponseRepositoryTest =
            new QuestionResponseRepositoryTest(jpaQuestionResponseRepository, jpaLinkRepository);

        @Test
        void addAndFindAllTest() {
            jpaQuestionResponseRepositoryTest.addAndFindAllTest();
        }

        @Test
        void findByLinkIdTest() {
            jpaQuestionResponseRepositoryTest.findByLinkIdTest();
        }

        @Test
        void findAllTest() {
            jpaQuestionResponseRepositoryTest.updateTest();
        }
    }
}
