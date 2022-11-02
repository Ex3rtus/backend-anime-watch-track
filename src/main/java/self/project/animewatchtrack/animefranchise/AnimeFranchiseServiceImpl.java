package self.project.animewatchtrack.animefranchise;

import lombok.AllArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import self.project.animewatchtrack.exceptions.AnimeFranchiseNotFoundException;
import self.project.animewatchtrack.exceptions.BadRequestException;

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
        AnimeFranchise animeFranchise = franchiseRepository.findById(franchiseId)
                        .orElseThrow(() -> {
                            String message = "anime franchise with ID : " + franchiseId + " not found";
                            return new AnimeFranchiseNotFoundException(message);
                        });
        return AnimeFranchiseMapper.mapToDTO(animeFranchise);
    }

    @Override
    public AnimeFranchise addAnimeFranchise(AnimeFranchiseCommand animeFranchiseCommand) {
        String franchiseTitle = animeFranchiseCommand.getFranchiseTitle();
        Optional<AnimeFranchise> animeFranchiseOptional = franchiseRepository.findByFranchiseTitle(franchiseTitle);

        if (animeFranchiseOptional.isPresent()) {
            String message = "anime franchise with title : " + franchiseTitle + " already exists";
            throw new BadRequestException(message);
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
                        () -> {
                            String message = "anime franchise with ID : " + franchiseId + " not found";
                            return new AnimeFranchiseNotFoundException(message);
                        }
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
            String message = "anime franchise with ID : " + franchiseId + " not found";
            throw new AnimeFranchiseNotFoundException(message);
        }
        franchiseRepository.deleteById(franchiseId);
    }
}
