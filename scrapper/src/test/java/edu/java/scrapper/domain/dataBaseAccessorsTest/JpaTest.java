package edu.java.scrapper.domain.dataBaseAccessorsTest;

import edu.java.scrapper.IntegrationTest;
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
import jakarta.annotation.PostConstruct;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
public class JpaTest extends IntegrationTest {
    @Autowired
    private ChatLinkRepository jpaChatLinkRepository;
    @Autowired
    private ChatRepository jpaChatRepository;
    @Autowired
    private GitHubResponseRepository jpaGitHubResponseRepository;
    @Autowired
    private LinkRepository jpaLinkRepository;
    @Autowired
    private QuestionResponseRepository jpaQuestionResponseRepository;

    private ChatLinkRepositoryTest jpaChatLinkRepositoryTest;
    private ChatRepositoryTest jpaChatRepositoryTest;
    private GitHubResponseRepositoryTest jpaGitHubResponseRepositoryTest;
    private LinkRepositoryTest jpaLinkRepositoryTest;
    private QuestionResponseRepositoryTest jpaQuestionResponseRepositoryTest;

    @PostConstruct
    void setUpTests() {
        jpaChatLinkRepositoryTest =
            new ChatLinkRepositoryTest(jpaChatLinkRepository, jpaChatRepository, jpaLinkRepository);
        jpaChatRepositoryTest = new ChatRepositoryTest(jpaChatRepository);
        jpaGitHubResponseRepositoryTest =
            new GitHubResponseRepositoryTest(jpaGitHubResponseRepository, jpaLinkRepository);
        jpaLinkRepositoryTest = new LinkRepositoryTest(jpaLinkRepository);
        jpaQuestionResponseRepositoryTest =
            new QuestionResponseRepositoryTest(jpaQuestionResponseRepository, jpaLinkRepository);
    }

    @Nested
    @Transactional
    class JpaChatLinkRepositoryTest {

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
    @Transactional
    class JpaChatRepositoryTest {
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
    @Transactional
    class JpaGitHubResponseRepositoryTest {
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
    @Transactional
    class JpaLinkRepositoryTest {
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
    @Transactional
    class JpaQuestionResponseRepositoryTest {

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
