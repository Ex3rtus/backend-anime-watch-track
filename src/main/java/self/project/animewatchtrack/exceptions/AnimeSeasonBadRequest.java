package self.project.animewatchtrack.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Youssef Kaïdi.
 * created 09 nov. 2022.
 */

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class AnimeSeasonBadRequest extends RuntimeException {

    public AnimeSeasonBadRequest(String animeId, Integer seasonNumber) {
        super("anime season number : " + seasonNumber + " already exists for anime with id : " + animeId);
    }

}
