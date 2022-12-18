package self.project.animewatchtrack.anime;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import self.project.animewatchtrack.validation.constraints.NullOrNotEmpty;
import self.project.animewatchtrack.validation.constraints.NullOrNotEmptyList;
import self.project.animewatchtrack.validation.constraints.NullOrPositive;
import self.project.animewatchtrack.validation.constraints.UUIDURI;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

import static self.project.animewatchtrack.constants.AnimeValidationMessages.*;
import static self.project.animewatchtrack.constants.ResourcePaths.*;

/**
 * @author Youssef Kaïdi.
 * created 02 nov. 2022.
 */

@AllArgsConstructor
@CrossOrigin(origins = {"http://localhost:3000"})
@Validated
@RestController
@RequestMapping(path = API + V1 + ANIME_FRANCHISES + "/{franchiseId}" + ANIMES)
public class AnimeController {

    private final AnimeService animeService;

    @GetMapping(path = "/{animeId}")
    public ResponseEntity<AnimeDTO> getAnimeById(@UUIDURI @PathVariable("animeId") String animeId) {
        return ResponseEntity.ok(animeService.getById(animeId));
    }

    @GetMapping
    public List<AnimeDTO> getAllAnimes() {
        return animeService.getAll();
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> addNewAnime(@UUIDURI @PathVariable("franchiseId") String franchiseId,
                                              @Valid @RequestBody AnimeCommand animeCommand) {
        String animeId = animeService.addAnime(franchiseId, animeCommand);
        return ResponseEntity.status(HttpStatus.CREATED).body(animeId);
    }

    @PatchMapping(path = "/{animeId}")
    public ResponseEntity<AnimeDTO> updateAnime(@UUIDURI @PathVariable("animeId")
                                                    String animeId,
                                                @NullOrNotEmpty(message = titleMessage)
                                                @RequestParam(required = false, name = "animeTitle")
                                                    String animeTitle,
                                                @NullOrPositive(message = yearMessage)
                                                @RequestParam(required = false, name = "airYear")
                                                    Integer airYear,
                                                @NullOrNotEmptyList(message = authorsMessage)
                                                @RequestParam(required = false, name = "mangaAuthors")
                                                    List<@NotBlank(message = authorMessage) String> mangaAuthors) {

        AnimeDTO responseDTO = animeService.updateAnime(animeId, animeTitle, airYear, mangaAuthors);
        return ResponseEntity.ok(responseDTO);
    }

    @PatchMapping(path = "/{animeId}" + MARK)
    public ResponseEntity<AnimeDTO> markAnime(@UUIDURI @PathVariable("animeId") String animeId,
                                              @NotNull(message = isWatchedMessage)
                                              @RequestParam("isWatched") Boolean isWatched) {
        AnimeDTO responseDTO = animeService.markAnime(animeId, isWatched);
        return ResponseEntity.ok(responseDTO);
    }

    @DeleteMapping(path = "/{animeId}")
    public ResponseEntity<Object> deleteAnime(@UUIDURI @PathVariable("animeId") String animeId) {
        animeService.deleteAnime(animeId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}

