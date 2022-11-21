package self.project.animewatchtrack.animefranchise;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import self.project.animewatchtrack.exceptions.AnimeFranchiseBadRequestException;
import self.project.animewatchtrack.exceptions.AnimeFranchiseNotFoundException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
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

    private AnimeFranchise franchise1;
    private AnimeFranchise franchise2;

    @BeforeEach
    void setUp() {
        underTest = new AnimeFranchiseServiceImpl(franchiseRepository);

        franchise1 = new AnimeFranchise().toBuilder()
                .id(UUID.randomUUID().toString())
                .franchiseTitle("Franchise To Find")
                .hasBeenWatched(false)
                .build();

        franchise2 = new AnimeFranchise().toBuilder()
                .id(UUID.randomUUID().toString())
                .franchiseTitle("Franchise To Get 2")
                .hasBeenWatched(true)
                .build();
    }

    @Test
    void itShouldGetFranchiseById() {
        String id = franchise1.getId();
        AnimeFranchiseDTO expected = AnimeFranchiseMapper.mapToDTO(franchise1);

        when(franchiseRepository.findById(franchise1.getId()))
                .thenReturn(Optional.of(franchise1));

        AnimeFranchiseDTO resultDTO = underTest.getById(id);

        assertThat(resultDTO).isEqualTo(expected);
        verify(franchiseRepository).findById(id);
    }

    @Test
    void itShouldThrowWhenAttemptingToFindNonexistentFranchiseById() {
        String nonPersistedFranchiseId = UUID.randomUUID().toString();
        String exceptionMessage = "anime franchise with ID : " + nonPersistedFranchiseId + " not found";
        AnimeFranchiseNotFoundException exceptionToBeThrown =
                new AnimeFranchiseNotFoundException(nonPersistedFranchiseId);

        when(franchiseRepository.findById(nonPersistedFranchiseId))
                .thenThrow(exceptionToBeThrown);

        assertThatThrownBy(() -> underTest.getById(nonPersistedFranchiseId))
                .isInstanceOf(AnimeFranchiseNotFoundException.class)
                .hasMessageContaining(exceptionMessage);
    }

    @Test
    void itShouldReturnAnEmptyAnimeFranchiseList() {
        List<AnimeFranchiseDTO> result = underTest.getAll();
        assertThat(result.isEmpty()).isTrue();
        verify(franchiseRepository).findAll();
    }

    @Test
    void itShouldReturnAPopulatedAnimeFranchiseList() {
        List<AnimeFranchise> fixture = Arrays.asList(franchise1, franchise2);
        when(franchiseRepository.findAll()).thenReturn(fixture);

        List<AnimeFranchiseDTO> expected = Stream.of(franchise1, franchise2)
                .map(AnimeFranchiseMapper::mapToDTO)
                .collect(Collectors.toList());

        List<AnimeFranchiseDTO> result = underTest.getAll();
        assertThat(result).isEqualTo(expected);
        verify(franchiseRepository).findAll();
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

        assertThat(resultId).isEqualTo(expectedId);
        verify(franchiseRepository).findByFranchiseTitle(animeFranchiseCommand.getFranchiseTitle());
        verify(franchiseRepository).save(any(AnimeFranchise.class));
    }

    @Test
    void itShouldThrowWhenAttemptingToCreateAnAnimeFranchiseThatExists() {
        String existingFranchiseTitle = franchise1.getFranchiseTitle();
        AnimeFranchiseCommand animeFranchiseCommand =
                new AnimeFranchiseCommand(existingFranchiseTitle, false);
        String exceptionMessage = "anime franchise with title : " + existingFranchiseTitle + " already exists";

        AnimeFranchise animeFranchise = AnimeFranchiseMapper.mapToEntity(animeFranchiseCommand);

        when(franchiseRepository.findByFranchiseTitle(existingFranchiseTitle))
                .thenReturn(Optional.of(animeFranchise));

        assertThatThrownBy(() -> underTest.addAnimeFranchise(animeFranchiseCommand))
                .isInstanceOf(AnimeFranchiseBadRequestException.class)
                .hasMessageContaining(exceptionMessage);

        verify(franchiseRepository).findByFranchiseTitle(existingFranchiseTitle);
        verify(franchiseRepository, never()).save(any());
    }

    @Test
    void itShouldUpdateAnimeFranchise() {
        String idFranchiseToUpdate = franchise1.getId();
        boolean initialHasBeenWatched = franchise2.getHasBeenWatched();

        String updatedTitle = "Franchise Updated";
        AnimeFranchiseDTO expected = AnimeFranchiseDTO.builder()
                .id(idFranchiseToUpdate)
                .franchiseTitle(updatedTitle)
                .hasBeenWatched(!initialHasBeenWatched)
                .build();

        when(franchiseRepository.findById(idFranchiseToUpdate)).thenReturn(Optional.of(franchise1));

        AnimeFranchiseDTO updateResult =
                underTest.updateFranchise(idFranchiseToUpdate, updatedTitle, !initialHasBeenWatched);

        assertThat(updateResult).isEqualTo(expected);
    }

    @Test
    void itShouldThrowWhenAttemptingToUpdateNonExistentFranchise() {
        String nonexistentFranchiseId = UUID.randomUUID().toString();
        String fakeFranchiseTitle = "Nonexistent Franchise Title";
        String exceptionMessage = "anime franchise with ID : " + nonexistentFranchiseId + " not found";
        assertThatThrownBy(() ->
                underTest.updateFranchise(nonexistentFranchiseId,
                        fakeFranchiseTitle,
                        false
        )).isInstanceOf(AnimeFranchiseNotFoundException.class)
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
    void itShouldThrowWhenAttemptingToDeleteNonexistentFranchise() {
        String id = UUID.randomUUID().toString();
        String exceptionMessage = "anime franchise with ID : " + id + " not found";
        when(franchiseRepository.existsById(id)).thenReturn(false);

        assertThatThrownBy(() -> underTest.deleteAnimeFranchise(id))
                .isInstanceOf(AnimeFranchiseNotFoundException.class)
                .hasMessageContaining(exceptionMessage);

        verify(franchiseRepository, never()).deleteById(any());
    }
}