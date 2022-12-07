package self.project.animewatchtrack.animeseason;

import java.util.List;

/**
 * @author Youssef Kaïdi.
 * created 09 nov. 2022.
 */

public interface AnimeSeasonService {

    AnimeSeasonDTO getById(String seasonId);

    List<AnimeSeasonDTO> getAll();

    String addAnimeSeason(String animeId, AnimeSeasonCommand animeSeasonCommand);

    AnimeSeasonDTO updateAnimeSeason(String seasonId,
                                     Integer seasonNumber, Integer totalEpisodesCount);

    AnimeSeasonDTO markAnimeSeason(String seasonId, Boolean newHasBeenWatched);

    void deleteAnimeSeason(String seasonId);

    AnimeSeasonDTO watchEpisodes(String seasonId, Integer newWatchCount);
}
