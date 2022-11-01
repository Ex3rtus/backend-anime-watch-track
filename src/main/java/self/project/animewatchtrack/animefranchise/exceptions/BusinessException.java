package self.project.animewatchtrack.animefranchise.exceptions;

/**
 * @author Youssef Kaïdi.
 * created 01 nov. 2022.
 */
public abstract class BusinessException extends RuntimeException {
    public abstract ErrorDetails getDetails();
}
