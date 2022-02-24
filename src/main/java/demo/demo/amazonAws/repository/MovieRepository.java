package demo.demo.amazonAws.repository;

import demo.demo.amazonAws.model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {
    Optional<Movie> findProductById(Long id);
    List<Movie> findAllByNameContaining(String name);
}
