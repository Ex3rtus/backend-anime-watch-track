package self.project.animewatchtrack.animeseason;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import self.project.animewatchtrack.anime.Anime;
import self.project.animewatchtrack.anime.AnimeRepository;
import self.project.animewatchtrack.exceptions.AnimeNotFoundExeption;
import self.project.animewatchtrack.exceptions.AnimeSeasonBadRequest;
import self.project.animewatchtrack.exceptions.AnimeSeasonNotFoundException;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Youssef Kaïdi.
 * created 09 nov. 2022.
 * TODO : Perform data validation
 * TODO : Use logger
 */

@AllArgsConstructor
@Service
public class AnimeSeasonServiceImpl implements AnimeSeasonService {

    private final AnimeRepository animeRepository;
    private final AnimeSeasonRepository seasonRepository;

    @Override
    public AnimeSeasonDTO getById(String seasonId) {
        AnimeSeason animeSeason = seasonRepository.findById(seasonId)
                .orElseThrow(() -> new AnimeSeasonNotFoundException(seasonId));
        return AnimeSeasonMapper.mapToDTO(animeSeason);
    }

    @Override
    public List<AnimeSeasonDTO> getAll() {
        return seasonRepository.findAll()
                .stream()
                .map(AnimeSeasonMapper::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public String addAnimeSeason(String animeId, AnimeSeasonCommand animeSeasonCommand) {
        Integer seasonNumber = animeSeasonCommand.getSeasonNumber();
        Optional<AnimeSeason> optionalAnimeSeason =
                seasonRepository.givenAnimeIdFindBySeasonNumber(animeId, seasonNumber);

        if (optionalAnimeSeason.isPresent()) {
            throw new AnimeSeasonBadRequest(animeId, seasonNumber);
        }

        Anime parentAnime = animeRepository.findById(animeId)
                .orElseThrow(() -> new AnimeNotFoundExeption(animeId));
        AnimeSeason animeSeason = AnimeSeasonMapper.mapToEntity(animeSeasonCommand);
        parentAnime.addSeason(animeSeason);

        return seasonRepository.save(animeSeason).getId();
    }

    @Override
    @Transactional
    public AnimeSeasonDTO updateAnimeSeason(String seasonId,
                                            Integer seasonNumber,Integer totalEpisodesCount) {
        AnimeSeason animeSeason = seasonRepository.findById(seasonId)
                .orElseThrow(() -> new AnimeSeasonNotFoundException(seasonId));

        if (seasonNumber != null && seasonNumber > 0
                && !Objects.equals(seasonNumber, animeSeason.getSeasonNumber())) {
            animeSeason.setSeasonNumber(seasonNumber);
        }

        if (totalEpisodesCount != null && totalEpisodesCount > 0
                && !Objects.equals(totalEpisodesCount, animeSeason.getTotalEpisodesCount())) {
            animeSeason.setTotalEpisodesCount(totalEpisodesCount);
        }

        return AnimeSeasonMapper.mapToDTO(animeSeason);
    }

    @Override
    @Transactional
    public AnimeSeasonDTO markAnimeSeason(String seasonId, Boolean newHasBeenWatched) {
        AnimeSeason animeSeason = seasonRepository.findById(seasonId)
                .orElseThrow(() -> new AnimeSeasonNotFoundException(seasonId));

        if (newHasBeenWatched != null) {
            animeSeason.getStrategyMap()
                    .get(newHasBeenWatched)
                    .markSeason(animeSeason);
        }

        return AnimeSeasonMapper.mapToDTO(animeSeason);
    }

    @Override
    @Transactional
    public AnimeSeasonDTO watchEpisodes(String seasonId, Integer newWatchCount) {
        AnimeSeason animeSeason = seasonRepository.findById(seasonId)
                .orElseThrow(() -> new AnimeSeasonNotFoundException(seasonId));

        if (Objects.equals(newWatchCount, animeSeason.getTotalEpisodesCount())) {
            animeSeason.getStrategyMap()
                    .get(Boolean.TRUE)
                    .markSeason(animeSeason);
        }

        if (newWatchCount != null && newWatchCount > 0
                && newWatchCount < animeSeason.getTotalEpisodesCount()
                && !Objects.equals(newWatchCount, animeSeason.getCurrentWatchCount())) {
            animeSeason.setCurrentWatchCount(newWatchCount);
        }

        return AnimeSeasonMapper.mapToDTO(animeSeason);
    }

    @Override
    public void deleteAnimeSeason(String seasonId) {
        AnimeSeason animeSeason = seasonRepository.findById(seasonId)
                .orElseThrow(() -> new AnimeSeasonNotFoundException(seasonId));
        animeSeason.getAnime()
                .removeSeason(animeSeason);
        seasonRepository.delete(animeSeason);
    }
}