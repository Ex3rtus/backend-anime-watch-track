package self.project.animewatchtrack.anime;

/**
 * @author Youssef Kaïdi.
 * created 20 nov. 2022.
 */

public class NotWatchedAnimeMarkerStrategy implements AnimeMarkerStrategy {
    @Override
    public void markAnimeAndCascadeDown(Anime anime) {
        anime.setHasBeenWatched(Boolean.FALSE);
        anime.getSeasons()
                .forEach(season -> season.getStrategyMap()
                        .get(Boolean.FALSE)
                        .markSeason(season));
    }
}
