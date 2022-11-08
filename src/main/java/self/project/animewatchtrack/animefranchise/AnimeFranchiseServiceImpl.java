package self.project.animewatchtrack.animefranchise;

import lombok.AllArgsConstructor;

import org.springframework.stereotype.Service;
import self.project.animewatchtrack.exceptions.AnimeFranchiseNotFoundException;
import self.project.animewatchtrack.exceptions.AnimeFranchiseBadRequestException;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Youssef Kaïdi.
 * created 26 oct. 2022.
 * TODO : Perform data validation
 * TODO : Add safe logger dependency
 */

@AllArgsConstructor
@Service
public class AnimeFranchiseServiceImpl implements AnimeFranchiseService {
    private final AnimeFranchiseRepository franchiseRepository;

    @Override
    public AnimeFranchiseDTO getById(String franchiseId) {
        AnimeFranchise animeFranchise = franchiseRepository.findById(franchiseId)
                        .orElseThrow(() -> new AnimeFranchiseNotFoundException(franchiseId));
        return AnimeFranchiseMapper.mapToDTO(animeFranchise);
    }

    @Override
    public List<AnimeFranchiseDTO> getAll() {
        return franchiseRepository.findAll()
                .stream().map(AnimeFranchiseMapper::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public String addAnimeFranchise(AnimeFranchiseCommand animeFranchiseCommand) {
        String franchiseTitle = animeFranchiseCommand.getFranchiseTitle();
        Optional<AnimeFranchise> animeFranchiseOptional = franchiseRepository.findByFranchiseTitle(franchiseTitle);

        if (animeFranchiseOptional.isPresent()) {
            throw new AnimeFranchiseBadRequestException(franchiseTitle);
        }

        AnimeFranchise animeFranchise = AnimeFranchiseMapper.mapToEntity(animeFranchiseCommand);
        return franchiseRepository.save(animeFranchise).getId();
    }

    @Override
    @Transactional
    public AnimeFranchiseDTO updateFranchise(String franchiseId,
                                             String newFranchiseTitle,
                                             Boolean newHasBeenWatched) {

        AnimeFranchise animeFranchise = franchiseRepository.findById(franchiseId)
                .orElseThrow(() -> new AnimeFranchiseNotFoundException(franchiseId));

        if (newFranchiseTitle != null
                && newFranchiseTitle.length() > 0
                && !Objects.equals(newFranchiseTitle, animeFranchise.getFranchiseTitle())) {
            animeFranchise.setFranchiseTitle(newFranchiseTitle);
        }

        if (newHasBeenWatched != animeFranchise.getHasBeenWatched()) {
            animeFranchise.setHasBeenWatched(newHasBeenWatched);
            animeFranchise.getAnimes()
                    .forEach(anime -> anime.setHasBeenWatched(newHasBeenWatched));
        }

        return AnimeFranchiseMapper.mapToDTO(animeFranchise);
    }

    @Override
    public void deleteAnimeFranchise(String franchiseId) {
        if (!franchiseRepository.existsById(franchiseId)) {
            throw new AnimeFranchiseNotFoundException(franchiseId);
        }
        franchiseRepository.deleteById(franchiseId);
    }
}
