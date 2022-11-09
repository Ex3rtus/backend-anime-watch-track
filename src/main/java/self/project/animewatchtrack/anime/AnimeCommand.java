package self.project.animewatchtrack.anime;

import lombok.*;

import java.util.List;


/**
 * @author Youssef Kaïdi.
 * created 02 nov. 2022.
 */

@AllArgsConstructor
@Builder
@Getter
public class AnimeCommand {

    private String animeTitle;
    private Integer initialAirYear;
    private List<String> originalMangaAuthors;
    private Boolean hasBeenWatched;

}
