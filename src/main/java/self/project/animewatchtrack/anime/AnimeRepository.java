package self.project.animewatchtrack.anime;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author Youssef Kaïdi.
 * created 02 nov. 2022.
 */

@Repository
public interface AnimeRepository extends JpaRepository<Anime, String> {
    @Query("SELECT a FROM Anime a WHERE a.animeTitle = ?1")
    Optional<Anime> findByTitle(String animeTitle);
}
