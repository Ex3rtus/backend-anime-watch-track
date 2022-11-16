package self.project.animewatchtrack.animeseason;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import self.project.animewatchtrack.anime.Anime;
import self.project.animewatchtrack.anime.AnimeRepository;
import self.project.animewatchtrack.exceptions.AnimeSeasonBadRequest;
import self.project.animewatchtrack.exceptions.AnimeSeasonNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

/**
 * @author Youssef Kaïdi.
 * created 14 nov. 2022.
 */

@SpringBootTest
class AnimeSeasonServiceIntegrationTest {

    @Autowired
    private AnimeSeasonRepository animeSeasonRepository;

    @Autowired
    private AnimeRepository animeRepository;

    @Autowired
    private AnimeSeasonServiceImpl animeSeasonService;

    private Anime parentAnime;
    private AnimeSeason seasonOne;
    private AnimeSeason seasonTwo;

    private AnimeSeasonCommand seasonCommand;

    @BeforeEach
    void setup() {
        animeRepository.deleteAll();
        animeSeasonRepository.deleteAll();

        parentAnime = Anime.builder()
                .animeTitle("Anime Title")
                .initialAirYear(1970)
                .originalMangaAuthors(List.of("Manga Author 1"))
                .seasons(new ArrayList<>())
                .hasBeenWatched(false)
                .build();

        seasonOne = AnimeSeason.builder()
                .seasonNumber(1)
                .totalEpisodesCount(158)
                .currentWatchCount(158)
                .hasBeenWatched(true)
                .build();

        seasonTwo = AnimeSeason.builder()
                .seasonNumber(2)
                .totalEpisodesCount(108)
                .currentWatchCount(5)
                .hasBeenWatched(false)
                .build();

        parentAnime.addSeason(seasonOne);
        parentAnime.addSeason(seasonTwo);

        parentAnime = animeRepository.save(parentAnime);
        seasonOne = animeSeasonRepository.save(seasonOne);
        seasonTwo = animeSeasonRepository.save(seasonTwo);

        seasonCommand = AnimeSeasonCommand.builder()
                .seasonNumber(3)
                .totalEpisodesCount(113)
                .currentWatchCount(0)
                .hasBeenWatched(false)
                .build();
    }

    @Test
    @Transactional
    void itShouldRetrieveAnimeSeasonById() {
        AnimeSeasonDTO resultDTO = animeSeasonService.getById(seasonOne.getId());

        assertThat(resultDTO.getId()).isEqualTo(seasonOne.getId());
        assertThat(resultDTO.getParentAnimeTitle()).isEqualTo(seasonOne.getAnime().getAnimeTitle());
        assertThat(resultDTO.getSeasonNumber()).isEqualTo(seasonOne.getSeasonNumber());
        assertThat(resultDTO.getTotalEpisodesCount()).isEqualTo(seasonOne.getTotalEpisodesCount());
        assertThat(resultDTO.getCurrentWatchCount()).isEqualTo(seasonOne.getCurrentWatchCount());
        assertThat(resultDTO.getHasBeenWatched()).isEqualTo(seasonOne.getHasBeenWatched());

    }

    @Test
    void itShouldThrowWhenAttemptingToFindByIdANonexistentSeason() {
        String nonPersistedSeasonId = UUID.randomUUID().toString();
        String expectionMessage = "anime season with ID : " + nonPersistedSeasonId + " not found";

        assertThatThrownBy(() -> animeSeasonService.getById(nonPersistedSeasonId))
                .isInstanceOf(AnimeSeasonNotFoundException.class)
                .hasMessage(expectionMessage);
    }

    @Test
    @Transactional
    void itShouldRetrievePopulatedListOfAnimeSeasons() {
        List<AnimeSeasonDTO> expected = Stream.of(seasonOne, seasonTwo)
                .map(AnimeSeasonMapper::mapToDTO)
                .collect(Collectors.toList());

        List<AnimeSeasonDTO> result = animeSeasonService.getAll();

        assertThat(result.size()).isEqualTo(expected.size());
        assertThat(result).isEqualTo(expected);
    }

