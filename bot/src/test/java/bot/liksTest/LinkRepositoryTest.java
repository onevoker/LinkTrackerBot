package bot.liksTest;

import com.pengrad.telegrambot.model.User;
import edu.java.bot.links.Link;
import edu.java.bot.links.LinkRepository;
import java.net.URI;
import java.util.Set;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class LinkRepositoryTest {
    private LinkRepository links;
    private static final Long userId = 1L;
    private User user;
    private static final String GIT_HUB = "https://github.com";
    private static final String STACK = "https://stackoverflow.com";
    private static final String MY_GITHUB = "https://github.com/onevoker";
    private Link MY_GITHUB_LINK;
    private Link STACK_LINK;
    private Link GIT_HUB_LINK;

    @BeforeEach
    void setUp() {
        this.links = new LinkRepository();
        this.user = new User(userId);
        this.MY_GITHUB_LINK = new Link(userId, MY_GITHUB);
        this.STACK_LINK = new Link(userId, STACK);
        this.GIT_HUB_LINK = new Link(userId, GIT_HUB);
    }

    @Test
    void testIsInUserLinks() {
        links.addUserLink(GIT_HUB_LINK);
        links.addUserLink(STACK_LINK);

        assertAll(
            () -> assertThat(links.isInUserLinks(GIT_HUB_LINK)).isTrue(),
            () -> assertThat(links.isInUserLinks(STACK_LINK)).isTrue()
        );
    }

    @Test
    void testAddTheSameUserLink() {
        links.addUserLink(GIT_HUB_LINK);
        links.addUserLink(GIT_HUB_LINK);
        Set<URI> userLinks = links.getUserLinks(user);

        assertThat(userLinks.size()).isEqualTo(1);
    }

    @Test
    void testAddUserLink() {
        Link unnormalizeLink = new Link(userId, "https://github.com////////////////");
        links.addUserLink(GIT_HUB_LINK);
        links.addUserLink(unnormalizeLink);
        links.addUserLink(STACK_LINK);
        links.addUserLink(MY_GITHUB_LINK);
        Set<URI> userLinks = links.getUserLinks(user);

        assertThat(userLinks.size()).isEqualTo(3);
    }

    @Test
    void deleteUserLink() {
        links.addUserLink(GIT_HUB_LINK);
        links.addUserLink(STACK_LINK);

        links.deleteUserLink(GIT_HUB_LINK);
        links.deleteUserLink(GIT_HUB_LINK);

        assertAll(
            () -> assertThat(links.getUserLinks(user).size()).isEqualTo(1),
            () -> {
                links.deleteUserLink(STACK_LINK);
                assertThat(links.getUserLinks(user).size()).isEqualTo(0);
            }
        );
    }

    @SneakyThrows @Test
    void testGetUserLinks() {
        links.addUserLink(GIT_HUB_LINK);
        links.addUserLink(STACK_LINK);
        links.addUserLink(MY_GITHUB_LINK);

        Set<URI> expected = Set.of(
            new URI(GIT_HUB),
            new URI(STACK),
            new URI(MY_GITHUB)
        );

        assertThat(links.getUserLinks(user)).usingRecursiveComparison().ignoringCollectionOrder().isEqualTo(expected);
    }

    @Test
    void testIsNotInUserLinks() {
        links.addUserLink(GIT_HUB_LINK);
        assertThat(links.isInUserLinks(STACK_LINK)).isFalse();
    }

    @Test
    void testIsRegistered() {
        assertAll(
            () -> assertThat(links.isRegistered(user)).isFalse(),
            () -> assertThat(links.isRegistered(user)).isTrue()
        );
    }

    @Test
    void testIsNotRegistered() {
        assertThat(links.isRegistered(user)).isFalse();
    }
}
