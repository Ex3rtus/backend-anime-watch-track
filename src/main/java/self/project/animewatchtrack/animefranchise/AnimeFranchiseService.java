package self.project.animewatchtrack.animefranchise;

import java.util.List;
import java.util.UUID;

/**
 * @author Youssef Kaïdi.
 * created 26 oct. 2022.
 */

public interface AnimeFranchiseService {

    AnimeFranchise addAnimeFranchise(AnimeFranchiseCommand animeFranchiseCommand);

    List<AnimeFranchiseDTO> getAll();

    AnimeFranchiseDTO getById(String franchiseId);

    AnimeFranchiseDTO updateFranchise(String franchiseId, String franchiseTitle, boolean hasBeenWatched);
}
