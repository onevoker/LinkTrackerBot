package bot.liksTest;

import com.pengrad.telegrambot.model.User;
import edu.java.bot.links.LinksRepository;
import java.net.URI;
import java.util.Set;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class LinksRepositoryTest {
    private LinksRepository links;
    private User user;
    private static final String GIT_HUB_LINK = "https://github.com";
    private static final String STACK_LINK = "https://stackoverflow.com";
    private static final String MY_GITHUB_LINK = "https://github.com/onevoker";

    @BeforeEach
    void setUp() {
        this.links = new LinksRepository();
        user = new User(1L);
    }

    @Test
    void testIsInUserLinks() {
        links.addUserLink(user, GIT_HUB_LINK);
        links.addUserLink(user, STACK_LINK);

        assertAll(
            () -> assertThat(links.isInUserLinks(user, GIT_HUB_LINK)).isTrue(),
            () -> assertThat(links.isInUserLinks(user, STACK_LINK)).isTrue()
        );
    }

    @Test
    void testAddTheSameUserLink() {
        links.addUserLink(user, GIT_HUB_LINK);
        links.addUserLink(user, GIT_HUB_LINK);
        Set<URI> userLinks = links.getUserLinks(user);

        assertThat(userLinks.size()).isEqualTo(1);
    }

    @Test
    void testAddUserLink() {
        links.addUserLink(user, GIT_HUB_LINK);
        links.addUserLink(user, "https://github.com////////////////");
        links.addUserLink(user, STACK_LINK);
        links.addUserLink(user, MY_GITHUB_LINK);
        Set<URI> userLinks = links.getUserLinks(user);

        assertThat(userLinks.size()).isEqualTo(3);
    }

    @Test
    void deleteUserLink() {
        links.addUserLink(user, GIT_HUB_LINK);
        links.addUserLink(user, STACK_LINK);

        links.deleteUserLink(user, GIT_HUB_LINK);
        links.deleteUserLink(user, GIT_HUB_LINK);

        assertAll(
            () -> assertThat(links.getUserLinks(user).size()).isEqualTo(1),
            () -> {
                links.deleteUserLink(user, STACK_LINK);
                assertThat(links.getUserLinks(user).size()).isEqualTo(0);
            }
        );
    }

    @SneakyThrows @Test
    void testGetUserLinks() {
        links.addUserLink(user, GIT_HUB_LINK);
        links.addUserLink(user, STACK_LINK);
        links.addUserLink(user, MY_GITHUB_LINK);

        Set<URI> expected = Set.of(
            new URI(GIT_HUB_LINK),
            new URI(STACK_LINK),
            new URI(MY_GITHUB_LINK)
        );

        assertThat(links.getUserLinks(user)).usingRecursiveComparison().ignoringCollectionOrder().isEqualTo(expected);
    }

    @Test
    void testIsNotInUserLinks() {
        links.addUserLink(user, GIT_HUB_LINK);
        assertThat(links.isInUserLinks(user, STACK_LINK)).isFalse();
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
