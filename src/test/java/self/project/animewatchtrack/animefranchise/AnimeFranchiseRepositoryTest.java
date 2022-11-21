package self.project.animewatchtrack.animefranchise;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

/**
 * @author Youssef Kaïdi.
 * created 27 oct. 2022.
 */

@DataJpaTest
class AnimeFranchiseRepositoryTest {

    @Autowired
    private AnimeFranchiseRepository repositoryUnderTest;

    @AfterEach
    void tearDown() {
        repositoryUnderTest.deleteAll();
    }

    @Test
    void itShouldFindFranchiseByTitle() {
        String thisFranchiseExists = "This Franchise Exists";
        AnimeFranchise animeFranchise = new AnimeFranchise().toBuilder()
                .id(UUID.randomUUID().toString())
                .franchiseTitle(thisFranchiseExists)
                .hasBeenWatched(false)
                .build();
        repositoryUnderTest.save(animeFranchise);

        boolean result = repositoryUnderTest.findByFranchiseTitle(thisFranchiseExists).isPresent();
        assertThat(result).isTrue();
    }

    @Test
    void itShouldFailToFindFranchiseByTitle() {
        String thisFranchiseDoesntExist = "This Franchise Doesn't Exist";
        boolean result = repositoryUnderTest.findByFranchiseTitle(thisFranchiseDoesntExist).isPresent();
        assertThat(result).isFalse();
    }
}