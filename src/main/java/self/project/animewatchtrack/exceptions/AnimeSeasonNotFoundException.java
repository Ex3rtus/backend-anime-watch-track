package self.project.animewatchtrack.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Youssef Kaïdi.
 * created 09 nov. 2022.
 */

@ResponseStatus(HttpStatus.NOT_FOUND)
public class AnimeSeasonNotFoundException extends RuntimeException {

    public AnimeSeasonNotFoundException(String seasonId) {
        super("anime season with ID : " + seasonId + " not found");
    }

}
