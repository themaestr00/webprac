package ru.ispras.wtpractice.videorent.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.ispras.wtpractice.videorent.entity.Movie;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MovieDAOTest extends BaseDAOTest {

    @Autowired
    private MovieDAO movieDAO;

    @Test
    void shouldFindMovieByName_Found() {
        List<Movie> movies = movieDAO.findByNameContainingIgnoreCase("удар");
        assertEquals(1, movies.size());
        assertEquals("Максимальный удар", movies.getFirst().getName());
    }

    @Test
    void shouldFindMoviesByCompany_Found() {
        List<Movie> movies = movieDAO.findByCompanyContainingIgnoreCase("Hollywood Storm");
        assertEquals(2, movies.size());
    }

    @Test
    void shouldFindMoviesByReleaseDateBetween() {
        LocalDate start = LocalDate.of(2017, 1, 1);
        LocalDate end = LocalDate.of(2017, 12, 31);

        List<Movie> movies = movieDAO.findByReleaseDateBetween(start, end);
        assertEquals(1, movies.size());
        assertEquals("Максимальный удар", movies.getFirst().getName());
    }

    @Test
    void shouldNotFindMovieByDirector_NotFound() {
        List<Movie> movies = movieDAO.findByDirectorContainingIgnoreCase("Квентин Тарантино");
        assertTrue(movies.isEmpty());
    }
}