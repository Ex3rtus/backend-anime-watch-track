package self.project.animewatchtrack.animefranchise;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import static self.project.animewatchtrack.constants.FranchiseValidationMessages.*;

/**
 * @author Youssef Kaïdi.
 * created 26 oct. 2022.
 */

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AnimeFranchiseCommand {

    @NotBlank(message = titleMessage)
    private String franchiseTitle;

    @NotNull(message = isWatchedMessage)
    private Boolean isWatched;

}
