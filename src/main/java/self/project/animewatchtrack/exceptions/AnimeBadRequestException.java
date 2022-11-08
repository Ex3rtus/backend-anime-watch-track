package self.project.animewatchtrack.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Youssef Kaïdi.
 * created 03 nov. 2022.
 */

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class AnimeBadRequestException extends RuntimeException {
    public AnimeBadRequestException(String animeTitle) {
        super("anime with title : " + animeTitle + " already exists");
    }
}
