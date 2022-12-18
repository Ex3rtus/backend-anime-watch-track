package self.project.animewatchtrack.animeseason;

/**
 * @author Youssef Kaïdi.
 * created 20 nov. 2022.
 */

public class WatchedSeasonMarkerStrategy implements SeasonMarkerStrategy {
    @Override
    public void markSeason(AnimeSeason season) {
        season.setIsWatched(Boolean.TRUE);
        season.setCurrentWatchCount(season.getTotalEpisodesCount());
    }
}
