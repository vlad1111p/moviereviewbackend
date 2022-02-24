package demo.demo.amazonAws.config.form;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import demo.demo.amazonAws.model.Message;
import demo.demo.amazonAws.model.Movie;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.ArrayList;
import java.util.List;
@Data
@Builder
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class MovieDTO {
    private Long id;
    private String name;
    private float score;
    private String genre;
    private String movieProfileImageLink;
    private String description;
    private List<String> messages=new ArrayList<>();

    public MovieDTO(Long id, String name, float score, String genre,
                    String movieProfileImageLink, String description, List<String> messages) {
        this.id = id;
        this.name = name;
        this.score = score;
        this.genre = genre;
        this.movieProfileImageLink = movieProfileImageLink;
        this.description = description;
        this.messages = messages;
    }


    public MovieDTO(Movie movie) {
        this.id = movie.getId();
        this.name = movie.getName();
        this.score = movie.getScore();
        this.genre = movie.getGenre();
        this.movieProfileImageLink = movie.getMovieProfileImageLink();
        this.description = movie.getDescription();
        for(Message message: movie.getMessages())
        this.messages.add(message.getContent());
    }




}
