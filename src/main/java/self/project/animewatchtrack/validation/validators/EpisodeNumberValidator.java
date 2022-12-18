package self.project.animewatchtrack.validation.validators;

import self.project.animewatchtrack.animeseason.AnimeSeasonCommand;
import self.project.animewatchtrack.validation.constraints.EpisodeNumber;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author Youssef Kaïdi.
 * created 15 déc. 2022.
 */

public class EpisodeNumberValidator implements ConstraintValidator<EpisodeNumber, AnimeSeasonCommand> {
    @Override
    public boolean isValid(AnimeSeasonCommand seasonCommand, ConstraintValidatorContext constraintValidatorContext) {
        Integer totalEpisodes = seasonCommand.getTotalEpisodesCount();
        Integer currentEpisode = seasonCommand.getCurrentWatchCount();
        return totalEpisodes >= currentEpisode;
    }
}
