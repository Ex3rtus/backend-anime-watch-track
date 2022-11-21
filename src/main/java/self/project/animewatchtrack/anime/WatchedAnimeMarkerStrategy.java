package self.project.animewatchtrack.anime;

/**
 * @author Youssef Kaïdi.
 * created 20 nov. 2022.
 */

public class WatchedAnimeMarkerStrategy implements AnimeMarkerStrategy {
    @Override
    public void markAnimeAndCascadeDown(Anime anime) {
        anime.setHasBeenWatched(Boolean.TRUE);
        anime.getSeasons()
                .forEach(season -> season.getStrategyMap()
                        .get(Boolean.TRUE)
                        .markSeason(season));
    }
}
