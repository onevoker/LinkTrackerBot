package edu.java.scrapper.repositoriesTest;

import edu.java.scrapper.controllers.exceptions.LinkWasNotTrackedException;
import edu.java.scrapper.controllers.exceptions.LinkWasTrackedException;
import edu.java.scrapper.dto.response.LinkResponse;
import edu.java.scrapper.dto.response.ListLinksResponse;
import edu.java.scrapper.repositories.LinkResponseRepository;
import java.net.URI;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class LinkRepositoryTest {
    private LinkResponseRepository links;
    private static final long CHAT_ID = 1L;
    private static final URI GIT_HUB_BASE_URI = URI.create("https://github.com");
    private static final URI GIT_HUB_REPO_URI = URI.create("https://github.com/onevoker/Tkf");
    private static final URI MY_GITHUB_PROFILE_URI = URI.create("https://github.com/onevoker");
    private static final LinkResponse MY_GITHUB_PROFILE_Link = new LinkResponse(CHAT_ID, MY_GITHUB_PROFILE_URI);
    private static final LinkResponse GIT_HUB_REPO_LINK = new LinkResponse(CHAT_ID, GIT_HUB_REPO_URI);
    private static final LinkResponse GIT_HUB_BASE_LINK = new LinkResponse(CHAT_ID, GIT_HUB_BASE_URI);

    @BeforeEach
    void setUp() {
        this.links = new LinkResponseRepository();
    }

    @Test
    void testAddUserLink() {
        links.addUserLink(GIT_HUB_BASE_LINK);

        assertThat(links.getUserLinks(CHAT_ID).size()).isEqualTo(1);
    }

    @Test
    void testAddTheSameUserLink() {
        links.addUserLink(GIT_HUB_BASE_LINK);

        var exception = assertThrows(LinkWasTrackedException.class, () -> links.addUserLink(GIT_HUB_BASE_LINK));
        assertThat(exception.getMessage()).isEqualTo("Ссылка уже добавлена, для просмотра ссылок введите /list");
    }

    @Test
    void deleteUserLink() {
        links.addUserLink(GIT_HUB_BASE_LINK);
        links.addUserLink(GIT_HUB_REPO_LINK);

        links.deleteUserLink(GIT_HUB_BASE_LINK);

        assertAll(
            () -> assertThat(links.getUserLinks(CHAT_ID).size()).isEqualTo(1),
            () -> {
                links.deleteUserLink(GIT_HUB_REPO_LINK);
                assertThat(links.getUserLinks(CHAT_ID).size()).isEqualTo(0);
            }
        );
    }

    @Test
    void deleteNotInRepositoryLink() {
        var exception = assertThrows(LinkWasNotTrackedException.class, () -> links.deleteUserLink(GIT_HUB_BASE_LINK));
        assertThat(exception.getMessage()).isEqualTo("Вы не отслеживаете данную ссылку");
    }

    @Test
    void testGetUserLinks() {
        links.addUserLink(GIT_HUB_BASE_LINK);
        links.addUserLink(GIT_HUB_REPO_LINK);
        links.addUserLink(MY_GITHUB_PROFILE_Link);

        List<LinkResponse> expectedList = List.of(
            new LinkResponse(CHAT_ID, GIT_HUB_BASE_URI),
            new LinkResponse(CHAT_ID, GIT_HUB_REPO_URI),
            new LinkResponse(CHAT_ID, MY_GITHUB_PROFILE_URI)
        );

        ListLinksResponse expected = new ListLinksResponse(expectedList, expectedList.size());

        assertThat(links.getUserLinks(CHAT_ID)).usingRecursiveComparison().ignoringCollectionOrder()
            .isEqualTo(expected);
    }
}
