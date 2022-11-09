package self.project.animewatchtrack.anime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import self.project.animewatchtrack.animefranchise.AnimeFranchise;
import self.project.animewatchtrack.animefranchise.AnimeFranchiseRepository;
import self.project.animewatchtrack.exceptions.AnimeBadRequestException;
import self.project.animewatchtrack.exceptions.AnimeNotFoundExeption;

import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

/**
 * @author Youssef Kaïdi.
 * created 04 nov. 2022.
 * TODO : fix stubbing arguments mismatch in itShouldAddAnime() test
 */

@ExtendWith(MockitoExtension.class)
class AnimeServiceImplTest {

    @Mock
    private AnimeRepository animeRepository;

    @Mock
    private AnimeFranchiseRepository franchiseRepository;

    @InjectMocks
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
        parentFranchise = AnimeFranchise.builder()
                .id(UUID.randomUUID().toString())
                .franchiseTitle("Parent Franchise Title")
                .hasBeenWatched(false)
                .animes(new ArrayList<>())
                .build();

        anime1 = Anime.builder()
                .id(UUID.randomUUID().toString())
                .animeTitle("Anime 1 Title")
                .initialAirYear(1970)
                .originalMangaAuthors(List.of("Manga 1 Author 1"))
                .hasBeenWatched(false)
                .build();

        anime2 = Anime.builder()
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
    void itShouldThrowWhenAttemptingToFindAnimeByInvalidId() {
        String fakeId = "Trust me, me no fake";
        String exceptionMessage = "anime with ID : " + fakeId + " not found";
        AnimeNotFoundExeption exceptionToBeThrown = new AnimeNotFoundExeption(fakeId);

        when(animeRepository.findById(fakeId))
                .thenThrow(exceptionToBeThrown);

        assertThatThrownBy(() -> serviceUnderTest.getById(fakeId))
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
//        Anime expectedAnimeNullId = AnimeMapper.mapToEntity(animeCommand);
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
        verify(animeRepository, never()).save(any());
    }

    @Test
    void itShouldUpdateAnime() {
        String existingId = anime1.getId();
        boolean initialValue = anime1.getHasBeenWatched();
        List<String> newListOfOriginalMangaAuthors = List.of("New Manga Author", "Collaborating Manga Author");
        String newTitle = "New Title";
        AnimeDTO expectedDTO = AnimeDTO.builder()
                .id(existingId)
                .parentFranchiseTitle(anime1.getAnimeFranchise().getFranchiseTitle())
                .animeTitle(newTitle)
                .initialAirYear(anime1.getInitialAirYear())
                .originalMangaAuthors(newListOfOriginalMangaAuthors)
                .hasBeenWatched(!initialValue)
                .build();

        when(animeRepository.findById(existingId))
                .thenReturn(Optional.of(anime1));

        AnimeDTO resultDTO = serviceUnderTest.updateAnime(existingId, newTitle,
                anime1.getInitialAirYear(), newListOfOriginalMangaAuthors, !initialValue);

        assertThat(resultDTO).isEqualTo(expectedDTO);
        verify(animeRepository).findById(existingId);
    }

    @Test
    void itShouldThrowWhenAttemptingToUpdateNonexistentAnime() {
        String animeId = "Nonexistent Anime ID";
        String exceptionMessage = "anime with ID : " + animeId + " not found";

        assertThatThrownBy(() -> serviceUnderTest.updateAnime(animeId, "Anime Title",
                anime1.getInitialAirYear(), anime1.getOriginalMangaAuthors(), false))
                .isInstanceOf(AnimeNotFoundExeption.class)
                .hasMessageContaining(exceptionMessage);
    }

    @Test
    void itShouldDeleteAnime() {
        String animeId = anime1.getId();

        when(animeRepository.findById(animeId))
                .thenReturn(Optional.of(anime1));

        serviceUnderTest.deleteAnime(animeId);

        assertThat(parentFranchise.getAnimes().size() == 1).isTrue();
        verify(animeRepository).deleteById(animeId);
    }

    @Test
    void itShouldThrowWhenAttemptingToDeleteAnime() {
        String animeId = "Nonexistent Anime ID";
        String exceptionMessage = "anime with ID : " + animeId + " not found";

        assertThatThrownBy(() -> serviceUnderTest.deleteAnime(animeId))
                .isInstanceOf(AnimeNotFoundExeption.class)
                .hasMessageContaining(exceptionMessage);
    }
}