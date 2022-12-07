package self.project.animewatchtrack.anime;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.List;

/**
 * @author Youssef Kaïdi.
 * created 02 nov. 2022.
 */

@Builder
@Getter
@EqualsAndHashCode
public class AnimeDTO {
    private String id;
    private String parentFranchiseTitle;
    private String animeTitle;
    private Integer initialAirYear;
    private List<String> originalMangaAuthors;
    private Boolean hasBeenWatched;
}

