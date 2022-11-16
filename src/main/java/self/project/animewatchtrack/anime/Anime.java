package self.project.animewatchtrack.anime;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import self.project.animewatchtrack.animefranchise.AnimeFranchise;
import self.project.animewatchtrack.animeseason.AnimeSeason;

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

    @OneToMany(
            mappedBy = "anime",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @ToString.Exclude
    private List<AnimeSeason> seasons = new ArrayList<>();

    @Column(name = "has_been_watched")
    private Boolean hasBeenWatched;

    public void addSeason(AnimeSeason season) {
        seasons.add(season);
        season.setAnime(this);
    }

    public void removeSeason(AnimeSeason season) {
        seasons.remove(season);
        season.setSeasonNumber(null);
    }
}
