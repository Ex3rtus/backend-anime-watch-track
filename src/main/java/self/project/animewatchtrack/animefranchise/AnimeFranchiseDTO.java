package self.project.animewatchtrack.animefranchise;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Youssef Kaïdi.
 * created 26 oct. 2022.
 */

@Builder
@EqualsAndHashCode
@Getter
public class AnimeFranchiseDTO {
    private String id;
    private String franchiseTitle;
    private Boolean hasBeenWatched;
}
