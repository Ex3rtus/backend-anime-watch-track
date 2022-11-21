package self.project.animewatchtrack.animeseason;

/**
 * @author Youssef Kaïdi.
 * created 20 nov. 2022.
 */

public class NotWatchedSeasonMarkerStrategy implements SeasonMarkerStrategy {
    @Override
    public void markSeason(AnimeSeason season) {
        season.setHasBeenWatched(Boolean.FALSE);
        season.setCurrentWatchCount(0);
    }
}
