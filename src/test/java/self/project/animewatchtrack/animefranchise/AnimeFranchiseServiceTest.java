package self.project.animewatchtrack.animefranchise;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import self.project.animewatchtrack.exceptions.AnimeFranchiseBadRequestException;
import self.project.animewatchtrack.exceptions.AnimeFranchiseNotFoundException;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * @author Youssef Kaïdi.
 * created 28 oct. 2022.
 */

@ExtendWith(MockitoExtension.class)
class AnimeFranchiseServiceTest {
    @Mock
    private AnimeFranchiseRepository franchiseRepository;
    private AnimeFranchiseServiceImpl underTest;

    @BeforeEach
    void setUp() {
        underTest = new AnimeFranchiseServiceImpl(franchiseRepository);
    }

    @Test
    void itShouldGetFranchiseById() {
        String id = UUID.randomUUID().toString();
        AnimeFranchise franchiseToFind = AnimeFranchise.builder()
                .id(id)
                .franchiseTitle("Franchise To Find")
                .hasBeenWatched(false)
                .build();

        AnimeFranchiseDTO expected = AnimeFranchiseMapper.mapToDTO(franchiseToFind);

        when(franchiseRepository.findById(franchiseToFind.getId()))
                .thenReturn(Optional.of(franchiseToFind));

        assertThat(underTest.getById(id)).isEqualTo(expected);
        verify(franchiseRepository).findById(id);
    }

    @Test
    void itShouldThrowWhenAttemptingToFindFranchiseByInvalidId() {
        String fakeUUID = "Believe me, i'm a uuid turned into a string";
        String exceptionMessage = "anime franchise with ID : " + fakeUUID + " not found";
        AnimeFranchiseNotFoundException exceptionToBeThrown =
                new AnimeFranchiseNotFoundException(fakeUUID);

        when(franchiseRepository.findById(fakeUUID))
                .thenThrow(exceptionToBeThrown);

        assertThatThrownBy(() -> underTest.getById(fakeUUID))
                .isInstanceOf(AnimeFranchiseNotFoundException.class)
                .hasMessageContaining(exceptionMessage);
    }

    @Test
    void itShouldReturnAnEmptyAnimeFranchiseList() {
        List<AnimeFranchiseDTO> result = underTest.getAll();
        verify(franchiseRepository).findAll();
        assertThat(result.isEmpty()).isTrue();
    }

    @Test
    void itShouldReturnAPopulatedAnimeFranchiseList() {
        AnimeFranchise franchise1 = AnimeFranchise.builder()
                .id(UUID.randomUUID().toString())
                .franchiseTitle("Franchise To Get 1")
                .hasBeenWatched(false)
                .build();

        AnimeFranchise franchise2 = AnimeFranchise.builder()
                .id(UUID.randomUUID().toString())
                .franchiseTitle("Franchise To Get 2")
                .hasBeenWatched(true)
                .build();

        List<AnimeFranchise> fixture = Arrays.asList(franchise1, franchise2);
        when(franchiseRepository.findAll()).thenReturn(fixture);

        List<AnimeFranchiseDTO> expected = Stream.of(franchise1, franchise2)
                .map(AnimeFranchiseMapper::mapToDTO)
                .collect(Collectors.toList());

        List<AnimeFranchiseDTO> result = underTest.getAll();
        assertThat(result).isEqualTo(expected);
    }

    @Test
    void itShouldAddAnimeFranchise() {
        AnimeFranchiseCommand animeFranchiseCommand =
                new AnimeFranchiseCommand("Can Be Added", false);
        AnimeFranchise expectedAnimeFranchise = AnimeFranchiseMapper.mapToEntity(animeFranchiseCommand);
        String expectedId = UUID.randomUUID().toString();
        expectedAnimeFranchise.setId(expectedId);

        when(franchiseRepository.save(any(AnimeFranchise.class)))
                .thenReturn(expectedAnimeFranchise);
        String resultId = underTest.addAnimeFranchise(animeFranchiseCommand);

        verify(franchiseRepository).save(any(AnimeFranchise.class));
        assertThat(resultId).isEqualTo(expectedId);
    }

    @Test
    void itShouldThrowWhenAttemptingToCreateAnAnimeFranchiseThatExists() {
        String existingFranchiseTitle = "Already Existing Franchise";
        AnimeFranchiseCommand animeFranchiseCommand =
                new AnimeFranchiseCommand(existingFranchiseTitle, false);
        String exceptionMessage = "anime franchise with title : " + existingFranchiseTitle + " already exists";

        AnimeFranchise animeFranchise = AnimeFranchiseMapper.mapToEntity(animeFranchiseCommand);

        when(franchiseRepository.findByFranchiseTitle(existingFranchiseTitle))
                .thenReturn(Optional.of(animeFranchise));

        assertThatThrownBy(() -> underTest.addAnimeFranchise(animeFranchiseCommand))
                .isInstanceOf(AnimeFranchiseBadRequestException.class)
                .hasMessageContaining(exceptionMessage);

        verify(franchiseRepository, never()).save(any());
    }

    @Test
    void itShouldUpdateAnimeFranchise() {
        String idFranchiseToUpdate = UUID.randomUUID().toString();
        boolean initialHasBeenWatched = false;
        AnimeFranchise initial = AnimeFranchise.builder()
                .id(idFranchiseToUpdate)
                .franchiseTitle("Franchise to Update")
                .hasBeenWatched(initialHasBeenWatched)
                .animes(new ArrayList<>())
                .build();

        String updatedTitle = "Franchise Updated";
        AnimeFranchiseDTO expected = AnimeFranchiseDTO.builder()
                .id(idFranchiseToUpdate)
                .franchiseTitle(updatedTitle)
                .hasBeenWatched(!initialHasBeenWatched)
                .build();

        when(franchiseRepository.findById(idFranchiseToUpdate)).thenReturn(Optional.of(initial));

        AnimeFranchiseDTO updateResult =
                underTest.updateFranchise(idFranchiseToUpdate, updatedTitle, !initialHasBeenWatched);

        assertThat(updateResult).isEqualTo(expected);
    }

    @Test
    void itShouldThrowWhenAttemptingToUpdateNonExistentFranchise() {
        String fakeFranchiseId = "Fake uuid";
        String fakeFranchiseTitle = "Fake Title";
        String exceptionMessage = "anime franchise with ID : " + fakeFranchiseId + " not found";
        assertThatThrownBy(() -> underTest.updateFranchise(fakeFranchiseId, fakeFranchiseTitle, false))
                .isInstanceOf(AnimeFranchiseNotFoundException.class)
                .hasMessageContaining(exceptionMessage);
    }

    @Test
    void itShouldDeleteAnimeFranchiseById() {
        String id = UUID.randomUUID().toString();
        when(franchiseRepository.existsById(id)).thenReturn(true);

        underTest.deleteAnimeFranchise(id);
        verify(franchiseRepository).deleteById(id);
    }

    @Test
    void itShouldThrowWhenAttemptingToDeleteNonExistentFranchise() {
        String id = UUID.randomUUID().toString();
        String exceptionMessage = "anime franchise with ID : " + id + " not found";
        when(franchiseRepository.existsById(id)).thenReturn(false);

        assertThatThrownBy(() -> underTest.deleteAnimeFranchise(id))
                .isInstanceOf(AnimeFranchiseNotFoundException.class)
                .hasMessageContaining(exceptionMessage);

        verify(franchiseRepository, never()).deleteById(any());
    }
}