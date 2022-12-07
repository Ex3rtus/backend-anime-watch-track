package self.project.animewatchtrack.anime;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static self.project.animewatchtrack.constants.ResourcePaths.*;

/**
 * @author Youssef Kaïdi.
 * created 02 nov. 2022.
 */

@AllArgsConstructor
@RestController
@CrossOrigin(origins = {"http://localhost:3000"})
@RequestMapping(path = API + V1 + ANIME_FRANCHISES + "/{franchiseId}" + ANIMES)
public class AnimeController {

    private final AnimeService animeService;

    @GetMapping(path ="/{animeId}")
    public ResponseEntity<AnimeDTO> getAnimeById(@PathVariable("animeId") String animeId) {
        return ResponseEntity.ok(animeService.getById(animeId));
    }

    @GetMapping
    public List<AnimeDTO> getAllAnimes() {
        return animeService.getAll();
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> addNewAnime(@PathVariable("franchiseId") String franchiseId,
                                              @RequestBody AnimeCommand animeCommand) {

        String animeId = animeService.addAnime(franchiseId, animeCommand);
        return ResponseEntity.status(HttpStatus.CREATED).body(animeId);

    }

    @PatchMapping(path = "/{animeId}")
    public ResponseEntity<AnimeDTO> updateAnime(@PathVariable("animeId") String animeId,
                                                @RequestParam(required = false, name = "animeTitle") String animeTitle,
                                                @RequestParam(required = false, name = "airYear") Integer airYear,
                                                @RequestParam(required = false, name = "mangaAuthors") List<String> mangaAuthors) {

        AnimeDTO responseDTO = animeService.updateAnime(animeId, animeTitle, airYear, mangaAuthors);
        return ResponseEntity.ok(responseDTO);
    }

    @PatchMapping(path ="/{animeId}" + MARK)
    public ResponseEntity<AnimeDTO> markAnime(@PathVariable("animeId") String animeId,
                                              @RequestParam("hasBeenWatched") Boolean newHasBeenWatched) {
        AnimeDTO responseDTO = animeService.markAnime(animeId, newHasBeenWatched);
        return ResponseEntity.ok(responseDTO);
    }

    @DeleteMapping(path ="/{animeId}")
    public ResponseEntity<Object> deleteAnime(@PathVariable("animeId") String animeId) {
        animeService.deleteAnime(animeId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
