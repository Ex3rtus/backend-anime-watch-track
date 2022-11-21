package self.project.animewatchtrack.animeseason;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;

import self.project.animewatchtrack.anime.Anime;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Youssef Kaïdi.
 * created 09 nov. 2022.
 */

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@Entity
@Table(name = "anime_seasons")
public class AnimeSeason {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "id")
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "anime_id")
    @ToString.Exclude
    private Anime anime;

    @Column(name = "season_number")
    private Integer seasonNumber;

    @Column(name = "total_episodes_count")
    private Integer totalEpisodesCount;

    @Column(name = "current_watch_count")
    private Integer currentWatchCount;

    @Column(name = "has_been_watched")
    private Boolean hasBeenWatched;

    @Transient
    @Setter(AccessLevel.NONE)
    @ToString.Exclude
    private Map<Boolean, SeasonMarkerStrategy> strategyMap;

    public AnimeSeason() {
        strategyMap = new HashMap<>();
        strategyMap.put(Boolean.TRUE, new WatchedSeasonMarkerStrategy());
        strategyMap.put(Boolean.FALSE, new NotWatchedSeasonMarkerStrategy());
    }

}


