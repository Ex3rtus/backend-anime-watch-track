package self.project.animewatchtrack.animefranchise;

import lombok.*;

import javax.persistence.*;

/**
 * @author Youssef Kaïdi.
 * created 26 oct. 2022.
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "anime_franchise")
public class AnimeFranchise {

    @Id
    @SequenceGenerator(
            name = "anime_franchise_sequence",
            sequenceName = "anime_franchise_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "anime_franchise_sequence"
    )
    @Column(name = "id")
    private Long id;
    @Column(name = "franchise_title")
    private String franchiseTitle;

    @Column(name = "has_been_watched")
    private boolean hasBeenWatched;

}




