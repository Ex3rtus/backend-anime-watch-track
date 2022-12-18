package self.project.animewatchtrack.animefranchise;

import java.util.List;

/**
 * @author Youssef Kaïdi.
 * created 26 oct. 2022.
 */

public interface AnimeFranchiseService {

    AnimeFranchiseDTO getById(String franchiseId);

    List<AnimeFranchiseDTO> getAll();

    String addAnimeFranchise(AnimeFranchiseCommand animeFranchiseCommand);

    AnimeFranchiseDTO updateFranchise(String franchiseId, String franchiseTitle);

    AnimeFranchiseDTO markFranchise(String franchiseId, Boolean isWatched);

    void deleteAnimeFranchise(String franchiseId);
}
