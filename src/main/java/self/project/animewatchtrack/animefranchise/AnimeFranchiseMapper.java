package self.project.animewatchtrack.animefranchise;

/**
 * @author Youssef Kaïdi.
 * created 26 oct. 2022.
 */

public class AnimeFranchiseMapper {
    public static AnimeFranchise mapToEntity(AnimeFranchiseCommand animeFranchiseCommand) {
        return new AnimeFranchise().toBuilder()
                .franchiseTitle(animeFranchiseCommand.getFranchiseTitle())
                .isWatched(animeFranchiseCommand.getIsWatched())
                .build();
    }

    public static AnimeFranchiseDTO mapToDTO(AnimeFranchise animeFranchise) {
        return AnimeFranchiseDTO.builder()
                .id(animeFranchise.getId())
                .franchiseTitle(animeFranchise.getFranchiseTitle())
                .isWatched(animeFranchise.getIsWatched())
                .build();
    }
}
