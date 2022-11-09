package self.project.animewatchtrack.anime;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import self.project.animewatchtrack.animefranchise.AnimeFranchise;

import javax.persistence.*;
import java.util.List;

/**
 * @author Youssef Kaïdi.
 * created 02 nov. 2022.
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "animes")
public class Anime {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "id")
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "anime_franchise_id")
    @ToString.Exclude
    private AnimeFranchise animeFranchise;

    @Column(name = "anime_title")
    private String animeTitle;

    @Column(name = "air_year")
    private Integer initialAirYear;

    @ElementCollection(
            fetch = FetchType.EAGER,
            targetClass = String.class
    )
    @CollectionTable(
            name = "manga_authors",
            joinColumns = @JoinColumn(name = "id", referencedColumnName = "id")
    )
    private List<String> originalMangaAuthors;

    @Column(name = "has_been_watched")
    private Boolean hasBeenWatched;

}
