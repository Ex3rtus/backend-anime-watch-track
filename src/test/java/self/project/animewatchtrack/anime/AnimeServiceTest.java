package self.project.animewatchtrack.anime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import self.project.animewatchtrack.animefranchise.AnimeFranchise;
import self.project.animewatchtrack.animefranchise.AnimeFranchiseRepository;
import self.project.animewatchtrack.exceptions.AnimeBadRequestException;
import self.project.animewatchtrack.exceptions.AnimeNotFoundExeption;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

/**
 * @author Youssef Kaïdi.
 * created 04 nov. 2022.
 */

@ExtendWith(MockitoExtension.class)
class AnimeServiceTest {

    @Mock
    private AnimeRepository animeRepository;
    @Mock
    private AnimeFranchiseRepository franchiseRepository;
    private AnimeServiceImpl serviceUnderTest;
    private AnimeFranchise parentFranchise;
    private Anime anime1;
    private Anime anime2;
    private AnimeDTO anime1DTO;
    private AnimeDTO anime2DTO;
    private AnimeCommand animeCommand;

    @BeforeEach
    void setup() {
        serviceUnderTest = new AnimeServiceImpl(animeRepository, franchiseRepository);

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
                .hasBeenWatched(true)
                .build();

        parentFranchise.addAnime(anime1);
        parentFranchise.addAnime(anime2);

        anime1DTO = AnimeMapper.mapToDTO(anime1);
        anime2DTO = AnimeMapper.mapToDTO(anime2);
        animeCommand = AnimeCommand.builder()
                .animeTitle("Anime To Create Title")
                .originalMangaAuthors(List.of("Another Manga Author 1", "Another Manga Author 2"))
                .initialAirYear(1999)
                .hasBeenWatched(true)
                .build();
    }

    @Test
    void itShouldFindAnimeById() {
        String id = anime1.getId();

        when(animeRepository.findById(id))
                .thenReturn(Optional.of(anime1));

        AnimeDTO resultDTO = serviceUnderTest.getById(id);

        assertThat(resultDTO).isEqualTo(anime1DTO);
        verify(animeRepository).findById(id);
    }

    @Test
    void itShouldThrowWhenAttemptingToFindNonexistentAnimeById() {
        String nonPersistedAnimeId = UUID.randomUUID().toString();
        String exceptionMessage = "anime with ID : " + nonPersistedAnimeId + " not found";
        AnimeNotFoundExeption exceptionToBeThrown = new AnimeNotFoundExeption(nonPersistedAnimeId);

        when(animeRepository.findById(nonPersistedAnimeId))
                .thenThrow(exceptionToBeThrown);

        assertThatThrownBy(() -> serviceUnderTest.getById(nonPersistedAnimeId))
                .isInstanceOf(AnimeNotFoundExeption.class)
                .hasMessageContaining(exceptionMessage);
    }

    @Test
    void itShouldReturnAnEmptyAnimeList() {
        List<AnimeDTO> result = serviceUnderTest.getAll();
        assertThat(result.isEmpty()).isTrue();
        verify(animeRepository).findAll();
    }

    @Test
    void itShouldReturnAPopulatedAnimeList() {
        List<Anime> fixture = Arrays.asList(anime1, anime2);
        when(animeRepository.findAll()).thenReturn(fixture);

        List<AnimeDTO> expected = List.of(anime1DTO, anime2DTO);
        List<AnimeDTO> result = serviceUnderTest.getAll();

        assertThat(result).isEqualTo(expected);
        verify(animeRepository).findAll();
    }

    @Test
    void itShouldAddAnime() {
        String franchiseId = parentFranchise.getId();
        Anime expectedAnime = AnimeMapper.mapToEntity(animeCommand);
        String expectedId = UUID.randomUUID().toString();
        expectedAnime.setId(expectedId);

        when(franchiseRepository.findById(franchiseId))
                .thenReturn(Optional.of(parentFranchise));
        when(animeRepository.save(any(Anime.class)))
                .thenReturn(expectedAnime);
        String resultId = serviceUnderTest.addAnime(franchiseId, animeCommand);

        assertThat(resultId).isEqualTo(expectedId);

        verify(animeRepository).findByTitle(animeCommand.getAnimeTitle());
        verify(franchiseRepository).findById(franchiseId);
        verify(animeRepository).save(any(Anime.class));

    }

    @Test
    void itShouldThrowWhenAttemptingToAddAnime() {
        String franchiseId = parentFranchise.getId();
        String existingAnimeTitle = anime1.getAnimeTitle();
        AnimeCommand existingAnimeCommand = AnimeCommand.builder()
                .animeTitle(existingAnimeTitle)
                .initialAirYear(anime1.getInitialAirYear())
                .originalMangaAuthors(anime1.getOriginalMangaAuthors())
                .hasBeenWatched(anime1.getHasBeenWatched())
                .build();
        String exceptionMessage = "anime with title : " + existingAnimeTitle + " already exists";

        when(animeRepository.findByTitle(existingAnimeTitle))
                .thenReturn(Optional.of(anime1));

        assertThatThrownBy(() -> serviceUnderTest.addAnime(franchiseId, existingAnimeCommand))
                .isInstanceOf(AnimeBadRequestException.class)
                .hasMessageContaining(exceptionMessage);

        verify(animeRepository).findByTitle(existingAnimeTitle);
        verify(animeRepository, never()).save(any());
    }

