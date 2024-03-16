package edu.java.scrapper.domain.jdbcRepositoriesTest;

import edu.java.scrapper.IntegrationTest;
import edu.java.scrapper.domain.repositories.jdbc.JdbcGitHubResponseRepository;
import edu.java.scrapper.domain.repositories.jdbc.JdbcLinkRepository;
import edu.java.scrapper.domain.models.Link;
import edu.java.scrapper.domain.repositories.interfaces.GitHubResponseRepository;
import edu.java.scrapper.domain.repositories.interfaces.LinkRepository;
import edu.java.scrapper.dto.gitHubDto.RepositoryResponse;
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

@SpringBootTest(classes = {JdbcGitHubResponseRepository.class, JdbcLinkRepository.class})
@EnableAutoConfiguration(exclude = LiquibaseAutoConfiguration.class)
public class JdbcGitHubResponseRepositoryTest extends IntegrationTest {
    @Autowired
    private GitHubResponseRepository gitHubResponseRepository;
    @Autowired
    private LinkRepository linkRepository;

    private static final Link LINK =
        new Link(
            URI.create("https://github.com/onevoker"),
            OffsetDateTime.now().with(ZoneOffset.UTC),
            OffsetDateTime.now().with(ZoneOffset.UTC)
        );
    private static final long REPO_ID = 132412412L;
    private static final RepositoryResponse RESPONSE = new RepositoryResponse(
        REPO_ID,
        OffsetDateTime.of(
            2024, 3, 14, 12, 13, 20, 0, ZoneOffset.UTC
        )
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
        gitHubResponseRepository.add(RESPONSE, linkId);
        assertThat(gitHubResponseRepository.findAll().getFirst()).isEqualTo(RESPONSE);
    }

    @Test
    @Transactional
    @Rollback
    void findByLinkIdTest() {
        gitHubResponseRepository.add(RESPONSE, linkId);

        RepositoryResponse result = gitHubResponseRepository.findByLinkId(linkId).getFirst();

        assertThat(result).isEqualTo(RESPONSE);
    }

    @Test
    @Transactional
    @Rollback
    void updateTest() {
        gitHubResponseRepository.add(RESPONSE, linkId);
        OffsetDateTime timeOfUpdate = OffsetDateTime.of(
            2025, 1, 1, 1, 1, 1, 0, ZoneOffset.UTC
        );
        RepositoryResponse newResponse = new RepositoryResponse(REPO_ID, timeOfUpdate);
        gitHubResponseRepository.update(newResponse, linkId);

        OffsetDateTime timeInRepo = gitHubResponseRepository.findByLinkId(linkId).getFirst().getPushedAt();

        assertThat(timeInRepo).isEqualTo(timeOfUpdate);
    }

}
