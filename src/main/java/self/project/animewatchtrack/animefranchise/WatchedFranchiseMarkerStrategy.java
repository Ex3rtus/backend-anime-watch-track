package self.project.animewatchtrack.animefranchise;

/**
 * @author Youssef Kaïdi.
 * created 20 nov. 2022.
 */

public class WatchedFranchiseMarkerStrategy implements FranchiseMarkerStrategy {

    @Override
    public void markFranchiseAndCascadeDown(AnimeFranchise franchise) {
        franchise.setIsWatched(Boolean.TRUE);
        franchise.getAnimes()
                .forEach(anime -> anime.getStrategyMap()
                        .get(Boolean.TRUE)
                        .markAnimeAndCascadeDown(anime));
    }
}
