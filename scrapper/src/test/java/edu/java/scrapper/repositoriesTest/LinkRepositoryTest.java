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
    private static final URI GIT_HUB = URI.create("https://github.com");
    private static final URI GIT_HUB_TKF = URI.create("https://github.com/onevoker/Tkf");
    private static final URI MY_GITHUB = URI.create("https://github.com/onevoker");
    private LinkResponse MY_GITHUB_LINK;
    private LinkResponse GIT_HUB_TKF_LINK;
    private LinkResponse GIT_HUB_LINK;

    @BeforeEach
    void setUp() {
        this.links = new LinkResponseRepository();
        this.MY_GITHUB_LINK = new LinkResponse(CHAT_ID, MY_GITHUB);
        this.GIT_HUB_TKF_LINK = new LinkResponse(CHAT_ID, GIT_HUB_TKF);
        this.GIT_HUB_LINK = new LinkResponse(CHAT_ID, GIT_HUB);
    }

    @Test
    void testAddUserLink() {
        links.addUserLink(GIT_HUB_LINK);

        assertThat(links.getUserLinks(CHAT_ID).size()).isEqualTo(1);
    }
    @Test
    void testAddTheSameUserLink() {
        links.addUserLink(GIT_HUB_LINK);

        var exception = assertThrows(LinkWasTrackedException.class, () -> links.addUserLink(GIT_HUB_LINK));
        assertThat(exception.getMessage()).isEqualTo("Ссылка уже добавлена, для просмотра ссылок введите /list");
    }

    @Test
    void deleteUserLink() {
        links.addUserLink(GIT_HUB_LINK);
        links.addUserLink(GIT_HUB_TKF_LINK);

        links.deleteUserLink(GIT_HUB_LINK);

        assertAll(
            () -> assertThat(links.getUserLinks(CHAT_ID).size()).isEqualTo(1),
            () -> {
                links.deleteUserLink(GIT_HUB_TKF_LINK);
                assertThat(links.getUserLinks(CHAT_ID).size()).isEqualTo(0);
            }
        );
    }

    @Test
    void deleteNotInRepositoryLink() {
        var exception = assertThrows(LinkWasNotTrackedException.class, () -> links.deleteUserLink(GIT_HUB_LINK));
        assertThat(exception.getMessage()).isEqualTo("Вы не отслеживаете данную ссылку");
    }

    @Test
    void testGetUserLinks() {
        links.addUserLink(GIT_HUB_LINK);
        links.addUserLink(GIT_HUB_TKF_LINK);
        links.addUserLink(MY_GITHUB_LINK);

        List<LinkResponse> expectedList = List.of(
            new LinkResponse(CHAT_ID, GIT_HUB),
            new LinkResponse(CHAT_ID, GIT_HUB_TKF),
            new LinkResponse(CHAT_ID, MY_GITHUB)
        );

        ListLinksResponse expected = new ListLinksResponse(expectedList, expectedList.size());

        assertThat(links.getUserLinks(CHAT_ID)).usingRecursiveComparison().ignoringCollectionOrder()
            .isEqualTo(expected);
    }
}
