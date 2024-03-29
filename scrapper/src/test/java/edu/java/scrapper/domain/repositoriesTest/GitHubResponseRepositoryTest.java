package edu.java.scrapper.domain.repositoriesTest;

import edu.java.scrapper.IntegrationTest;
import edu.java.scrapper.domain.models.Link;
import edu.java.scrapper.domain.repositories.interfaces.GitHubResponseRepository;
import edu.java.scrapper.domain.repositories.interfaces.LinkRepository;
import edu.java.scrapper.dto.gitHubDto.RepositoryResponse;
import java.net.URI;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import lombok.RequiredArgsConstructor;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RequiredArgsConstructor
public class GitHubResponseRepositoryTest extends IntegrationTest {
    private final GitHubResponseRepository gitHubResponseRepository;
    private final LinkRepository linkRepository;

    private static final Link LINK =
        new Link(
            URI.create("https://github.com/onevoker"),
            OffsetDateTime.now(ZoneOffset.UTC),
            OffsetDateTime.now(ZoneOffset.UTC)
        );
    private static final long REPO_ID = 132412412L;
    private static final RepositoryResponse RESPONSE = new RepositoryResponse(
        REPO_ID,
        OffsetDateTime.of(
            2024, 3, 14, 12, 13, 20, 0, ZoneOffset.UTC
        )
    );
    private Long linkId;

    private void setUpRepos() {
        linkRepository.add(LINK);
        linkId = linkRepository.findAll().getFirst().getId();
    }

    public void addAndFindAllTest() {
        setUpRepos();
        gitHubResponseRepository.add(RESPONSE, linkId);

        assertThat(gitHubResponseRepository.findAll().getFirst().getId()).isEqualTo(REPO_ID);
    }

    public void findByLinkIdTest() {
        setUpRepos();
        gitHubResponseRepository.add(RESPONSE, linkId);

        List<RepositoryResponse> result = gitHubResponseRepository.findByLinkId(linkId);

        assertThat(result.size()).isEqualTo(1);
    }

    public void updateTest() {
        setUpRepos();
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
