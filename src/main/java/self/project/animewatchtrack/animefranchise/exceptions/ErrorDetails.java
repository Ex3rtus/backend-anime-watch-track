package self.project.animewatchtrack.animefranchise.exceptions;

import lombok.AllArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

/**
 * @author Youssef Kaïdi.
 * created 01 nov. 2022.
 */

public class ErrorDetails {
    private final LocalDateTime timestamp = LocalDateTime.now();
    private HttpStatus code;
    private final String message;

    public ErrorDetails(HttpStatus code, String message) {
        this.code = code;
        this.message = message;
    }

}
