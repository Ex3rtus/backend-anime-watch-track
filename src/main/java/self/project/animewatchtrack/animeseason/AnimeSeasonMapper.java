package self.project.animewatchtrack.animeseason;

/**
 * @author Youssef Kaïdi.
 * created 09 nov. 2022.
 */

public class AnimeSeasonMapper {
    public static AnimeSeasonDTO mapToDTO(AnimeSeason animeSeason) {
        return AnimeSeasonDTO.builder()
                .id(animeSeason.getId())
                .parentAnimeTitle(animeSeason.getAnime().getAnimeTitle())
                .seasonNumber(animeSeason.getSeasonNumber())
                .totalEpisodesCount(animeSeason.getTotalEpisodesCount())
                .currentWatchCount(animeSeason.getCurrentWatchCount())
                .hasBeenWatched(animeSeason.getHasBeenWatched())
                .build();
    }

    public static AnimeSeason mapToEntity(AnimeSeasonCommand animeSeasonCommand) {
        return new AnimeSeason().toBuilder()
                .seasonNumber(animeSeasonCommand.getSeasonNumber())
                .totalEpisodesCount(animeSeasonCommand.getTotalEpisodesCount())
                .currentWatchCount(animeSeasonCommand.getCurrentWatchCount())
                .hasBeenWatched(animeSeasonCommand.getHasBeenWatched())
                .build();
    }
}
