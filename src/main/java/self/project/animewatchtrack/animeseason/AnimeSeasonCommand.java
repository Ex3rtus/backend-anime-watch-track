package self.project.animewatchtrack.animeseason;

import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

import self.project.animewatchtrack.validation.constraints.EpisodeNumber;

import static self.project.animewatchtrack.constants.SeasonValidationMessages.*;

/**
 * @author Youssef Kaïdi.
 * created 09 nov. 2022.
 */

@Builder
@Getter
@EpisodeNumber(message = episodeNumberMessage)
public class AnimeSeasonCommand {

    @Positive(message = numberMessage)
    private Integer seasonNumber;

    @Positive(message = totalEpisodesMessage)
    private Integer totalEpisodesCount;

    @PositiveOrZero(message = watchCountMessage)
    private Integer currentWatchCount;

    @NotNull(message = isWatchedMessage)
    private Boolean isWatched;

}
