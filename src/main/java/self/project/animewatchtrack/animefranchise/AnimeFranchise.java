package self.project.animewatchtrack.animefranchise;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.UUID;

/**
 * @author Youssef Kaïdi.
 * created 26 oct. 2022.
 */

@Data
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "anime_franchises")
public class AnimeFranchise {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "id")
    private String id;

    @Column(name = "franchise_title")
    private String franchiseTitle;

    @Column(name = "has_been_watched")
    private boolean hasBeenWatched;

}




