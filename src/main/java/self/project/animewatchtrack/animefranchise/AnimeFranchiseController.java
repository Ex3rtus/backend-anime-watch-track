package self.project.animewatchtrack.animefranchise;

import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static self.project.animewatchtrack.constants.ResourcePaths.*;

/**
 * @author Youssef Kaïdi.
 * created 26 oct. 2022.
 * TODO : add global config for CORS
 */

@AllArgsConstructor
@CrossOrigin(origins = {"http://localhost:3000"})
@RestController
@RequestMapping(path = API + V1 + ANIME_FRANCHISE)
public class AnimeFranchiseController {

    private final AnimeFranchiseService animeFranchiseService;

    @GetMapping
    public List<AnimeFranchiseDTO> getAllFranchises() {
        return animeFranchiseService.getAll();
    }

    @GetMapping(path = "/{franchiseId}")
    public ResponseEntity<AnimeFranchiseDTO> getFranchiseById(@PathVariable("franchiseId") String franchiseId) {
        return ResponseEntity.ok(animeFranchiseService.getById(franchiseId));
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> addNewAnimeFranchise(@RequestBody AnimeFranchiseCommand animeFranchiseCommand) {
        AnimeFranchise animeFranchise = animeFranchiseService.addAnimeFranchise(animeFranchiseCommand);
        return ResponseEntity.ok(animeFranchise.getId());
    }
}
