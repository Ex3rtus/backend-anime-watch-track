package self.project.animewatchtrack;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import self.project.animewatchtrack.anime.Anime;
import self.project.animewatchtrack.animefranchise.AnimeFranchise;
import self.project.animewatchtrack.animeseason.AnimeSeason;

import java.util.ArrayList;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

/**
 * @author Youssef Kaïdi.
 * created 18 nov. 2022.
 */

public class AnimeEntitiesHelperMethodsIT {
    private static AnimeFranchise watchedFranchise;
    private static AnimeFranchise unwatchedFranchise;
    private static Anime watchedAnime;
    private static Anime unwatchedAnime;

    private static AnimeSeason watchedSeason1;
    private static AnimeSeason watchedSeason2;
    private static AnimeSeason unwatchedSeason1;
    private static AnimeSeason unwatchedSeason2;

    @BeforeAll
    static void initialize() {
        watchedFranchise = new AnimeFranchise().toBuilder()
                .id(UUID.randomUUID().toString())
                .franchiseTitle("Franchise 1")
                .animes(new ArrayList<>())
                .isWatched(Boolean.TRUE)
                .build();

        unwatchedFranchise = new AnimeFranchise().toBuilder()
                .id(UUID.randomUUID().toString())
                .franchiseTitle("Franchise 2")
                .animes(new ArrayList<>())
                .isWatched(Boolean.FALSE)
                .build();

        watchedAnime = new Anime().toBuilder()
                .id(UUID.randomUUID().toString())
                .animeTitle("Anime 1")
                .isWatched(Boolean.TRUE)
                .seasons(new ArrayList<>())
                .build();

        unwatchedAnime = new Anime().toBuilder()
                .id(UUID.randomUUID().toString())
                .animeTitle("Anime 2")
                .isWatched(Boolean.FALSE)
                .seasons(new ArrayList<>())
                .build();

        watchedSeason1 = new AnimeSeason().toBuilder()
                .id(UUID.randomUUID().toString())
                .seasonNumber(1)
                .totalEpisodesCount(99)
                .currentWatchCount(99)
                .isWatched(Boolean.TRUE)
                .build();

        watchedSeason2 = new AnimeSeason().toBuilder()
                .id(UUID.randomUUID().toString())
                .seasonNumber(2)
                .totalEpisodesCount(89)
                .currentWatchCount(89)
                .isWatched(Boolean.TRUE)
                .build();

        unwatchedSeason1 = new AnimeSeason().toBuilder()
                .id(UUID.randomUUID().toString())
                .seasonNumber(1)
                .totalEpisodesCount(98)
                .currentWatchCount(0)
                .isWatched(Boolean.FALSE)
                .build();

        unwatchedSeason2 = new AnimeSeason().toBuilder()
                .id(UUID.randomUUID().toString())
                .seasonNumber(2)
                .totalEpisodesCount(88)
                .currentWatchCount(0)
                .isWatched(Boolean.FALSE)
                .build();
    }

    @BeforeEach
    void setup() {
        watchedFranchise.getAnimes().clear();
        unwatchedFranchise.getAnimes().clear();

        watchedAnime.getSeasons().clear();
        unwatchedAnime.getSeasons().clear();
    }

    @Test
    void itShouldAddAnimesToFranchise() {
        watchedFranchise.addAnime(watchedAnime);
        unwatchedFranchise.addAnime(unwatchedAnime);

        assertThat(watchedFranchise.getAnimes().size()).isEqualTo(1);
        assertThat(watchedFranchise.getAnimes().contains(watchedAnime)).isTrue();
        assertThat(watchedAnime.getAnimeFranchise()).isEqualTo(watchedFranchise);

        assertThat(unwatchedFranchise.getAnimes().size()).isEqualTo(1);
        assertThat(unwatchedFranchise.getAnimes().contains(unwatchedAnime)).isTrue();
        assertThat(unwatchedAnime.getAnimeFranchise()).isEqualTo(unwatchedFranchise);
    }

    @Test
    void itShouldAddSeasonsToAnime() {
        watchedAnime.addSeason(watchedSeason1);
        watchedAnime.addSeason(watchedSeason2);

        unwatchedAnime.addSeason(unwatchedSeason1);
        unwatchedAnime.addSeason(unwatchedSeason2);

        assertThat(watchedAnime.getSeasons().size()).isEqualTo(2);
        assertThat(watchedAnime.getSeasons().contains(watchedSeason1)).isTrue();
        assertThat(watchedAnime.getSeasons().contains(watchedSeason2)).isTrue();
        assertThat(watchedSeason1.getAnime()).isEqualTo(watchedAnime);
        assertThat(watchedSeason2.getAnime()).isEqualTo(watchedAnime);

        assertThat(unwatchedAnime.getSeasons().size()).isEqualTo(2);
        assertThat(unwatchedAnime.getSeasons().contains(unwatchedSeason1)).isTrue();
        assertThat(unwatchedAnime.getSeasons().contains(unwatchedSeason2)).isTrue();
        assertThat(unwatchedSeason1.getAnime()).isEqualTo(unwatchedAnime);
        assertThat(unwatchedSeason2.getAnime()).isEqualTo(unwatchedAnime);
    }

