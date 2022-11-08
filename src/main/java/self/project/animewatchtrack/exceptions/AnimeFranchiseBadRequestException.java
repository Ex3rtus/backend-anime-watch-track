package self.project.animewatchtrack.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Youssef Kaïdi.
 * created 01 nov. 2022.
 */

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class AnimeFranchiseBadRequestException extends RuntimeException {
    public AnimeFranchiseBadRequestException(String franchiseTitle) {
        super("anime franchise with title : " + franchiseTitle + " already exists");
    }
}
