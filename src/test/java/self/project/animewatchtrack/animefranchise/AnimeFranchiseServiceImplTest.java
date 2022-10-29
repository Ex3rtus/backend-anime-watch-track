package self.project.animewatchtrack.animefranchise;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

/**
 * @author Youssef Kaïdi.
 * created 28 oct. 2022.
 */

@ExtendWith(MockitoExtension.class)
class AnimeFranchiseServiceImplTest {
    @Mock
    private AnimeFranchiseRepository franchiseRepository;
    private AnimeFranchiseServiceImpl underTest;

    @BeforeEach
    void setUp() {
        underTest = new AnimeFranchiseServiceImpl(franchiseRepository);
    }

    @Test
    void itShouldAddAnimeFranchise() {
        //given
        AnimeFranchiseCommand animeFranchiseCommand =
                new AnimeFranchiseCommand("Can Be Added", false);
        AnimeFranchise animeFranchise = AnimeFranchiseMapper.mapToEntity(animeFranchiseCommand);
        //when
        underTest.addAnimeFranchise(animeFranchiseCommand);

        //then
        ArgumentCaptor<AnimeFranchise> animeFranchiseArgumentCaptor =
                ArgumentCaptor.forClass(AnimeFranchise.class);

        verify(franchiseRepository)
                .save(animeFranchiseArgumentCaptor.capture());

        AnimeFranchise capturedFranchise = animeFranchiseArgumentCaptor.getValue();
        assertThat(capturedFranchise).isEqualTo(animeFranchise);
    }

    @Test
    void itShouldThrowWhenAnimeFranchiseExists() {
        //given
        String existingFranchiseTitle = "Can Be Added";
        AnimeFranchiseCommand animeFranchiseCommand =
                new AnimeFranchiseCommand(existingFranchiseTitle, false);

        AnimeFranchise animeFranchise = AnimeFranchiseMapper.mapToEntity(animeFranchiseCommand);

        given(franchiseRepository.findByFranchiseTitle(existingFranchiseTitle))
                .willReturn(Optional.of(animeFranchise));

        assertThatThrownBy(() -> underTest.addAnimeFranchise(animeFranchiseCommand))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("anime franchise with title " + existingFranchiseTitle + " already exists");

        verify(franchiseRepository, never()).save(any());
    }
}