    @Test
    void itShouldMarkChildrenAsWatched() {
        unwatchedFranchise.addAnime(unwatchedAnime);
        unwatchedAnime.addSeason(unwatchedSeason1);
        unwatchedAnime.addSeason(unwatchedSeason2);

        unwatchedFranchise.getStrategyMap()
                .get(Boolean.TRUE)
                .markFranchiseAndCascadeDown(unwatchedFranchise);

        assertThat(unwatchedFranchise.getIsWatched()).isTrue();
        assertThat(unwatchedAnime.getIsWatched()).isTrue();
        assertThat(unwatchedSeason1.getIsWatched()).isTrue();
        assertThat(unwatchedSeason2.getIsWatched()).isTrue();
        assertThat(unwatchedSeason1.getCurrentWatchCount()).isEqualTo(unwatchedSeason1.getTotalEpisodesCount());
        assertThat(unwatchedSeason2.getCurrentWatchCount()).isEqualTo(unwatchedSeason2.getTotalEpisodesCount());
    }

    @Test
    void itShouldResetChildrenWatch() {
        watchedFranchise.addAnime(watchedAnime);
        watchedAnime.addSeason(watchedSeason1);
        watchedAnime.addSeason(watchedSeason2);

        watchedFranchise.getStrategyMap()
                .get(Boolean.FALSE)
                .markFranchiseAndCascadeDown(watchedFranchise);

        assertThat(watchedFranchise.getIsWatched()).isFalse();
        assertThat(watchedAnime.getIsWatched()).isFalse();
        assertThat(watchedSeason1.getIsWatched()).isFalse();
        assertThat(watchedSeason2.getIsWatched()).isFalse();
        assertThat(watchedSeason1.getCurrentWatchCount()).isEqualTo(0);
        assertThat(watchedSeason2.getCurrentWatchCount()).isEqualTo(0);
    }

    @Test
    void itShouldRemoveAnimesFromFranchise() {
        //given
        watchedFranchise.addAnime(watchedAnime);
        unwatchedFranchise.addAnime(unwatchedAnime);

        //when
        watchedFranchise.removeAnime(watchedAnime);
        unwatchedFranchise.removeAnime(unwatchedAnime);

        //then
        assertThat(watchedFranchise.getAnimes().size()).isEqualTo(0);
        assertThat(watchedFranchise.getAnimes().contains(watchedAnime)).isFalse();
        assertThat(watchedAnime.getAnimeFranchise()).isNull();

        assertThat(unwatchedFranchise.getAnimes().size()).isEqualTo(0);
        assertThat(unwatchedFranchise.getAnimes().contains(unwatchedAnime)).isFalse();
        assertThat(unwatchedAnime.getAnimeFranchise()).isNull();
    }

    @Test
    void itShouldRemoveSeasonsFromAnime() {
        //given
        watchedAnime.addSeason(watchedSeason1);
        watchedAnime.addSeason(watchedSeason2);

        unwatchedAnime.addSeason(unwatchedSeason1);
        unwatchedAnime.addSeason(unwatchedSeason2);

        //when
        watchedAnime.removeSeason(watchedSeason1);
        watchedAnime.removeSeason(watchedSeason2);

        unwatchedAnime.removeSeason(unwatchedSeason1);
        unwatchedAnime.removeSeason(unwatchedSeason2);

        //then
        assertThat(watchedAnime.getSeasons().size()).isEqualTo(0);
        assertThat(watchedAnime.getSeasons().contains(watchedSeason1)).isFalse();
        assertThat(watchedAnime.getSeasons().contains(watchedSeason2)).isFalse();
        assertThat(watchedSeason1.getAnime()).isNull();
        assertThat(watchedSeason2.getAnime()).isNull();

        assertThat(unwatchedAnime.getSeasons().size()).isEqualTo(0);
        assertThat(unwatchedAnime.getSeasons().contains(unwatchedSeason1)).isFalse();
        assertThat(unwatchedAnime.getSeasons().contains(unwatchedSeason2)).isFalse();
        assertThat(unwatchedSeason1.getAnime()).isNull();
        assertThat(unwatchedSeason2.getAnime()).isNull();
    }
}
