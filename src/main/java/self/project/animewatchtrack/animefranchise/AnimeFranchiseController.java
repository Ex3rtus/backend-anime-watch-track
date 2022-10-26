package self.project.animewatchtrack.animefranchise;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import self.project.animewatchtrack.contants.ResourcePaths;

/**
 * @author Youssef Kaïdi.
 * created 26 oct. 2022.
 */

// TODO : add global config for CORS

@AllArgsConstructor
@CrossOrigin(origins = {"http://localhost:3000"})
@RestController
@RequestMapping(path = ResourcePaths.API + ResourcePaths.V1 + ResourcePaths.ANIME_FRANCHISE)
public class AnimeFranchiseController {

    private final AnimeFranchiseService animeFranchiseService;

    @PostMapping
    public ResponseEntity<Object> addNewAnimeFranchise(@RequestBody AnimeFranchiseCommand animeFranchiseCommand) {
        animeFranchiseService.addAnimeFranchise(animeFranchiseCommand);
        return ResponseEntity.ok().build();
    }
}
