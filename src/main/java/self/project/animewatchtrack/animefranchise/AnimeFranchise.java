package self.project.animewatchtrack.animefranchise;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import self.project.animewatchtrack.anime.Anime;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Youssef Kaïdi.
 * created 26 oct. 2022.
 */

@Data
@AllArgsConstructor
@Builder(toBuilder = true)
@Entity
@Table(
        name = "anime_franchises",
        uniqueConstraints = @UniqueConstraint(name = "UniqueFranchiseTitle", columnNames = "franchise_title")
        )
public class AnimeFranchise {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "id")
    private String id;

    @Column(name = "franchise_title")
    private String franchiseTitle;

    @Column(name = "is_watched")
    private Boolean isWatched;

    @OneToMany(
            mappedBy = "animeFranchise",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @ToString.Exclude
    private List<Anime> animes;

    @Transient
    @Setter(AccessLevel.NONE)
    @ToString.Exclude
    private Map<Boolean, FranchiseMarkerStrategy> strategyMap;

    public AnimeFranchise() {
        animes = new ArrayList<>();
        strategyMap = new HashMap<>();
        strategyMap.put(Boolean.TRUE, new WatchedFranchiseMarkerStrategy());
        strategyMap.put(Boolean.FALSE, new NotWatchedFranchiseMarkerStrategy());
    }

    public void addAnime(Anime anime) {
        animes.add(anime);
        anime.setAnimeFranchise(this);
    }

    public void removeAnime(Anime anime) {
        animes.remove(anime);
        anime.setAnimeFranchise(null);
    }

}




