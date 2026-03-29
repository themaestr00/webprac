package ru.ispras.wtpractice.videorent.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.ispras.wtpractice.videorent.entity.Exemplar;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ExemplarDAOTest extends BaseDAOTest {

    @Autowired
    private ExemplarDAO exemplarDAO;

    @Test
    void shouldFindExemplarsByMovieId() {
        List<Exemplar> exemplars = exemplarDAO.findByMovieId(1L);
        assertEquals(2, exemplars.size());
    }

    @Test
    void shouldFindAvailableExemplarsOnly() {
        List<Exemplar> available = exemplarDAO.findByAvailableIsTrue();
        assertEquals(3, available.size());
    }

    @Test
    void shouldCountAvailableExemplarsForMovie() {
        long count = exemplarDAO.countByMovieIdAndAvailableIsTrue(1L);
        assertEquals(1L, count);
    }

    @Test
    void shouldFindExemplarsByMediaType() {
        List<Exemplar> vhsExemplars = exemplarDAO.findByMediaTypeNameContainingIgnoreCase("VHS");
        assertEquals(2, vhsExemplars.size());
    }

    @Test
    void shouldFindExemplarsCountByMovieId() {
        long count = exemplarDAO.countByMovieId(1L);
        long count_zero = exemplarDAO.countByMovieId(3L);
        assertEquals(2L, count);
        assertEquals(0L, count_zero);
    }
}