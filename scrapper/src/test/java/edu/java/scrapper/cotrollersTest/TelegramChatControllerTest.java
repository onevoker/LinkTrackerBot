package edu.java.scrapper.cotrollersTest;

import edu.java.scrapper.controllers.telegramConrollers.TelegramChatController;
import edu.java.scrapper.domain.services.interfaces.ChatService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class TelegramChatControllerTest {
    @Mock
    private ChatService chatService;

    @InjectMocks
    private TelegramChatController telegramChatController;

    private MockMvc mockMvc;
    private static final String ENDPOINT_PATH = "/tg-chat";

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(telegramChatController).build();
    }

    @Test
    public void testRegisterChat() throws Exception {
        long id = 1L;

        mockMvc.perform(post(ENDPOINT_PATH + "/" + id)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());

        verify(chatService).register(id);
    }

    @Test
    public void testUnregisterNotInPathChat() throws Exception {
        mockMvc.perform(delete(ENDPOINT_PATH)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }
}

