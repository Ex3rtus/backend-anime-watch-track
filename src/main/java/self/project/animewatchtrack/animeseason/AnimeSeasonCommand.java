package self.project.animewatchtrack.animeseason;

import lombok.Builder;
import lombok.Getter;

/**
 * @author Youssef Kaïdi.
 * created 09 nov. 2022.
 */

@Builder
@Getter
public class AnimeSeasonCommand {

    private Integer seasonNumber;
    private Integer totalEpisodesCount;
    private Integer currentWatchCount;
    private Boolean hasBeenWatched;

}
