package self.project.animewatchtrack.animeseason;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import self.project.animewatchtrack.anime.AnimeDTO;

import java.util.List;

import static self.project.animewatchtrack.constants.ResourcePaths.*;

/**
 * @author Youssef Kaïdi.
 * created 09 nov. 2022.
 */

@AllArgsConstructor
@CrossOrigin(origins = {"http://localhost:3000"})
@RestController
@RequestMapping(path = API + V1 + ANIME_FRANCHISES + "/{franchiseId}" + ANIMES + "/{animeId}" + ANIME_SEASONS)
public class AnimeSeasonController {

    private final AnimeSeasonService animeSeasonService;

    @GetMapping(path = "{seasonId}")
    public ResponseEntity<AnimeSeasonDTO> getSeasonById(@PathVariable("seasonId") String seasonId) {
        return ResponseEntity.ok(animeSeasonService.getById(seasonId));
    }

    @GetMapping
    public List<AnimeSeasonDTO> getAllAnimeSeasons() {
        return animeSeasonService.getAll();
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> addNewAnimeSeason(@PathVariable("animeId") String animeId,
                                                    @RequestBody AnimeSeasonCommand animeSeasonCommand) {
        String seasonId = animeSeasonService.addAnimeSeason(animeId, animeSeasonCommand);
        return ResponseEntity.status(HttpStatus.CREATED).body(seasonId);
    }

    @PatchMapping(path = "/{seasonId}")
    public ResponseEntity<AnimeSeasonDTO> updateAnimeSeason(@PathVariable("seasonId") String seasonId,
                                                            @RequestParam(required = false, name = "seasonNumber") Integer seasonNumber,
                                                            @RequestParam(required = false, name = "totalEpisodesCount") Integer totalEpisodesCount) {
        AnimeSeasonDTO seasonDTO = animeSeasonService.updateAnimeSeason(seasonId, seasonNumber, totalEpisodesCount);
        return ResponseEntity.ok(seasonDTO);
    }

    @PatchMapping(path = "/{seasonId}" + MARK)
    public ResponseEntity<AnimeSeasonDTO> markAnimeSeason(@PathVariable("seasonId") String seasonId,
                                                          @RequestParam("hasBeenWatched") Boolean newHasBeenWatched) {
        AnimeSeasonDTO seasonDTO = animeSeasonService.markAnimeSeason(seasonId, newHasBeenWatched);
        return ResponseEntity.ok(seasonDTO);
    }

    @PatchMapping(path = "/{seasonId}" + WATCH)
    public ResponseEntity<AnimeSeasonDTO> watchEpisodes(@PathVariable("seasonId") String seasonId,
                                                        @RequestParam("watchCount") Integer newWatchCount) {
        AnimeSeasonDTO seasonDTO = animeSeasonService.watchEpisodes(seasonId, newWatchCount);
        return ResponseEntity.ok(seasonDTO);
    }

    @DeleteMapping(path = "/{seasonId}")
    public ResponseEntity<Object> deleteAnimeSeason(@PathVariable("seasonId") String seasonId) {
        animeSeasonService.deleteAnimeSeason(seasonId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
