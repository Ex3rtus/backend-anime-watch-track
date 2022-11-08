package self.project.animewatchtrack.animefranchise;

import lombok.*;

/**
 * @author Youssef Kaïdi.
 * created 26 oct. 2022.
 */

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AnimeFranchiseCommand {

    private String franchiseTitle;
    private Boolean hasBeenWatched;

}
