package self.project.animewatchtrack.animefranchise;

/**
 * @author Youssef Kaïdi.
 * created 20 nov. 2022.
 */

public class NotWatchedFranchiseMarkerStrategy implements FranchiseMarkerStrategy {

    @Override
    public void markFranchiseAndCascadeDown(AnimeFranchise franchise) {
        franchise.setIsWatched(Boolean.FALSE);
        franchise.getAnimes()
                .forEach(anime -> anime.getStrategyMap()
                        .get(Boolean.FALSE)
                        .markAnimeAndCascadeDown(anime));
    }
}
