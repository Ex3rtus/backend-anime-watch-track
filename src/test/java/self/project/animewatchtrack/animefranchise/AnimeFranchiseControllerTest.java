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
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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

    @Test
    void itShouldRetrieveListOfAnimeFranchises() throws Exception {
        AnimeFranchiseDTO franchise1 = AnimeFranchiseDTO.builder()
                .id(UUID.randomUUID().toString())
                .franchiseTitle("Franchise To Get 1")
                .hasBeenWatched(false)
                .build();

        AnimeFranchiseDTO franchise2 = AnimeFranchiseDTO.builder()
                .id(UUID.randomUUID().toString())
                .franchiseTitle("Franchise To Get 2")
                .hasBeenWatched(true)
                .build();

        List<AnimeFranchiseDTO> expectedDTOList = List.of(franchise1, franchise2);

        when(mockedService.getAll()).thenReturn(expectedDTOList);

        mockMvc.perform(get(API + V1 + ANIME_FRANCHISE))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(equalTo(2))))
                .andExpect(jsonPath("$[0].id", equalTo(franchise1.getId())))
                .andExpect(jsonPath("$[0].franchiseTitle", equalTo(franchise1.getFranchiseTitle())))
                .andExpect(jsonPath("$[0].hasBeenWatched", equalTo(franchise1.isHasBeenWatched())))
                .andExpect(jsonPath("$[1].id", equalTo(franchise2.getId())))
                .andExpect(jsonPath("$[1].franchiseTitle", equalTo(franchise2.getFranchiseTitle())))
                .andExpect(jsonPath("$[1].hasBeenWatched", equalTo(franchise2.isHasBeenWatched())));
    }

    @Test
    void itShouldRetrieveFranchiseById() throws Exception {
        String id = UUID.randomUUID().toString();
        AnimeFranchiseDTO animeFranchiseDTO = AnimeFranchiseDTO.builder()
                .id(id)
                .franchiseTitle("Franchise Name")
                .hasBeenWatched(true)
                .build();

        String responsePayload = jsonMapper.writeValueAsString(animeFranchiseDTO);

        when(mockedService.getById(id))
                .thenReturn(animeFranchiseDTO);

        mockMvc.perform(get(API + V1 + ANIME_FRANCHISE + "/{id}", id)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(responsePayload));
    }

    @Test
    void itShouldAddNewAnimeFranchise() throws Exception {

        AnimeFranchiseCommand animeFranchiseCommand = new AnimeFranchiseCommand("Should Add", false);
        AnimeFranchise animeFranchise = AnimeFranchiseMapper.mapToEntity(animeFranchiseCommand);
        animeFranchise.setId(UUID.randomUUID().toString());

        String requestPayload = jsonMapper.writeValueAsString(animeFranchiseCommand);

        when(mockedService.addAnimeFranchise(any(AnimeFranchiseCommand.class)))
                .thenReturn(animeFranchise);
        String expected = animeFranchise.getId();

        mockMvc.perform(post(API + V1 + ANIME_FRANCHISE)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestPayload))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(expected));
    }

    @Test
    void itShouldUpdateExisting() throws Exception {

        String franchiseId = UUID.randomUUID().toString();
        String updatedFranchiseTitle = "Updated Franchise Title";
        boolean hasBeenWatched = true;
        AnimeFranchiseDTO updatedFranchiseDTO = AnimeFranchiseDTO.builder()
                .id(franchiseId)
                .franchiseTitle(updatedFranchiseTitle)
                .hasBeenWatched(hasBeenWatched)
                .build();
        String responsePayload = jsonMapper.writeValueAsString(updatedFranchiseDTO);

        when(mockedService.updateFranchise(franchiseId, updatedFranchiseTitle, hasBeenWatched))
                .thenReturn(updatedFranchiseDTO);

        mockMvc.perform(patch(API + V1 + ANIME_FRANCHISE + "/{franchiseId}", franchiseId)
                .param("franchiseTitle", updatedFranchiseTitle)
                .param("hasBeenWatched", "true"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(responsePayload));
    }
}