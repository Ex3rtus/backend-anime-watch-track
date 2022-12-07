package self.project.animewatchtrack.anime;

import java.util.List;

/**
 * @author Youssef Kaïdi.
 * created 02 nov. 2022.
 */

public interface AnimeService {
    AnimeDTO getById(String animeId);

    List<AnimeDTO> getAll();

    String addAnime(String franchiseId, AnimeCommand animeCommand);

    AnimeDTO updateAnime(String animeId, String animeTitle,
                         Integer airYear, List<String> mangaAuthors);

    AnimeDTO markAnime(String animeId, Boolean newHasBeenWatched);

    void deleteAnime(String animeId);
}
