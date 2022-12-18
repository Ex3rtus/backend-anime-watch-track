package self.project.animewatchtrack.animeseason;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import self.project.animewatchtrack.validation.constraints.NullOrPositive;
import self.project.animewatchtrack.validation.constraints.UUIDURI;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

import static self.project.animewatchtrack.constants.ResourcePaths.*;
import static self.project.animewatchtrack.constants.SeasonValidationMessages.*;

/**
 * @author Youssef Kaïdi.
 * created 09 nov. 2022.
 */

@AllArgsConstructor
@CrossOrigin(origins = {"http://localhost:3000"})
@Validated
@RestController
@RequestMapping(path = API + V1 + ANIME_FRANCHISES + "/{franchiseId}" + ANIMES + "/{animeId}" + ANIME_SEASONS)
public class AnimeSeasonController {

    private final AnimeSeasonService animeSeasonService;

    @GetMapping(path = "{seasonId}")
    public ResponseEntity<AnimeSeasonDTO> getSeasonById(@UUIDURI @PathVariable("seasonId") String seasonId) {
        return ResponseEntity.ok(animeSeasonService.getById(seasonId));
    }

    @GetMapping
    public List<AnimeSeasonDTO> getAllAnimeSeasons() {
        return animeSeasonService.getAll();
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> addNewAnimeSeason(@UUIDURI @PathVariable("animeId") String animeId,
                                                    @Valid @RequestBody AnimeSeasonCommand animeSeasonCommand) {
        String seasonId = animeSeasonService.addAnimeSeason(animeId, animeSeasonCommand);
        return ResponseEntity.status(HttpStatus.CREATED).body(seasonId);
    }

    @PatchMapping(path = "/{seasonId}")
    public ResponseEntity<AnimeSeasonDTO> updateAnimeSeason(@UUIDURI @PathVariable("seasonId") String seasonId,
                                                            @NullOrPositive(message = numberMessage)
                                                            @RequestParam(required = false, name = "seasonNumber")
                                                                Integer seasonNumber,
                                                            @NullOrPositive(message = totalEpisodesMessage)
                                                            @RequestParam(required = false, name = "totalEpisodesCount")
                                                                Integer totalEpisodesCount) {
        AnimeSeasonDTO seasonDTO = animeSeasonService.updateAnimeSeason(seasonId, seasonNumber, totalEpisodesCount);
        return ResponseEntity.ok(seasonDTO);
    }

    @PatchMapping(path = "/{seasonId}" + MARK)
    public ResponseEntity<AnimeSeasonDTO> markAnimeSeason(@UUIDURI @PathVariable("seasonId") String seasonId,
                                                          @NotNull(message = isWatchedMessage)
                                                          @RequestParam("isWatched") Boolean isWatched) {
        AnimeSeasonDTO seasonDTO = animeSeasonService.markAnimeSeason(seasonId, isWatched);
        return ResponseEntity.ok(seasonDTO);
    }

    @PatchMapping(path = "/{seasonId}" + WATCH)
    public ResponseEntity<AnimeSeasonDTO> watchEpisodes(@UUIDURI @PathVariable("seasonId") String seasonId,
                                                        @PositiveOrZero(message = watchCountMessage)
                                                        @RequestParam("watchCount") Integer newWatchCount) {
        AnimeSeasonDTO seasonDTO = animeSeasonService.watchEpisodes(seasonId, newWatchCount);
        return ResponseEntity.ok(seasonDTO);
    }

    @DeleteMapping(path = "/{seasonId}")
    public ResponseEntity<Object> deleteAnimeSeason(@UUIDURI @PathVariable("seasonId") String seasonId) {
        animeSeasonService.deleteAnimeSeason(seasonId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
