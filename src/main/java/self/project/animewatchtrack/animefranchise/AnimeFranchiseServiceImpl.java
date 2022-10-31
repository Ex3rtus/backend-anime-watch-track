package self.project.animewatchtrack.animefranchise;

import lombok.AllArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Youssef Kaïdi.
 * created 26 oct. 2022.
 * TODO: perform data validation in service layer
 * TODO: add safe logger dependency
 * TODO: use custom exceptions
 */

@AllArgsConstructor
@Service
public class AnimeFranchiseServiceImpl implements AnimeFranchiseService {
    private final AnimeFranchiseRepository franchiseRepository;

    @Override
    public List<AnimeFranchiseDTO> getAll() {
        return franchiseRepository.findAll()
                .stream().map(AnimeFranchiseMapper::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public AnimeFranchiseDTO getById(String franchiseId) {
        AnimeFranchise animeFranchise =
                franchiseRepository.findById(franchiseId)
                        .orElseThrow(() -> new RuntimeException("anime franchise with ID : " + franchiseId + " not found"));
        return AnimeFranchiseMapper.mapToDTO(animeFranchise);
    }

    @Override
    public AnimeFranchise addAnimeFranchise(AnimeFranchiseCommand animeFranchiseCommand) {
        String franchiseTitle = animeFranchiseCommand.getFranchiseTitle();
        Optional<AnimeFranchise> animeFranchiseOptional = franchiseRepository.findByFranchiseTitle(franchiseTitle);

        if (animeFranchiseOptional.isPresent()) {
            throw new RuntimeException("anime franchise with title " + franchiseTitle + " already exists");
        }

        AnimeFranchise animeFranchise = AnimeFranchiseMapper.mapToEntity(animeFranchiseCommand);
        return franchiseRepository.save(animeFranchise);
    }

    @Override
    @Transactional
    public AnimeFranchiseDTO updateFranchise(String franchiseId,
                                             String newFranchiseTitle,
                                             boolean newHasBeenWatched) {
        AnimeFranchise animeFranchise = franchiseRepository.findById(franchiseId)
                .orElseThrow(
                        () -> new RuntimeException("anime franchise with ID : " + franchiseId + " not found")
                );
        if (newFranchiseTitle != null
                && newFranchiseTitle.length() > 0
        && !Objects.equals(newFranchiseTitle, animeFranchise.getFranchiseTitle())) {
            animeFranchise.setFranchiseTitle(newFranchiseTitle);
        }

        if (newHasBeenWatched != animeFranchise.isHasBeenWatched()) {
            animeFranchise.setHasBeenWatched(newHasBeenWatched);
        }

        return AnimeFranchiseMapper.mapToDTO(animeFranchise);
    }

    @Override
    public void deleteAnimeFranchise(String franchiseId) {
        if (!franchiseRepository.existsById(franchiseId)) {
            throw new RuntimeException("anime franchise with ID : " + franchiseId + " not found");
        }
        franchiseRepository.deleteById(franchiseId);
    }
}
