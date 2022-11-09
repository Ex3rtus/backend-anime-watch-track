package self.project.animewatchtrack.animefranchise;

import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

/**
 * @author Youssef Kaïdi.
 * created 26 oct. 2022.
 */

public interface AnimeFranchiseService {

    AnimeFranchiseDTO getById(String franchiseId);

    List<AnimeFranchiseDTO> getAll();

    String addAnimeFranchise(AnimeFranchiseCommand animeFranchiseCommand);

    AnimeFranchiseDTO updateFranchise(String franchiseId, String franchiseTitle, Boolean hasBeenWatched);

    void deleteAnimeFranchise(String franchiseId);
}
