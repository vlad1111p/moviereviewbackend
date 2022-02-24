package demo.demo.amazonAws.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;

@Data
@NoArgsConstructor
@Entity(name = "Movie")
@AllArgsConstructor
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    @Min(value=1)
    @Max(value=10)
    private float score;
    private String genre;
    private String movieProfileImageLink;
    @Column(columnDefinition="TEXT")
    private String description;
    @JsonIgnore
    @OneToMany(
            cascade= {CascadeType.PERSIST,
                    CascadeType.MERGE,
                    CascadeType.REFRESH,
                    CascadeType.DETACH},
            fetch = FetchType.EAGER)
    @Fetch(FetchMode.SUBSELECT)
    private List<Message> messages;



    public Movie( String name, String description) {

        this.name = name;
        this.description = description;
    }
}
