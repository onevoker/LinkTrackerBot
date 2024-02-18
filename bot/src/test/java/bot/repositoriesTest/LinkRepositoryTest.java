package bot.repositoriesTest;

import com.pengrad.telegrambot.model.User;
import edu.java.bot.links.Link;
import edu.java.bot.repositories.LinkRepository;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class LinkRepositoryTest {
    private LinkRepository links;
    private static final Long userId = 1L;
    private User user;
    private static final String GIT_HUB = "https://github.com";
    private static final String GIT_HUB_TKF = "https://github.com/onevoker/Tkf";
    private static final String MY_GITHUB = "https://github.com/onevoker";
    private Link MY_GITHUB_LINK;
    private Link GIT_HUB_TKF_LINK;
    private Link GIT_HUB_LINK;

    @BeforeEach
    void setUp() {
        this.links = new LinkRepository();
        this.user = new User(userId);
        this.MY_GITHUB_LINK = new Link(userId, MY_GITHUB);
        this.GIT_HUB_TKF_LINK = new Link(userId, GIT_HUB_TKF);
        this.GIT_HUB_LINK = new Link(userId, GIT_HUB);
    }

    @Test
    void testIsInUserLinks() {
        links.addUserLink(GIT_HUB_LINK);
        links.addUserLink(GIT_HUB_TKF_LINK);

        assertAll(
            () -> assertThat(links.isInUserLinks(GIT_HUB_LINK)).isTrue(),
            () -> assertThat(links.isInUserLinks(GIT_HUB_TKF_LINK)).isTrue()
        );
    }

    @Test
    void testAddTheSameUserLink() {
        links.addUserLink(GIT_HUB_LINK);
        links.addUserLink(GIT_HUB_LINK);
        Set<Link> userLinks = links.getUserLinks(user);

        assertThat(userLinks.size()).isEqualTo(1);
    }

    @Test
    void testAddUserLink() {
        String strLink = "https://github.com////////////////";
        Link unnormalizeLink = new Link(userId, strLink);
        links.addUserLink(GIT_HUB_LINK);
        links.addUserLink(unnormalizeLink);
        links.addUserLink(GIT_HUB_TKF_LINK);
        links.addUserLink(MY_GITHUB_LINK);
        Set<Link> userLinks = links.getUserLinks(user);

        assertThat(userLinks.size()).isEqualTo(3);
    }

    @Test
    void deleteUserLink() {
        links.addUserLink(GIT_HUB_LINK);
        links.addUserLink(GIT_HUB_TKF_LINK);

        links.deleteUserLink(GIT_HUB_LINK);
        links.deleteUserLink(GIT_HUB_LINK);

        assertAll(
            () -> assertThat(links.getUserLinks(user).size()).isEqualTo(1),
            () -> {
                links.deleteUserLink(GIT_HUB_TKF_LINK);
                assertThat(links.getUserLinks(user).size()).isEqualTo(0);
            }
        );
    }

    @Test
    void testGetUserLinks() {
        links.addUserLink(GIT_HUB_LINK);
        links.addUserLink(GIT_HUB_TKF_LINK);
        links.addUserLink(MY_GITHUB_LINK);

        Set<Link> expected = Set.of(
            new Link(userId, GIT_HUB),
            new Link(userId, GIT_HUB_TKF),
            new Link(userId, MY_GITHUB)
        );

        assertThat(links.getUserLinks(user)).usingRecursiveComparison().ignoringCollectionOrder().isEqualTo(expected);
    }

    @Test
    void testIsNotInUserLinks() {
        links.addUserLink(GIT_HUB_LINK);
        assertThat(links.isInUserLinks(GIT_HUB_TKF_LINK)).isFalse();
    }
}
