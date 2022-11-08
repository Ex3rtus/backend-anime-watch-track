package self.project.animewatchtrack.animefranchise;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import self.project.animewatchtrack.anime.Anime;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Youssef Kaïdi.
 * created 26 oct. 2022.
 */

@Data
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
    private Boolean hasBeenWatched;

    @OneToMany(
            mappedBy = "animeFranchise",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @ToString.Exclude
    private List<Anime> animes = new ArrayList<>();

    public void addAnime(Anime anime) {
        animes.add(anime);
        anime.setAnimeFranchise(this);
    }

    public void removeAnime(Anime anime) {
        animes.remove(anime);
        anime.setAnimeFranchise(null);
    }
}




