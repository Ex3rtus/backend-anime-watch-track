package self.project.animewatchtrack.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * @author Youssef Kaïdi.
 * created 18 déc. 2022.
 */

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class AnimeValidationMessages {
    public static final String titleMessage = "Anime title value cannot be blank";
    public static final String yearMessage = "Air year must be a positive number";
    public static final String authorsMessage = "Authors list cannot be empty";
    public static final String authorMessage = "Manga author cannot be blank";
    public static final String isWatchedMessage = "Anime watch mark cannot be null";
}
