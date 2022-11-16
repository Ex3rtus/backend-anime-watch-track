package self.project.animewatchtrack.animeseason;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * @author Youssef Kaïdi.
 * created 09 nov. 2022.
 */

@Builder
@Getter
@EqualsAndHashCode
public class AnimeSeasonDTO {

    private String id;
    private String parentAnimeTitle;
    private Integer seasonNumber;
    private Integer totalEpisodesCount;
    private Integer currentWatchCount;
    private Boolean hasBeenWatched;

}
