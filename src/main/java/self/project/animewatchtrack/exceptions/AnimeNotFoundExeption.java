package self.project.animewatchtrack.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Youssef Kaïdi.
 * created 03 nov. 2022.
 */

@ResponseStatus(HttpStatus.NOT_FOUND)
public class AnimeNotFoundExeption extends RuntimeException {
    public AnimeNotFoundExeption(String animeId) {
        super("anime with ID : " + animeId + " not found");
    }
}
