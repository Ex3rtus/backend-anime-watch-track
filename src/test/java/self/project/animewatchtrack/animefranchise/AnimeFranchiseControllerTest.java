package self.project.animewatchtrack.animefranchise;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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

    private static AnimeFranchiseDTO franchiseDTO1;

    private static AnimeFranchiseDTO franchiseDTO2;

    private static AnimeFranchiseCommand franchiseCommand;

    @BeforeAll
    static void setup() {
        franchiseDTO1 = AnimeFranchiseDTO.builder()
                .id(UUID.randomUUID().toString())
                .franchiseTitle("Franchise 1")
                .hasBeenWatched(false)
                .build();

        franchiseDTO2 = AnimeFranchiseDTO.builder()
                .id(UUID.randomUUID().toString())
                .franchiseTitle("Franchise 2")
                .hasBeenWatched(true)
                .build();

        franchiseCommand = new AnimeFranchiseCommand("Should Add", false);
    }

    @Test
    void itShouldRetrieveListOfAnimeFranchises() throws Exception {
        List<AnimeFranchiseDTO> expectedDTOList = List.of(franchiseDTO1, franchiseDTO2);

        when(mockedService.getAll()).thenReturn(expectedDTOList);

        mockMvc.perform(get(API + V1 + ANIME_FRANCHISES))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(equalTo(2))))
                .andExpect(jsonPath("$[0].id", equalTo(franchiseDTO1.getId())))
                .andExpect(jsonPath("$[0].franchiseTitle", equalTo(franchiseDTO1.getFranchiseTitle())))
                .andExpect(jsonPath("$[0].hasBeenWatched", equalTo(franchiseDTO1.getHasBeenWatched())))
                .andExpect(jsonPath("$[1].id", equalTo(franchiseDTO2.getId())))
                .andExpect(jsonPath("$[1].franchiseTitle", equalTo(franchiseDTO2.getFranchiseTitle())))
                .andExpect(jsonPath("$[1].hasBeenWatched", equalTo(franchiseDTO2.getHasBeenWatched())));

        verify(mockedService).getAll();
    }

    @Test
    void itShouldRetrieveFranchiseById() throws Exception {
        String id = franchiseDTO1.getId();
        String responsePayload = jsonMapper.writeValueAsString(franchiseDTO1);

        when(mockedService.getById(id))
                .thenReturn(franchiseDTO1);

        mockMvc.perform(get(API + V1 + ANIME_FRANCHISES + "/{id}", id)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(responsePayload));

        verify(mockedService).getById(id);
    }

    @Test
    void itShouldAddNewAnimeFranchise() throws Exception {
        String expected = UUID.randomUUID().toString();
        String requestPayload = jsonMapper.writeValueAsString(franchiseCommand);

        when(mockedService.addAnimeFranchise(any(AnimeFranchiseCommand.class)))
                .thenReturn(expected);

        mockMvc.perform(post(API + V1 + ANIME_FRANCHISES)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestPayload))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().string(expected));
    }

    @Test
    void itShouldUpdateExistingAnimeFranchise() throws Exception {
        String franchiseId = franchiseDTO1.getId();
        String updatedFranchiseTitle = "New Franchise 1 Title";
        Boolean hasBeenWatched = true;
        AnimeFranchiseDTO updatedFranchiseDTO = AnimeFranchiseDTO.builder()
                .id(franchiseId)
                .franchiseTitle(updatedFranchiseTitle)
                .hasBeenWatched(hasBeenWatched)
                .build();

        String responsePayload = jsonMapper.writeValueAsString(updatedFranchiseDTO);

        when(mockedService.updateFranchise(franchiseId, updatedFranchiseTitle, hasBeenWatched))
                .thenReturn(updatedFranchiseDTO);

        mockMvc.perform(patch(API + V1 + ANIME_FRANCHISES + "/{franchiseId}", franchiseId)
                .param("franchiseTitle", updatedFranchiseTitle)
                .param("hasBeenWatched", "true"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(responsePayload));
        verify(mockedService).updateFranchise(franchiseId, updatedFranchiseTitle, hasBeenWatched);
    }

    @Test
    void itShouldDeleteAnimeFranchiseById() throws Exception {
        String franchiseId = franchiseDTO2.getId();

        mockMvc.perform(delete(API + V1 + ANIME_FRANCHISES + "/{franchiseId}", franchiseId))
                .andDo(print())
                .andExpect(status().isOk());
        verify(mockedService).deleteAnimeFranchise(franchiseId);
    }
}