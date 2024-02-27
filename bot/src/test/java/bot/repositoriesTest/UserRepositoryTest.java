package bot.repositoriesTest;

import com.pengrad.telegrambot.model.User;
import edu.java.bot.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class UserRepositoryTest {
    private User user;
    private UserRepository users;

    @BeforeEach
    void setUpTest() {
        this.user = new User(1L);
        this.users = new UserRepository();
    }

    @Test
    void testIsRegistered() {
        assertAll(
            () -> assertThat(users.isRegistered(user)).isFalse(),
            () -> assertThat(users.isRegistered(user)).isTrue()
        );
    }

    @Test
    void testIsNotRegistered() {
        assertThat(users.isRegistered(user)).isFalse();
    }
}
