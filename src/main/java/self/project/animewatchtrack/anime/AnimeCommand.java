package self.project.animewatchtrack.anime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.List;

import static self.project.animewatchtrack.constants.AnimeValidationMessages.*;

/**
 * @author Youssef Kaïdi.
 * created 02 nov. 2022.
 */

@AllArgsConstructor
@Builder
@Getter
public class AnimeCommand {

    @NotBlank(message = titleMessage)
    private String animeTitle;

    @Positive(message = yearMessage)
    private Integer initialAirYear;

    @NotEmpty(message = authorsMessage)
    private List<@NotBlank(message = authorMessage) String> originalMangaAuthors;

    @NotNull(message = isWatchedMessage)
    private Boolean isWatched;

}