    @Test
    @Transactional
    void itShouldAddAnimeSeason() {
        String persistedSeasonId = animeSeasonService.addAnimeSeason(parentAnime.getId(), seasonCommand);
        AnimeSeason expectedPersisted = AnimeSeasonMapper.mapToEntity(seasonCommand);
        expectedPersisted.setId(persistedSeasonId);

        AnimeSeason persisted = animeSeasonRepository.givenAnimeIdFindBySeasonNumber(parentAnime.getId(),
                seasonCommand.getSeasonNumber()).get();

        assertThat(persisted.getSeasonNumber()).isEqualTo(expectedPersisted.getSeasonNumber());
        assertThat(persisted.getTotalEpisodesCount()).isEqualTo(expectedPersisted.getTotalEpisodesCount());
        assertThat(persisted.getCurrentWatchCount()).isEqualTo(expectedPersisted.getCurrentWatchCount());
        assertThat(persisted.getHasBeenWatched()).isEqualTo(expectedPersisted.getHasBeenWatched());
        assertThat(persisted.getAnime()).isEqualTo(parentAnime);
    }

    @Test
    @Transactional
    void itShouldThrowWhenAttemptingToAddExistingSeasonToAnAnime() {
        String animeId = parentAnime.getId();
        String exceptionMessage = "anime season number : 1 already exists for anime with id : " + animeId;
        AnimeSeasonCommand existingSeasonCommand = AnimeSeasonCommand.builder()
                .seasonNumber(1)
                .totalEpisodesCount(158)
                .currentWatchCount(158)
                .hasBeenWatched(true)
                .build();

        assertThatThrownBy(() -> animeSeasonService.addAnimeSeason(parentAnime.getId(), existingSeasonCommand))
                .isInstanceOf(AnimeSeasonBadRequest.class)
                .hasMessage(exceptionMessage);
    }

    @Test
    @Transactional
    void itShouldUpdateExistingSeason() {
        Integer newTotalEpisodesCount = seasonOne.getTotalEpisodesCount() + 20;
        Integer newCurrentWatchCount = seasonOne.getCurrentWatchCount() + 5;
        AnimeSeason updateExpectation = AnimeSeason.builder()
                .id(seasonOne.getId())
                .seasonNumber(seasonOne.getSeasonNumber() + 3)
                .totalEpisodesCount(newTotalEpisodesCount)
                .currentWatchCount(newCurrentWatchCount)
                .hasBeenWatched(false)
                .build();

        AnimeSeasonDTO updateDTOResult = animeSeasonService.updateAnimeSeason(
                updateExpectation.getId(),
                updateExpectation.getSeasonNumber(),
                updateExpectation.getTotalEpisodesCount(),
                updateExpectation.getCurrentWatchCount(),
                updateExpectation.getHasBeenWatched());

        assertThat(updateDTOResult.getSeasonNumber()).isEqualTo(updateExpectation.getSeasonNumber());
        assertThat(updateDTOResult.getTotalEpisodesCount()).isEqualTo(updateExpectation.getTotalEpisodesCount());
        assertThat(updateDTOResult.getCurrentWatchCount()).isEqualTo(updateExpectation.getCurrentWatchCount());
        assertThat(updateDTOResult.getHasBeenWatched()).isEqualTo(updateExpectation.getHasBeenWatched());
    }

    @Test
    void itShouldThrowWhenAttemptingToUpdateNonexistentSeason() {
        String nonexistentSeasonId = UUID.randomUUID().toString();
        String exceptionMessage = "anime season with ID : " + nonexistentSeasonId + " not found";
        assertThatThrownBy(
                () -> animeSeasonService.updateAnimeSeason(nonexistentSeasonId, null, null, null, null))
                .isInstanceOf(AnimeSeasonNotFoundException.class)
                .hasMessage(exceptionMessage);
    }

    @Test
    @Transactional
    void itShouldDeleteExistingSeason() {
        String deletedSeasonId = seasonTwo.getId();
        String exceptionMessage = "anime season with ID : " + deletedSeasonId + " not found";
        animeSeasonService.deleteAnimeSeason(deletedSeasonId);

        assertThatThrownBy(
                () -> animeSeasonService.getById(deletedSeasonId))
                .isInstanceOf(AnimeSeasonNotFoundException.class)
                .hasMessage(exceptionMessage);
    }
}