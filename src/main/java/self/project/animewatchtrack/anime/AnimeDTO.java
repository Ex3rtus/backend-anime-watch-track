package self.project.animewatchtrack.anime;

import lombok.*;

import java.util.List;


/**
 * @author Youssef Kaïdi.
 * created 02 nov. 2022.
 */

@Builder
@EqualsAndHashCode
@Getter
public class AnimeDTO {
    private String id;
    private String parentFranchiseTitle;
    private String animeTitle;
    private Integer initialAirYear;
    private List<String> originalMangaAuthors;
    private Boolean hasBeenWatched;
}
