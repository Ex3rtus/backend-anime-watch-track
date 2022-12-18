package self.project.animewatchtrack.anime;

/**
 * @author Youssef Kaïdi.
 * created 02 nov. 2022.
 */

public class AnimeMapper {

    public static Anime mapToEntity(AnimeCommand animeCommand) {
        return new Anime().toBuilder()
                .animeTitle(animeCommand.getAnimeTitle())
                .initialAirYear(animeCommand.getInitialAirYear())
                .originalMangaAuthors(animeCommand.getOriginalMangaAuthors())
                .isWatched(animeCommand.getIsWatched())
                .build();
    }
    public static AnimeDTO mapToDTO(Anime anime) {
        return AnimeDTO.builder()
                .id(anime.getId())
                .parentFranchiseTitle(anime.getAnimeFranchise().getFranchiseTitle())
                .animeTitle(anime.getAnimeTitle())
                .initialAirYear(anime.getInitialAirYear())
                .originalMangaAuthors(anime.getOriginalMangaAuthors())
                .isWatched(anime.getIsWatched())
                .build();
    }
}
