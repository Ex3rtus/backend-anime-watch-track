package self.project.animewatchtrack.animefranchise;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
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
@RequestMapping(path = API + V1 + ANIME_FRANCHISES)
public class AnimeFranchiseController {

    private final AnimeFranchiseService animeFranchiseService;

    @GetMapping(path = "/{franchiseId}")
    public ResponseEntity<AnimeFranchiseDTO> getFranchiseById(@PathVariable("franchiseId") String franchiseId) {
        return ResponseEntity.ok(animeFranchiseService.getById(franchiseId));
    }

    @GetMapping
    public List<AnimeFranchiseDTO> getAllFranchises() {
        return animeFranchiseService.getAll();
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> addNewAnimeFranchise(@RequestBody AnimeFranchiseCommand animeFranchiseCommand) {
        String animeFranchiseId = animeFranchiseService.addAnimeFranchise(animeFranchiseCommand);
        return ResponseEntity.status(HttpStatus.CREATED).body(animeFranchiseId);
    }

    @PatchMapping(path = "/{franchiseId}")
    public ResponseEntity<AnimeFranchiseDTO> updateAnimeFranchise(@PathVariable("franchiseId") String franchiseId,
                                                                  @RequestParam("franchiseTitle") String franchiseTitle) {
        AnimeFranchiseDTO responseDTO = animeFranchiseService.updateFranchise(franchiseId, franchiseTitle);
        return ResponseEntity.ok(responseDTO);
    }

    @PatchMapping(path = "/{franchiseId}" + MARK)
    public ResponseEntity<AnimeFranchiseDTO> markAnimeFranchise(@PathVariable("franchiseId") String franchiseId,
                                                                @RequestParam("hasBeenWatched") Boolean newHasBeenWatched) {
        AnimeFranchiseDTO responseDTO = animeFranchiseService.markFranchise(franchiseId, newHasBeenWatched);
        return ResponseEntity.ok(responseDTO);
    }

    @DeleteMapping(path = "/{franchiseId}")
    public ResponseEntity<Object> deleteAnimeFranchise(@PathVariable("franchiseId") String franchiseId) {
        animeFranchiseService.deleteAnimeFranchise(franchiseId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
