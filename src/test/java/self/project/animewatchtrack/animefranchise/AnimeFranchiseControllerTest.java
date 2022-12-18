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
import org.springframework.web.bind.MethodArgumentNotValidException;

import javax.validation.ConstraintViolationException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static self.project.animewatchtrack.constants.FranchiseValidationMessages.isWatchedMessage;
import static self.project.animewatchtrack.constants.FranchiseValidationMessages.titleMessage;
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
    private static AnimeFranchiseCommand invalidCommand;
    private final static String BASE_URI = API + V1 + ANIME_FRANCHISES;

    @BeforeAll
    static void setup() {
        franchiseDTO1 = AnimeFranchiseDTO.builder()
                .id(UUID.randomUUID().toString())
                .franchiseTitle("Franchise 1")
                .isWatched(false)
                .build();

        franchiseDTO2 = AnimeFranchiseDTO.builder()
                .id(UUID.randomUUID().toString())
                .franchiseTitle("Franchise 2")
                .isWatched(true)
                .build();

        franchiseCommand = new AnimeFranchiseCommand("Should Add", false);
        invalidCommand = new AnimeFranchiseCommand(" ", null);
    }

    @Test
    void itShouldRetrieveListOfAnimeFranchises() throws Exception {
        List<AnimeFranchiseDTO> expectedDTOList = List.of(franchiseDTO1, franchiseDTO2);

        when(mockedService.getAll()).thenReturn(expectedDTOList);

        mockMvc.perform(get(BASE_URI))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(equalTo(2))))
                .andExpect(jsonPath("$[0].id", equalTo(franchiseDTO1.getId())))
                .andExpect(jsonPath("$[0].franchiseTitle", equalTo(franchiseDTO1.getFranchiseTitle())))
                .andExpect(jsonPath("$[0].isWatched", equalTo(franchiseDTO1.getIsWatched())))
                .andExpect(jsonPath("$[1].id", equalTo(franchiseDTO2.getId())))
                .andExpect(jsonPath("$[1].franchiseTitle", equalTo(franchiseDTO2.getFranchiseTitle())))
                .andExpect(jsonPath("$[1].isWatched", equalTo(franchiseDTO2.getIsWatched())));

        verify(mockedService).getAll();
    }

    @Test
    void itShouldRetrieveFranchiseById() throws Exception {
        String id = franchiseDTO1.getId();
        String responsePayload = jsonMapper.writeValueAsString(franchiseDTO1);

        when(mockedService.getById(id))
                .thenReturn(franchiseDTO1);

        mockMvc.perform(get(BASE_URI + "/{franchiseId}", id)
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

        mockMvc.perform(post(BASE_URI)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestPayload))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().string(expected));
    }

    @Test
    void itShouldFailToCreateFranchiseGivenInvalidateFranchiseCommand() throws Exception {
        String requestPayload = jsonMapper.writeValueAsString(invalidCommand);

        mockMvc.perform(post(BASE_URI)
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestPayload))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException))
                .andExpect(result -> result.getResolvedException().getMessage().equals(titleMessage));
    }
    @Test
    void itShouldUpdateExistingAnimeFranchise() throws Exception {
        String franchiseId = franchiseDTO1.getId();
        String updatedFranchiseTitle = "New Franchise 1 Title";
        AnimeFranchiseDTO updatedFranchiseDTO = AnimeFranchiseDTO.builder()
                .id(franchiseId)
                .franchiseTitle(updatedFranchiseTitle)
                .isWatched(franchiseDTO1.getIsWatched())
                .build();

        String responsePayload = jsonMapper.writeValueAsString(updatedFranchiseDTO);

        when(mockedService.updateFranchise(franchiseId, updatedFranchiseTitle))
                .thenReturn(updatedFranchiseDTO);

        mockMvc.perform(patch(BASE_URI + "/{franchiseId}", franchiseId)
                .param("franchiseTitle", updatedFranchiseTitle))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(responsePayload));
        verify(mockedService).updateFranchise(franchiseId, updatedFranchiseTitle);
    }

    @Test
    void itShouldFailToUpdateFranchiseTitle() {
        String franchiseId = franchiseDTO1.getId();
        String invalidTitle = "    ";

        assertThatThrownBy(() -> mockMvc.perform(patch(BASE_URI + "/{franchiseId}", franchiseId)
                        .param("franchiseTitle", invalidTitle))
                .andDo(print())
                .andExpect(status().isBadRequest()))
                .hasMessageContaining(titleMessage);
    }

    @Test
    void itShouldMarkExistingAnimeFranchiseAsNotWatched() throws Exception {
        String franchiseId = franchiseDTO1.getId();

        AnimeFranchiseDTO expectedDTO = AnimeFranchiseDTO.builder()
                .id(franchiseId)
                .franchiseTitle(franchiseDTO1.getFranchiseTitle())
                .isWatched(true)
                .build();

        String expectedPayload = jsonMapper.writeValueAsString(expectedDTO);

        when(mockedService.markFranchise(franchiseId, true))
                .thenReturn(expectedDTO);

        mockMvc.perform(patch(BASE_URI + "/{franchiseId}" + MARK, franchiseId)
                        .param("isWatched", expectedDTO.getIsWatched().toString()))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedPayload));

        verify(mockedService).markFranchise(franchiseId, true);
    }

    @Test
    void itShouldMarkExistingAnimeFranchiseAsWatched() throws Exception {
        String franchiseId = franchiseDTO2.getId();

        AnimeFranchiseDTO expectedDTO = AnimeFranchiseDTO.builder()
                .id(franchiseId)
                .franchiseTitle(franchiseDTO2.getFranchiseTitle())
                .isWatched(false)
                .build();

        String expectedPayload = jsonMapper.writeValueAsString(expectedDTO);

        when(mockedService.markFranchise(franchiseId, false))
                .thenReturn(expectedDTO);

        mockMvc.perform(patch(BASE_URI + "/{franchiseId}" + MARK, franchiseId)
                        .param("isWatched", expectedDTO.getIsWatched().toString()))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedPayload));

        verify(mockedService).markFranchise(franchiseId, false);
    }

    @Test
    void itShouldDeleteAnimeFranchiseById() throws Exception {
        String franchiseId = franchiseDTO2.getId();

        mockMvc.perform(delete(BASE_URI + "/{franchiseId}", franchiseId))
                .andDo(print())
                .andExpect(status().isNoContent());
        verify(mockedService).deleteAnimeFranchise(franchiseId);
    }
}