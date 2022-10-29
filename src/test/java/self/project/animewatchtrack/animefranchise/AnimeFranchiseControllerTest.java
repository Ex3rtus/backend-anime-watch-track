package self.project.animewatchtrack.animefranchise;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static self.project.animewatchtrack.constants.ResourcePaths.*;

/**
 * @author Youssef Kaïdi.
 * created 28 oct. 2022.
 */

@AutoConfigureMockMvc
@WebMvcTest(AnimeFranchiseController.class)
class AnimeFranchiseControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private AnimeFranchiseServiceImpl mockedService;

    private final ObjectMapper jsonMapper = new ObjectMapper();

    @Test
    void itShouldAddNewAnimeFranchise() throws Exception {

        AnimeFranchiseCommand animeFranchiseCommand = new AnimeFranchiseCommand("Should Add", false);
        AnimeFranchise animeFranchise = AnimeFranchiseMapper.mapToEntity(animeFranchiseCommand);
        animeFranchise.setId(UUID.randomUUID().toString());

        String requestPayload = jsonMapper.writeValueAsString(animeFranchiseCommand);

        when(mockedService.addAnimeFranchise(any(AnimeFranchiseCommand.class))).thenReturn(animeFranchise);
        String expected = animeFranchise.getId();

        mockMvc.perform(post(API + V1 + ANIME_FRANCHISE)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestPayload))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(expected));
    }
}