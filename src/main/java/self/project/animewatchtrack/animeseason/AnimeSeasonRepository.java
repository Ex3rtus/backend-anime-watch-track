package self.project.animewatchtrack.animeseason;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author Youssef Kaïdi.
 * created 09 nov. 2022.
 */

@Repository
public interface AnimeSeasonRepository extends JpaRepository<AnimeSeason, String> {

    @Query("SELECT s FROM AnimeSeason s WHERE s.anime.id = ?1 and s.seasonNumber = ?2")
    Optional<AnimeSeason> givenAnimeIdFindBySeasonNumber(String animeId, Integer seasonNumber);

}
