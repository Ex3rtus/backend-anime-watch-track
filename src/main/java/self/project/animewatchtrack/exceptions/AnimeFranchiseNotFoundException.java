package self.project.animewatchtrack.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Youssef Kaïdi.
 * created 01 nov. 2022.
 */

@ResponseStatus(HttpStatus.NOT_FOUND)
public class AnimeFranchiseNotFoundException extends RuntimeException {

    public AnimeFranchiseNotFoundException(String message) {
        super(message);
    }
}
