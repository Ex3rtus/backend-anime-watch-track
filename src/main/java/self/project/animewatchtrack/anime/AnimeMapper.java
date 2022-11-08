package self.project.animewatchtrack.anime;

import self.project.animewatchtrack.animefranchise.AnimeFranchise;

/**
 * @author Youssef Kaïdi.
 * created 02 nov. 2022.
 */

public class AnimeMapper {

    public static Anime mapToEntity(AnimeCommand animeCommand) {
        return Anime.builder()
                .animeTitle(animeCommand.getAnimeTitle())
                .initialAirYear(animeCommand.getInitialAirYear())
                .originalMangaAuthors(animeCommand.getOriginalMangaAuthors())
                .hasBeenWatched(animeCommand.getHasBeenWatched())
                .build();
    }
    public static AnimeDTO mapToDTO(Anime anime) {
        return AnimeDTO.builder()
                .id(anime.getId())
                .parentFranchiseTitle(anime.getAnimeFranchise().getFranchiseTitle())
                .animeTitle(anime.getAnimeTitle())
                .initialAirYear(anime.getInitialAirYear())
                .originalMangaAuthors(anime.getOriginalMangaAuthors())
                .hasBeenWatched(anime.getHasBeenWatched())
                .build();
    }
}
