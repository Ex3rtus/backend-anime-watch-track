package self.project.animewatchtrack.animefranchise;

import java.util.ArrayList;

/**
 * @author Youssef Kaïdi.
 * created 26 oct. 2022.
 */

public class AnimeFranchiseMapper {
    public static AnimeFranchise mapToEntity(AnimeFranchiseCommand animeFranchiseCommand) {
        return AnimeFranchise.builder()
                .franchiseTitle(animeFranchiseCommand.getFranchiseTitle())
                .hasBeenWatched(animeFranchiseCommand.getHasBeenWatched())
                .build();
    }

    public static AnimeFranchiseDTO mapToDTO(AnimeFranchise animeFranchise) {
        return AnimeFranchiseDTO.builder()
                .id(animeFranchise.getId())
                .franchiseTitle(animeFranchise.getFranchiseTitle())
                .hasBeenWatched(animeFranchise.getHasBeenWatched())
                .build();
    }
}
