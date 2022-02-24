package demo.demo.amazonAws.controllers;

import demo.demo.amazonAws.config.form.MovieDTO;
import demo.demo.amazonAws.config.form.SimpleJsonFile;
import demo.demo.amazonAws.config.form.SimpleJsonFile1;
import demo.demo.amazonAws.model.Message;
import demo.demo.amazonAws.model.Movie;
import demo.demo.amazonAws.repository.MessageRepository;
import demo.demo.amazonAws.repository.MovieRepository;
import demo.demo.amazonAws.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class MovieController {

    private final MovieRepository movieRepository;
    private final UserRepository userRepository;
    private final MessageRepository messageRepository;

    @Transactional
    @Async
    @PostMapping(value = "/findMovies")
    public List<Movie> findMoviesByName(@Valid @RequestBody SimpleJsonFile simpleJsonFile) {


        return movieRepository.findAllByNameContaining(simpleJsonFile.getContent());
    }

    @Transactional
    @Async
    @PostMapping(value = "/findmoviebyid")
    public Movie findMoviesById(@Valid @RequestBody SimpleJsonFile simpleJsonFile) {


        return movieRepository.findById(Long.valueOf(simpleJsonFile.getContent())).get();
    }

    @Transactional
    @Async
    @PostMapping(value = "/findmoviemessages")
    public List<Message> getMessages(@Valid @RequestBody SimpleJsonFile simpleJsonFile) {


        return messageRepository.findAllByMovie(
                movieRepository.findById(Long.valueOf(simpleJsonFile.getContent())).get());
    }

    @Transactional
    @Async
    @PostMapping(value = "/setmoviedescription")
    public void setDescription(@Valid @RequestBody SimpleJsonFile simpleJsonFile) {
        Movie movie = movieRepository.findById(Long.valueOf(simpleJsonFile.getEmail())).get();
        movie.setDescription(simpleJsonFile.getContent());
        movieRepository.save(movie);

    }

    @Transactional
    @Async
    @RequestMapping(value = "/movies", method = RequestMethod.GET)
    public List<MovieDTO> getAllMovies() {

        List<MovieDTO> movieDTOList = new ArrayList<MovieDTO>();
        movieRepository.findAll().forEach(e -> movieDTOList.add(new MovieDTO(e)));
        return movieDTOList;
    }

    @Transactional
    @Async
    @PostMapping(value = "/createmessage")
    public void createMessages(@Valid @RequestBody SimpleJsonFile1 simpleJsonFile) {
        System.out.println(simpleJsonFile.getContent());
        Message message = new Message(simpleJsonFile.getContent(),
                movieRepository.getById(Long.valueOf(simpleJsonFile.getContent1())),
                userRepository.getById(Long.valueOf(simpleJsonFile.getContent2())));
        messageRepository.save(message);

    }

}
