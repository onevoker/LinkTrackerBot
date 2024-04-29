package bot.contollersTest;

import dto.response.LinkUpdateResponse;
import edu.java.bot.updateHandlers.BotUpdaterService;
import edu.java.bot.updateHandlers.http.controllers.BotController;
import io.micrometer.core.instrument.Counter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class BotControllerTest {
    @Mock
    private BotUpdaterService botUpdaterService;
    @Mock
    private Counter messagesProcessedCounter;

    @InjectMocks
    private BotController botController;

    private MockMvc mockMvc;
    private static final String ENDPOINT_PATH = "/updates";

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(botController).build();
    }

    @Test
    public void testSendUpdate() throws Exception {
        mockMvc.perform(post(ENDPOINT_PATH)
                .content("""
                    {
                      "url": "https://test.com",
                      "description": "Появилось обновление",
                      "tgChatIds": [
                        1,
                        2,
                        3
                      ]
                    }""")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());

        verify(messagesProcessedCounter).increment();
        verify(botUpdaterService).sendUpdate(any(LinkUpdateResponse.class));
    }
}
