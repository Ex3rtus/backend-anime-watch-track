package self.project.animewatchtrack.animeseason;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import self.project.animewatchtrack.anime.Anime;
import self.project.animewatchtrack.animefranchise.AnimeFranchise;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static self.project.animewatchtrack.constants.ResourcePaths.*;

/**
 * @author Youssef Kaïdi.
 * created 15 nov. 2022.
 */

@AutoConfigureMockMvc
@WebMvcTest(AnimeSeasonController.class)
class AnimeSeasonControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private AnimeSeasonService mockedService;
    private final ObjectMapper jsonMapper = new ObjectMapper();
    private static AnimeFranchise parentFranchise;
    private static Anime parentAnime;
    private static AnimeSeason seasonOne;
    private static AnimeSeason seasonTwo;
    private static AnimeSeasonDTO seasonOneDTO;
    private static AnimeSeasonDTO seasonTwoDTO;
    private static AnimeSeasonCommand seasonCommand;
    private final static String BASE_URI = API + V1 + ANIME_FRANCHISES + "/{franchiseId}"
            + ANIMES + "/{animeId}" + ANIME_SEASONS;;

    @BeforeAll
    static void setup() {
        parentFranchise = new AnimeFranchise().toBuilder()
                .id(UUID.randomUUID().toString())
                .franchiseTitle("Parent Franchise Title")
                .hasBeenWatched(false)
                .build();

        parentAnime = new Anime().toBuilder()
                .id(UUID.randomUUID().toString())
                .animeTitle("Anime Title")
                .initialAirYear(1970)
                .originalMangaAuthors(List.of("Manga Author 1"))
                .hasBeenWatched(false)
                .build();

        seasonOne = new AnimeSeason().toBuilder()
                .id(UUID.randomUUID().toString())
                .seasonNumber(1)
                .totalEpisodesCount(158)
                .currentWatchCount(158)
                .hasBeenWatched(true)
                .build();

        seasonTwo = new AnimeSeason().toBuilder()
                .id(UUID.randomUUID().toString())
                .seasonNumber(2)
                .totalEpisodesCount(108)
                .currentWatchCount(5)
                .hasBeenWatched(false)
                .build();

        parentFranchise.addAnime(parentAnime);
        parentAnime.addSeason(seasonOne);
        parentAnime.addSeason(seasonTwo);

        seasonOneDTO = AnimeSeasonMapper.mapToDTO(seasonOne);
        seasonTwoDTO = AnimeSeasonMapper.mapToDTO(seasonTwo);

        seasonCommand = AnimeSeasonCommand.builder()
                .seasonNumber(3)
                .totalEpisodesCount(113)
                .currentWatchCount(0)
                .hasBeenWatched(false)
                .build();
    }

    @Test
    void itShouldGetById() throws Exception {
        String franchiseId = parentFranchise.getId();
        String animeId = parentAnime.getId();
        String seasonOneId = seasonOne.getId();
        String expectedPayload = jsonMapper.writeValueAsString(seasonOneDTO);

        when(mockedService.getById(seasonOneId))
                .thenReturn(seasonOneDTO);

        mockMvc.perform(get(BASE_URI + "/{seasonId}", franchiseId, animeId, seasonOneId))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedPayload));

        verify(mockedService).getById(seasonOneId);
    }

    @Test
    void itShouldGetPopulatedList() throws Exception {
        List<AnimeSeasonDTO> expectedDTOList = List.of(seasonOneDTO, seasonTwoDTO);
        String franchiseId = parentFranchise.getId();
        String animeId = parentAnime.getId();

        when(mockedService.getAll()).thenReturn(expectedDTOList);

        mockMvc.perform(get(BASE_URI, franchiseId, animeId))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(equalTo(2))))
                .andExpect(jsonPath("$[0].id", equalTo(seasonOneDTO.getId())))
                .andExpect(jsonPath("$[0].parentAnimeTitle", equalTo(seasonOneDTO.getParentAnimeTitle())))
                .andExpect(jsonPath("$[0].seasonNumber", equalTo(seasonOneDTO.getSeasonNumber())))
                .andExpect(jsonPath("$[0].totalEpisodesCount", equalTo(seasonOneDTO.getTotalEpisodesCount())))
                .andExpect(jsonPath("$[0].currentWatchCount", equalTo(seasonOneDTO.getCurrentWatchCount())))
                .andExpect(jsonPath("$[0].hasBeenWatched", equalTo(seasonOneDTO.getHasBeenWatched())))
                .andExpect(jsonPath("$[1].id", equalTo(seasonTwoDTO.getId())))
                .andExpect(jsonPath("$[1].parentAnimeTitle", equalTo(seasonTwoDTO.getParentAnimeTitle())))
                .andExpect(jsonPath("$[1].seasonNumber", equalTo(seasonTwoDTO.getSeasonNumber())))
                .andExpect(jsonPath("$[1].totalEpisodesCount", equalTo(seasonTwoDTO.getTotalEpisodesCount())))
                .andExpect(jsonPath("$[1].currentWatchCount", equalTo(seasonTwoDTO.getCurrentWatchCount())))
                .andExpect(jsonPath("$[1].hasBeenWatched", equalTo(seasonTwoDTO.getHasBeenWatched())));

        verify(mockedService).getAll();
    }

    @Test
    void itShouldCreateNewSeason() throws Exception {
        String franchiseId = parentFranchise.getId();
        String animeId = parentAnime.getId();
        String expectedId = UUID.randomUUID().toString();
        String requestPayload = jsonMapper.writeValueAsString(seasonCommand);

        when(mockedService.addAnimeSeason(any(String.class), any(AnimeSeasonCommand.class)))
                .thenReturn(expectedId);

        mockMvc.perform(post(BASE_URI, franchiseId, animeId)
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestPayload))
                .andExpect(status().isCreated())
                .andExpect(content().string(expectedId));
    }

    @Test
    void itShouldUpdateExistingSeason() throws Exception {
        String franchiseId = parentFranchise.getId();
        String animeId = parentAnime.getId();
        String seasonId = seasonOneDTO.getId();
        Integer newTotalEpisodesCount = seasonTwoDTO.getTotalEpisodesCount() + 30;
        Integer newCurrentWatchCount = seasonTwoDTO.getCurrentWatchCount() + 47;
        AnimeSeasonDTO expectedDTO = AnimeSeasonDTO.builder()
                .id(seasonOneDTO.getId())
                .parentAnimeTitle(seasonOneDTO.getParentAnimeTitle())
                .seasonNumber(seasonOneDTO.getSeasonNumber())
                .totalEpisodesCount(newTotalEpisodesCount)
                .currentWatchCount(newCurrentWatchCount)
                .hasBeenWatched(seasonOneDTO.getHasBeenWatched())
                .build();
        String responsePayload = jsonMapper.writeValueAsString(expectedDTO);

        when(mockedService.updateAnimeSeason(
                expectedDTO.getId(),
                expectedDTO.getSeasonNumber(),
                expectedDTO.getTotalEpisodesCount()
        )).thenReturn(expectedDTO);

        mockMvc.perform(patch(BASE_URI + "/{seasonId}",
                franchiseId, animeId, seasonId)
                .param("seasonNumber", expectedDTO.getSeasonNumber().toString())
                .param("totalEpisodesCount", expectedDTO.getTotalEpisodesCount().toString())
                .param("currentWatchCount", expectedDTO.getCurrentWatchCount().toString())
                .param("hasBeenWatched", expectedDTO.getHasBeenWatched().toString()))
                .andExpect(status().isOk())
                .andExpect(content().string(responsePayload));

        verify(mockedService).updateAnimeSeason(
                expectedDTO.getId(),
                expectedDTO.getSeasonNumber(),
                expectedDTO.getTotalEpisodesCount()
        );
    }

    @Test
    void itShouldMarkExistingAnimeSeasonAsNotWatchedAndResetWatchCount() throws Exception {
        String franchiseId = parentFranchise.getId();
        String animeId = parentAnime.getId();
        String seasonId = seasonTwoDTO.getId();

        AnimeSeasonDTO expectedDTO = AnimeSeasonDTO.builder()
                .id(seasonId)
                .parentAnimeTitle(seasonTwoDTO.getParentAnimeTitle())
                .seasonNumber(seasonTwoDTO.getSeasonNumber())
                .currentWatchCount(0)
                .hasBeenWatched(false)
                .build();

        String expectedPayload = jsonMapper.writeValueAsString(expectedDTO);

        when(mockedService.markAnimeSeason(seasonId, false))
                .thenReturn(expectedDTO);

        mockMvc.perform(patch(BASE_URI + "/{seasonId}" + MARK, franchiseId, animeId, seasonId)
                        .param("hasBeenWatched", expectedDTO.getHasBeenWatched().toString()))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedPayload));

        verify(mockedService).markAnimeSeason(seasonId, false);
    }

    @Test
    void itShouldMarkExistingAnimeSeasonAsWatchedAndUpdateWatchCount() throws Exception {
        String franchiseId = parentFranchise.getId();
        String animeId = parentAnime.getId();
        String seasonId = seasonTwoDTO.getId();

        AnimeSeasonDTO expectedDTO = AnimeSeasonDTO.builder()
                .id(seasonTwoDTO.getId())
                .parentAnimeTitle(seasonTwoDTO.getParentAnimeTitle())
                .seasonNumber(seasonTwoDTO.getSeasonNumber())
                .currentWatchCount(seasonTwoDTO.getTotalEpisodesCount())
                .hasBeenWatched(true)
                .build();

        String expectedPayload = jsonMapper.writeValueAsString(expectedDTO);

        when(mockedService.markAnimeSeason(seasonTwoDTO.getId(), true))
                .thenReturn(expectedDTO);

        mockMvc.perform(patch(BASE_URI + "/{seasonId}" + MARK, franchiseId, animeId, seasonId)
                .param("hasBeenWatched", expectedDTO.getHasBeenWatched().toString()))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedPayload));

        verify(mockedService).markAnimeSeason(seasonId, true);
    }

    @Test
    void itShouldUpdateWatchCount() throws Exception {
        String franchiseId = parentFranchise.getId();
        String animeId = parentAnime.getId();
        String seasonId = seasonTwoDTO.getId();
        Integer newWatchCount = seasonTwoDTO.getCurrentWatchCount() + 42;

        AnimeSeasonDTO expectedDTO = AnimeSeasonDTO.builder()
                .id(seasonTwoDTO.getId())
                .parentAnimeTitle(seasonTwoDTO.getParentAnimeTitle())
                .seasonNumber(seasonTwoDTO.getSeasonNumber())
                .currentWatchCount(newWatchCount)
                .hasBeenWatched(seasonTwoDTO.getHasBeenWatched())
                .build();

        String expectedPayload = jsonMapper.writeValueAsString(expectedDTO);

        when(mockedService.watchEpisodes(seasonTwoDTO.getId(), newWatchCount))
                .thenReturn(expectedDTO);

        mockMvc.perform(patch(BASE_URI + "/{seasonId}" + WATCH, franchiseId, animeId, seasonId)
                        .param("watchCount", expectedDTO.getCurrentWatchCount().toString()))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedPayload));

        verify(mockedService).watchEpisodes(seasonId, newWatchCount);
    }

    @Test
    void itShouldDeleteExistingSeason() throws Exception {
        String franchiseId = parentFranchise.getId();
        String animeId = parentAnime.getId();
        String seasonId = seasonTwo.getId();

        mockMvc.perform(delete(BASE_URI + "/{seasonId}", franchiseId, animeId, seasonId))
                .andExpect(status().isNoContent());
        parentAnime.removeSeason(seasonTwo);

        assertThat(parentAnime.getSeasons().size()).isEqualTo(1);
        verify(mockedService).deleteAnimeSeason(seasonId);
    }
}