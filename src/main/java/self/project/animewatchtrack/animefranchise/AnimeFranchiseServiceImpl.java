package self.project.animewatchtrack.animefranchise;

import lombok.AllArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author Youssef Kaïdi.
 * created 26 oct. 2022.
 */

/* TODO: perform data validation in service layer
 *       add safe logger dependency
 *       use custom exceptions
 */

@AllArgsConstructor
@Service
public class AnimeFranchiseServiceImpl implements AnimeFranchiseService {

    private final AnimeFranchiseRepository franchiseRepository;

    @Override
    public AnimeFranchise addAnimeFranchise(AnimeFranchiseCommand animeFranchiseCommand) {
        String franchiseTitle = animeFranchiseCommand.getFranchiseTitle();
        Optional<AnimeFranchise> animeFranchiseOptional = franchiseRepository.findByFranchiseTitle(franchiseTitle);
        if (animeFranchiseOptional.isPresent()) {
            throw new RuntimeException("anime franchise with title" + franchiseTitle + " already exists");
        }

        AnimeFranchise animeFranchise = AnimeFranchiseMapper.mapToEntity(animeFranchiseCommand);
        return franchiseRepository.save(animeFranchise);
    }
}
