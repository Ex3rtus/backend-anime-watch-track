package self.project.animewatchtrack.animefranchise;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author Youssef Kaïdi.
 * created 26 oct. 2022.
 */

@Repository
public interface AnimeFranchiseRepository extends JpaRepository<AnimeFranchise, String> {

    @Query("SELECT f FROM AnimeFranchise f WHERE f.franchiseTitle = ?1")
    Optional<AnimeFranchise> findByFranchiseTitle(String franchiseTitle);

}
