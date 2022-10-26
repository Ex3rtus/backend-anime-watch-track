package self.project.animewatchtrack.animefranchise;

/**
 * @author Youssef Kaïdi.
 * created 26 oct. 2022.
 */

public class AnimeFranchiseMapper {
    public static AnimeFranchise mapToEntity(AnimeFranchiseCommand animeFranchiseCommand) {
        return AnimeFranchise.builder()
                .franchiseTitle(animeFranchiseCommand.getFranchiseTitle())
                .hasBeenWatched(animeFranchiseCommand.isHasBeenWatched())
                .build();
    }
}
