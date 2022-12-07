package self.project.animewatchtrack.anime;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import self.project.animewatchtrack.animefranchise.AnimeFranchise;
import self.project.animewatchtrack.animefranchise.AnimeFranchiseRepository;
import self.project.animewatchtrack.exceptions.AnimeBadRequestException;
import self.project.animewatchtrack.exceptions.AnimeFranchiseNotFoundException;
import self.project.animewatchtrack.exceptions.AnimeNotFoundExeption;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Youssef Kaïdi.
 * created 02 nov. 2022.
 * TODO : Perform data validation in service layer
 * TODO : Use logger
 */

@AllArgsConstructor
@Service
public class AnimeServiceImpl implements AnimeService {

    private final AnimeRepository animeRepository;
    private final AnimeFranchiseRepository franchiseRepository;

    @Override
    public AnimeDTO getById(String animeId) {
        Anime anime = animeRepository.findById(animeId)
                .orElseThrow(() -> new AnimeNotFoundExeption(animeId));
        return AnimeMapper.mapToDTO(anime);
    }

    @Override
    public List<AnimeDTO> getAll() {
        return animeRepository.findAll()
                .stream()
                .map(AnimeMapper::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public String addAnime(String franchiseId, AnimeCommand animeCommand) {
        String animeTitle = animeCommand.getAnimeTitle();
        Optional<Anime> animeOptional = animeRepository.findByTitle(animeTitle);

        if (animeOptional.isPresent()) {
            throw new AnimeBadRequestException(animeTitle);
        }

        AnimeFranchise animeFranchise = franchiseRepository.findById(franchiseId)
                .orElseThrow(() -> new AnimeFranchiseNotFoundException(franchiseId));
        Anime anime = AnimeMapper.mapToEntity(animeCommand);
        animeFranchise.addAnime(anime);

        return animeRepository.save(anime).getId();
    }

    @Override
    @Transactional
    public AnimeDTO updateAnime(String animeId, String animeTitle, Integer airYear, List<String> mangaAuthors) {
        Anime anime = animeRepository.findById(animeId)
                .orElseThrow(() -> new AnimeNotFoundExeption(animeId));

        if (animeTitle != null && animeTitle.length() > 0
                && !Objects.equals(animeTitle, anime.getAnimeTitle())) {
            anime.setAnimeTitle(animeTitle);
        }

        if (airYear!= null && airYear > 0
                && !Objects.equals(airYear, anime.getInitialAirYear())) {
            anime.setInitialAirYear(airYear);
        }

        if (!mangaAuthors.isEmpty()
                && !Objects.equals(mangaAuthors, anime.getOriginalMangaAuthors())) {
            anime.setOriginalMangaAuthors(mangaAuthors);
        }

        return AnimeMapper.mapToDTO(anime);
    }

    @Override
    @Transactional
    public AnimeDTO markAnime(String animeId, Boolean newHasBeenWatched) {
        Anime anime = animeRepository.findById(animeId)
                .orElseThrow(() -> new AnimeNotFoundExeption(animeId));

        if (newHasBeenWatched!= null) {
            anime.getStrategyMap()
                    .get(newHasBeenWatched)
                    .markAnimeAndCascadeDown(anime);
        }

        return AnimeMapper.mapToDTO(anime);
    }

    @Override
    public void deleteAnime(String animeId) {
        Anime anime = animeRepository.findById(animeId)
                .orElseThrow(() -> new AnimeNotFoundExeption(animeId));
        anime.getAnimeFranchise()
                .removeAnime(anime);
        animeRepository.deleteById(animeId);
    }
}
