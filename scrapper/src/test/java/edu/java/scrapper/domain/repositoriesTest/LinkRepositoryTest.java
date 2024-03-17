package edu.java.scrapper.domain.repositoriesTest;

import edu.java.scrapper.IntegrationTest;
import edu.java.scrapper.domain.models.Link;
import edu.java.scrapper.domain.repositories.interfaces.LinkRepository;
import java.net.URI;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class LinkRepositoryTest extends IntegrationTest {
    @Autowired
    private LinkRepository linkRepository;
    private static final Link LINK =
        new Link(
            URI.create("https://github.com/onevoker"),
            OffsetDateTime.now().with(ZoneOffset.UTC),
            OffsetDateTime.now().with(ZoneOffset.UTC)
        );

    @Test
    @Transactional
    @Rollback
    void addTest() {
        linkRepository.add(LINK);
        assertThat(linkRepository.findAll().size()).isEqualTo(1);
        assertDoesNotThrow(() -> linkRepository.add(LINK));
    }

    @Test
    @Transactional
    @Rollback
    void removeTest() {
        linkRepository.add(LINK);
        assertThat(linkRepository.findAll().size()).isEqualTo(1);
        linkRepository.remove(linkRepository.findAll().getFirst().getId());
        assertThat(linkRepository.findAll().size()).isEqualTo(0);
    }

    @Test
    @Transactional
    @Rollback
    void findAllTest() {
        linkRepository.add(LINK);
        List<Link> links = linkRepository.findAll();
        Link expected = new Link(
            LINK.getUrl(),
            LINK.getLastUpdate(),
            LINK.getLastApiCheck()
        );
        Link result = links.getFirst();

        assertThat(result.getUrl()).isEqualTo(expected.getUrl());
        assertThat(links.size()).isEqualTo(1);
    }

    @Test
    @Transactional
    @Rollback
    void findByUrlTest() {
        linkRepository.add(LINK);
        URI addedUrl = URI.create("https://github.com/onevoker/LinkTrackerBot");
        Link oneMoreLink = new Link(
            addedUrl,
            OffsetDateTime.now().with(ZoneOffset.UTC),
            OffsetDateTime.now().with(ZoneOffset.UTC)
        );
        linkRepository.add(oneMoreLink);

        List<Link> result = linkRepository.findByUrl(addedUrl);
        assertAll(
            () -> assertThat(result.size()).isEqualTo(1),
            () -> assertThat(result.getFirst().getUrl()).isEqualTo(addedUrl),
            () -> assertThat(linkRepository.findByUrl(URI.create("https://notAddedUrl.com"))).isEqualTo(List.of())
        );
    }
}
