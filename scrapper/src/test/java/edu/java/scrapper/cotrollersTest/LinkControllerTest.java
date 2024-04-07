package edu.java.scrapper.cotrollersTest;

import edu.java.scrapper.controllers.telegramConrollers.LinkController;
import edu.java.scrapper.domain.services.interfaces.LinkService;
import java.net.URI;
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
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class LinkControllerTest {
    @Mock
    private LinkService linkService;

    @InjectMocks
    private LinkController linkController;

    private MockMvc mockMvc;
    private static final String HEADER = "Tg-Chat-Id";
    private static final String ENDPOINT_PATH = "/links";

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(linkController).build();
    }

    @Test
    public void testGetTrackedLinks() throws Exception {
        long chatId = 1L;
        mockMvc.perform(get(ENDPOINT_PATH)
                .header(HEADER, chatId)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());

        verify(linkService).listAll(chatId);
    }

    @Test
    public void testUncorrectTrackLink() throws Exception {
        long uncorrectChatId = -1L;
        int expectedStatus = 400;
        String url = "http://addLink.com";

        mockMvc.perform(post(ENDPOINT_PATH)
                .header(HEADER, uncorrectChatId)
                .content("{\"url\":\"%s\"}".formatted(url))
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().is(expectedStatus));

        verify(linkService, never()).add(anyLong(), any());
    }

    @Test
    public void testUntrackLink() throws Exception {
        String url = "http://removeLink.com";
        long chatId = 1L;

        mockMvc.perform(delete(ENDPOINT_PATH)
                .header(HEADER, chatId)
                .content("{\"url\":\"%s\"}".formatted(url))
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());

        verify(linkService).remove(eq(chatId), eq(URI.create(url)));
    }
}
