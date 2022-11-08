package self.project.animewatchtrack.anime;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

/**
 * @author Youssef Kaïdi.
 * created 04 nov. 2022.
 */

@DataJpaTest
class AnimeRepositoryTest {

    @Autowired
    private AnimeRepository repositoryUnderTest;

    @AfterEach
    void tearDown() {
        repositoryUnderTest.deleteAll();
    }

    @Test
    void itShouldFindAnimeByTitle() {
        String existingAnimeTitle = "Anime Title";
        Anime anime = Anime.builder()
                .id(UUID.randomUUID().toString())
                .animeTitle(existingAnimeTitle)
                .initialAirYear(1970)
                .originalMangaAuthors(List.of("Manga 1 Author 1"))
                .hasBeenWatched(false)
                .build();

        repositoryUnderTest.save(anime);

        boolean result = repositoryUnderTest.findByTitle(existingAnimeTitle).isPresent();
        assertThat(result).isTrue();
    }

    @Test
    void itShouldFailToFindAnimeByTitle() {
        String nonexistentAnimeTitle = "Nonexistent Anime";
        boolean result = repositoryUnderTest.findByTitle(nonexistentAnimeTitle).isPresent();
        assertThat(result).isFalse();
    }
}