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

        parentAnime = new Anime().toBuilder()
                .animeTitle("Anime Title")
                .initialAirYear(1970)
                .originalMangaAuthors(List.of("Manga Author 1"))
                .hasBeenWatched(false)
                .build();

        seasonOne = new AnimeSeason().toBuilder()
                .seasonNumber(1)
                .totalEpisodesCount(158)
                .currentWatchCount(158)
                .hasBeenWatched(true)
                .build();

        seasonTwo = new AnimeSeason().toBuilder()
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
        String exceptionMessage = "anime season with ID : " + nonPersistedSeasonId + " not found";

        assertThatThrownBy(() -> animeSeasonService.getById(nonPersistedSeasonId))
                .isInstanceOf(AnimeSeasonNotFoundException.class)
                .hasMessage(exceptionMessage);
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
        Integer newSeasonNumber = seasonOne.getSeasonNumber() + 3;
        AnimeSeason updateExpectation = new AnimeSeason().toBuilder()
                .id(seasonOne.getId())
                .seasonNumber(newSeasonNumber)
                .totalEpisodesCount(newTotalEpisodesCount)
                .currentWatchCount(seasonOne.getCurrentWatchCount())
                .hasBeenWatched(seasonOne.getHasBeenWatched())
                .build();

        AnimeSeasonDTO updateDTOResult = animeSeasonService.updateAnimeSeason(
                updateExpectation.getId(),
                updateExpectation.getSeasonNumber(),
                updateExpectation.getTotalEpisodesCount());

        assertThat(updateDTOResult.getSeasonNumber()).isEqualTo(updateExpectation.getSeasonNumber());
        assertThat(updateDTOResult.getTotalEpisodesCount()).isEqualTo(updateExpectation.getTotalEpisodesCount());
    }

    @Test
    void itShouldThrowWhenAttemptingToUpdateNonexistentSeason() {
        String nonexistentSeasonId = UUID.randomUUID().toString();
        String exceptionMessage = "anime season with ID : " + nonexistentSeasonId + " not found";
        assertThatThrownBy(
                () -> animeSeasonService.updateAnimeSeason(nonexistentSeasonId, null, null))
                .isInstanceOf(AnimeSeasonNotFoundException.class)
                .hasMessage(exceptionMessage);
    }

    @Test
    @Transactional
    void itShouldMarkAnimeSeasonAsNotWatchedAndResetWatchCount() {
        AnimeSeasonDTO expected = AnimeSeasonDTO.builder()
                .id(seasonTwo.getId())
                .seasonNumber(seasonTwo.getSeasonNumber())
                .totalEpisodesCount(seasonTwo.getTotalEpisodesCount())
                .currentWatchCount(0)
                .hasBeenWatched(false)
                .build();

        AnimeSeasonDTO updateDTOResult = animeSeasonService.markAnimeSeason(seasonTwo.getId(), false);

        assertThat(updateDTOResult.getCurrentWatchCount()).isEqualTo(expected.getCurrentWatchCount());
        assertThat(updateDTOResult.getHasBeenWatched()).isEqualTo(expected.getHasBeenWatched());
    }

    @Test
    @Transactional
    void itShouldMarkAnimeSeasonAsWatchedAndUpdateWatchCount() {
        AnimeSeasonDTO expected = AnimeSeasonDTO.builder()
                .id(seasonTwo.getId())
                .seasonNumber(seasonTwo.getSeasonNumber())
                .totalEpisodesCount(seasonTwo.getTotalEpisodesCount())
                .currentWatchCount(seasonTwo.getTotalEpisodesCount())
                .hasBeenWatched(true)
                .build();

        AnimeSeasonDTO updateDTOResult = animeSeasonService.markAnimeSeason(seasonTwo.getId(), true);

        assertThat(updateDTOResult.getCurrentWatchCount()).isEqualTo(expected.getCurrentWatchCount());
        assertThat(updateDTOResult.getHasBeenWatched()).isEqualTo(expected.getHasBeenWatched());
    }

    @Test
    void itShouldThrowWhenAttemptingToMarkNonexistentSeason() {
        String nonexistentSeasonId = UUID.randomUUID().toString();
        String exceptionMessage = "anime season with ID : " + nonexistentSeasonId + " not found";
        assertThatThrownBy(
                () -> animeSeasonService.markAnimeSeason(nonexistentSeasonId, false))
                .isInstanceOf(AnimeSeasonNotFoundException.class)
                .hasMessage(exceptionMessage);
    }

    @Test
    @Transactional
    void itShouldUpdateWatchCountOfAnimeSeason() {
        Integer newWatchCount = seasonTwo.getCurrentWatchCount() + 37;
        AnimeSeason expected = new AnimeSeason().toBuilder()
                .id(seasonTwo.getId())
                .seasonNumber(seasonTwo.getSeasonNumber())
                .totalEpisodesCount(seasonTwo.getTotalEpisodesCount())
                .currentWatchCount(newWatchCount)
                .hasBeenWatched(seasonTwo.getHasBeenWatched())
                .build();

        AnimeSeasonDTO updateDTOResult = animeSeasonService.watchEpisodes(seasonTwo.getId(), newWatchCount);

        assertThat(updateDTOResult.getCurrentWatchCount()).isEqualTo(expected.getCurrentWatchCount());
    }

    @Test
    @Transactional
    void itShouldNotUpdateWatchCountOfAnimeSeasonWhenGreaterThanTotalEpisodes() {
        Integer invalidWatchCount = seasonTwo.getTotalEpisodesCount() + 420;
        AnimeSeasonDTO updateDTOResult = animeSeasonService.watchEpisodes(seasonTwo.getId(), invalidWatchCount);

        assertThat(updateDTOResult.getCurrentWatchCount()).isEqualTo(seasonTwo.getCurrentWatchCount());
    }

    @Test
    @Transactional
    void itShouldNotUpdateWatchCountOfAnimeSeasonWhenProvidingNegativeOne() {
        Integer negativeWatchCount = -39;
        AnimeSeasonDTO updateDTOResult = animeSeasonService.watchEpisodes(seasonTwo.getId(), negativeWatchCount);

        assertThat(updateDTOResult.getCurrentWatchCount()).isEqualTo(seasonTwo.getCurrentWatchCount());
    }

    @Test
    @Transactional
    void itShouldMarkSeasonAsWatchedWhenUpdatingWatchCountToTotalEpisodes() {
        Integer totalEpisodesCount = seasonTwo.getTotalEpisodesCount();
        AnimeSeasonDTO updateDTOResult = animeSeasonService.watchEpisodes(seasonTwo.getId(), totalEpisodesCount);

        assertThat(updateDTOResult.getCurrentWatchCount()).isEqualTo(seasonTwo.getCurrentWatchCount());
        assertThat(updateDTOResult.getHasBeenWatched()).isTrue();
    }

    @Test
    void itShouldThrowWhenAttemptingToUpdateWatchCountOfNonexistentSeason() {
        String nonexistentSeasonId = UUID.randomUUID().toString();
        String exceptionMessage = "anime season with ID : " + nonexistentSeasonId + " not found";
        assertThatThrownBy(
                () -> animeSeasonService.watchEpisodes(nonexistentSeasonId, 420))
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