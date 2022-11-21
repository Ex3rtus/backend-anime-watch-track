package self.project.animewatchtrack.anime;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import self.project.animewatchtrack.animefranchise.AnimeFranchise;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static self.project.animewatchtrack.constants.ResourcePaths.*;

/**
 * @author Youssef Kaïdi.
 * created 06 nov. 2022.
 */

@AutoConfigureMockMvc
@WebMvcTest(AnimeController.class)
class AnimeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AnimeService mockedService;

    private final ObjectMapper jsonMapper = new ObjectMapper();

    private static AnimeFranchise parentFranchise;
    private static Anime anime1;
    private static AnimeDTO anime1DTO;
    private static Anime anime2;
    private static AnimeDTO anime2DTO;
    private static AnimeCommand animeCommand;

    @BeforeAll
    static void setup() {
        parentFranchise = new AnimeFranchise().toBuilder()
                .id(UUID.randomUUID().toString())
                .franchiseTitle("Parent Franchise Title")
                .hasBeenWatched(false)
                .build();

        anime1 = new Anime().toBuilder()
                .id(UUID.randomUUID().toString())
                .animeTitle("Anime 1 Title")
                .initialAirYear(1970)
                .originalMangaAuthors(List.of("Manga 1 Author 1"))
                .hasBeenWatched(false)
                .build();

        anime2 = new Anime().toBuilder()
                .id(UUID.randomUUID().toString())
                .animeTitle("Anime 2 Title")
                .initialAirYear(1997)
                .originalMangaAuthors(List.of("Manga 2 Author 1 ", "Manga 2 Author 2"))
                .hasBeenWatched(false)
                .build();

        parentFranchise.addAnime(anime1);
        parentFranchise.addAnime(anime2);

        anime1DTO = AnimeMapper.mapToDTO(anime1);
        anime2DTO = AnimeMapper.mapToDTO(anime2);

        animeCommand = AnimeCommand.builder()
                .animeTitle("Anime To Create")
                .originalMangaAuthors(List.of("Another Manga Author 1", "Another Manga Author 2"))
                .initialAirYear(1999)
                .hasBeenWatched(true)
                .build();
    }

    @Test
    void itShouldRetrieveListOfAnimes() throws Exception {
        List<AnimeDTO> expectedDTOList = List.of(anime1DTO, anime2DTO);
        String franchiseId = parentFranchise.getId();
        String parentFranchiseTitle = parentFranchise.getFranchiseTitle();

        when(mockedService.getAll()).thenReturn(expectedDTOList);

        mockMvc.perform(get(API + V1 + ANIME_FRANCHISES + "/{franchiseId}" + ANIMES, franchiseId))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(equalTo(2))))
                .andExpect(jsonPath("$[0].id", equalTo(anime1DTO.getId())))
                .andExpect(jsonPath("$[0].parentFranchiseTitle", equalTo(parentFranchiseTitle)))
                .andExpect(jsonPath("$[0].animeTitle", equalTo(anime1DTO.getAnimeTitle())))
                .andExpect(jsonPath("$[0].initialAirYear", equalTo(anime1DTO.getInitialAirYear())))
                .andExpect(jsonPath("$[0].originalMangaAuthors", equalTo(anime1DTO.getOriginalMangaAuthors())))
                .andExpect(jsonPath("$[0].hasBeenWatched", equalTo(anime1DTO.getHasBeenWatched())))
                .andExpect(jsonPath("$[1].id", equalTo(anime2DTO.getId())))
                .andExpect(jsonPath("$[1].parentFranchiseTitle", equalTo(parentFranchiseTitle)))
                .andExpect(jsonPath("$[1].animeTitle", equalTo(anime2DTO.getAnimeTitle())))
                .andExpect(jsonPath("$[1].initialAirYear", equalTo(anime2DTO.getInitialAirYear())))
                .andExpect(jsonPath("$[1].originalMangaAuthors", equalTo(anime2DTO.getOriginalMangaAuthors())))
                .andExpect(jsonPath("$[1].hasBeenWatched", equalTo(anime2DTO.getHasBeenWatched())));
        verify(mockedService).getAll();
    }

    @Test
    void itShouldRetrieveAnimeById() throws Exception {
        String franchiseId = parentFranchise.getId();
        String animeId = anime1DTO.getId();
        String expectedPayload = jsonMapper.writeValueAsString(anime1DTO);

        when(mockedService.getById(animeId)).thenReturn(anime1DTO);

        mockMvc.perform(get(API + V1 + ANIME_FRANCHISES +
                        "/{franchiseId}" + ANIMES + "/{animeId}", franchiseId, animeId))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedPayload));
        verify(mockedService).getById(animeId);
    }

    @Test
    void itShouldCreateAnime() throws Exception {
        String franchiseId = parentFranchise.getId();
        String expectedId = UUID.randomUUID().toString();
        String requestPayload = jsonMapper.writeValueAsString(animeCommand);

        when(mockedService.addAnime(eq(franchiseId), any(AnimeCommand.class)))
                .thenReturn(expectedId);

        mockMvc.perform(post(API + V1 + ANIME_FRANCHISES +
                                "/{franchiseId}" + ANIMES, franchiseId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestPayload))
                .andExpect(status().isCreated())
                .andExpect(content().string(expectedId));
    }

    @Test
    void itShouldUpdateAnime() throws Exception {
        String franchiseId = parentFranchise.getId();
        AnimeDTO expectedDTO = AnimeDTO.builder()
                .id(anime1DTO.getId())
                .parentFranchiseTitle(anime1DTO.getParentFranchiseTitle())
                .animeTitle("Anime 1 Updated Title")
                .initialAirYear(anime1DTO.getInitialAirYear() + 4)
                .originalMangaAuthors(anime1DTO.getOriginalMangaAuthors())
                .hasBeenWatched(!anime1DTO.getHasBeenWatched())
                .build();

        String authorsParam = expectedDTO.getOriginalMangaAuthors()
                .stream()
                .reduce("", String::concat);

        when(mockedService.updateAnime(expectedDTO.getId(),
                expectedDTO.getAnimeTitle(),
                expectedDTO.getInitialAirYear(),
                expectedDTO.getOriginalMangaAuthors(),
                expectedDTO.getHasBeenWatched()))
                .thenReturn(expectedDTO);

        String responsePayload = jsonMapper.writeValueAsString(expectedDTO);

        mockMvc.perform(patch(API + V1 + ANIME_FRANCHISES +
                "/{franchiseId}" + ANIMES + "/{animeId}", franchiseId, anime1DTO.getId())
                        .param("animeTitle", expectedDTO.getAnimeTitle())
                        .param("airYear", expectedDTO.getInitialAirYear().toString())
                        .param("mangaAuthors", authorsParam)
                        .param("hasBeenWatched", expectedDTO.getHasBeenWatched().toString()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(responsePayload));

        verify(mockedService).updateAnime(
                expectedDTO.getId(),
                expectedDTO.getAnimeTitle(),
                expectedDTO.getInitialAirYear(),
                expectedDTO.getOriginalMangaAuthors(),
                expectedDTO.getHasBeenWatched());
    }

    @Test
    void itShouldDeleteAnimeById() throws Exception {
        String franchiseId = parentFranchise.getId();
        String animeId = anime1DTO.getId();

        mockMvc.perform(delete(API + V1 + ANIME_FRANCHISES +
                "/{franchiseId}" + ANIMES + "/{animeId}", franchiseId, animeId))
                .andExpect(status().isOk());
        parentFranchise.removeAnime(anime1);

        assertThat(parentFranchise.getAnimes().size()).isEqualTo(1);
        verify(mockedService).deleteAnime(animeId);
    }
}