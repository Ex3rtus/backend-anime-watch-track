package self.project.animewatchtrack.animefranchise;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

/**
 * @author Youssef Kaïdi.
 * created 26 oct. 2022.
 */

@Builder
@EqualsAndHashCode
@Setter
@Getter
public class AnimeFranchiseDTO {
    private String id;
    private String franchiseTitle;
    private boolean hasBeenWatched;
}
