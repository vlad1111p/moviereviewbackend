package demo.demo.amazonAws.repository;

import demo.demo.amazonAws.model.Message;
import demo.demo.amazonAws.model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

List<Message> findAllByMovie(Movie movie);
}