    @Test
    void itShouldUpdateAnime() {
        String existingId = anime1.getId();
        List<String> newListOfOriginalMangaAuthors = List.of("New Manga Author", "Collaborating Manga Author");
        String newTitle = "New Title";
        Integer newInitialAirYear = anime1DTO.getInitialAirYear() + 1;
        AnimeDTO expectedDTO = AnimeDTO.builder()
                .id(existingId)
                .parentFranchiseTitle(anime1DTO.getParentFranchiseTitle())
                .animeTitle(newTitle)
                .initialAirYear(newInitialAirYear)
                .originalMangaAuthors(newListOfOriginalMangaAuthors)
                .hasBeenWatched(anime1DTO.getHasBeenWatched())
                .build();

        when(animeRepository.findById(existingId))
                .thenReturn(Optional.of(anime1));

        AnimeDTO resultDTO = serviceUnderTest.updateAnime(existingId, newTitle,
                newInitialAirYear, newListOfOriginalMangaAuthors);

        assertThat(resultDTO.getAnimeTitle()).isEqualTo(expectedDTO.getAnimeTitle());
        assertThat(resultDTO.getInitialAirYear()).isEqualTo(expectedDTO.getInitialAirYear());
        assertThat(resultDTO.getOriginalMangaAuthors()).isEqualTo(expectedDTO.getOriginalMangaAuthors());
        verify(animeRepository).findById(existingId);
    }

    @Test
    void itShouldThrowWhenAttemptingToUpdateNonexistentAnime() {
        String animeId = "Nonexistent Anime ID";
        String exceptionMessage = "anime with ID : " + animeId + " not found";

        assertThatThrownBy(() -> serviceUnderTest.updateAnime(animeId, "Anime Title",
                anime1.getInitialAirYear(), anime1.getOriginalMangaAuthors()))
                .isInstanceOf(AnimeNotFoundExeption.class)
                .hasMessageContaining(exceptionMessage);
    }

    @Test
    void itShouldMarkAnimeAsWatched() {
        String animeId = anime1DTO.getId();
        AnimeDTO expectedDTO = AnimeDTO.builder()
                .id(animeId)
                .parentFranchiseTitle(anime1DTO.getParentFranchiseTitle())
                .animeTitle(anime1DTO.getAnimeTitle())
                .initialAirYear(anime1DTO.getInitialAirYear())
                .originalMangaAuthors(anime1DTO.getOriginalMangaAuthors())
                .hasBeenWatched(true)
                .build();

        when(animeRepository.findById(animeId))
                .thenReturn(Optional.of(anime1));

        AnimeDTO resultDTO = serviceUnderTest.markAnime(animeId, true);

        assertThat(resultDTO).isEqualTo(expectedDTO);
        verify(animeRepository).findById(animeId);
    }

    @Test
    void itShouldMarkAnimeAsNotWatched() {
        String animeId = anime2DTO.getId();
        AnimeDTO expectedDTO = AnimeDTO.builder()
                .id(animeId)
                .parentFranchiseTitle(anime2DTO.getParentFranchiseTitle())
                .animeTitle(anime2DTO.getAnimeTitle())
                .initialAirYear(anime2DTO.getInitialAirYear())
                .originalMangaAuthors(anime2DTO.getOriginalMangaAuthors())
                .hasBeenWatched(false)
                .build();

        when(animeRepository.findById(animeId))
                .thenReturn(Optional.of(anime2));

        AnimeDTO resultDTO = serviceUnderTest.markAnime(animeId, false);

        assertThat(resultDTO).isEqualTo(expectedDTO);
        verify(animeRepository).findById(animeId);
    }

    @Test
    void itShouldThrowWhenAttemptingToMarkNonexistentAnime() {
        String animeId = "Nonexistent Anime ID";
        String exceptionMessage = "anime with ID : " + animeId + " not found";

        assertThatThrownBy(() -> serviceUnderTest.markAnime(animeId, true))
                .isInstanceOf(AnimeNotFoundExeption.class)
                .hasMessageContaining(exceptionMessage);
    }

    @Test
    void itShouldDeleteAnime() {
        String animeId = anime1.getId();

        when(animeRepository.findById(animeId))
                .thenReturn(Optional.of(anime1));

        serviceUnderTest.deleteAnime(animeId);

        assertThat(parentFranchise.getAnimes().size()).isEqualTo(1);
        verify(animeRepository).deleteById(animeId);
    }

    @Test
    void itShouldThrowWhenAttemptingToDeleteNonexistentAnime() {
        String animeId = UUID.randomUUID().toString();
        String exceptionMessage = "anime with ID : " + animeId + " not found";

        assertThatThrownBy(() -> serviceUnderTest.deleteAnime(animeId))
                .isInstanceOf(AnimeNotFoundExeption.class)
                .hasMessageContaining(exceptionMessage);
    }
}