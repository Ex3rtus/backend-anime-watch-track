package self.project.animewatchtrack.animefranchise.exceptions;

import org.springframework.http.HttpStatus;

/**
 * @author Youssef Kaïdi.
 * created 01 nov. 2022.
 */
public class AnimeFranchiseNotFoundException extends BusinessException {

    private final ErrorDetails errorDetails;

    public AnimeFranchiseNotFoundException(String message) {
        this.errorDetails = new ErrorDetails(
                HttpStatus.NOT_FOUND,
                message
        );
    }

    @Override
    public ErrorDetails getDetails() {
        return errorDetails;
    }
}
