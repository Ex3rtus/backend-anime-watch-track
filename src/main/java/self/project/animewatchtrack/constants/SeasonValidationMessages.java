package self.project.animewatchtrack.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * @author Youssef Kaïdi.
 * created 18 déc. 2022.
 */

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SeasonValidationMessages {
    public final static String episodeNumberMessage = "Season episode number and total episodes don't match";
    public final static String numberMessage = "Season number must be a positive number";
    public final static String totalEpisodesMessage = "Total number of episodes must be positive integer";
    public final static String watchCountMessage = "Current watch count must be a positive integer";
    public final static String isWatchedMessage = "Season watch mark cannot be null";
}
