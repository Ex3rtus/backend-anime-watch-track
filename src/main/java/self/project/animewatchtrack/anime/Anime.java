package self.project.animewatchtrack.anime;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import self.project.animewatchtrack.animefranchise.AnimeFranchise;
import self.project.animewatchtrack.animeseason.AnimeSeason;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Youssef Kaïdi.
 * created 02 nov. 2022.
 */

@Data
@AllArgsConstructor
@Builder(toBuilder = true)
@Entity
@Table(
        name = "animes",
        uniqueConstraints = @UniqueConstraint(name = "UniqueAnimeTitle", columnNames = "anime_title")
        )
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
    private List<AnimeSeason> seasons;

    @Column(name = "is_watched")
    private Boolean isWatched;

    @Transient
    @Setter(AccessLevel.NONE)
    @ToString.Exclude
    private Map<Boolean, AnimeMarkerStrategy> strategyMap;

    public Anime() {
        seasons = new ArrayList<>();
        originalMangaAuthors = new ArrayList<>();
        strategyMap = new HashMap<>();
        strategyMap.put(Boolean.TRUE, new WatchedAnimeMarkerStrategy());
        strategyMap.put(Boolean.FALSE, new NotWatchedAnimeMarkerStrategy());
    }

    public void addSeason(AnimeSeason season) {
        seasons.add(season);
        season.setAnime(this);
    }

    public void removeSeason(AnimeSeason season) {
        seasons.remove(season);
        season.setAnime(null);
    }

}
