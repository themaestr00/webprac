package ru.ispras.wtpractice.videorent.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.ispras.wtpractice.videorent.entity.IssuedExemplar;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class IssuedExemplarDAOTest extends BaseDAOTest {

    @Autowired
    private IssuedExemplarDAO issuedExemplarDAO;

    @Test
    void shouldFindIssuedExemplarsByClientId_Found() {
        List<IssuedExemplar> issued = issuedExemplarDAO.findByIdClientId(1L);
        assertFalse(issued.isEmpty());
        assertEquals(1, issued.size());
        assertEquals(1L, issued.getFirst().getId().getClientId());
        assertEquals(2L, issued.getFirst().getId().getExemplarId());
    }

    @Test
    void shouldFindIssuedExemplarsByClientId_NotFound() {
        List<IssuedExemplar> issued = issuedExemplarDAO.findByIdClientId(2L);
        assertTrue(issued.isEmpty());
    }

    @Test
    void shouldCountIssuedExemplars() {
        long count1 = issuedExemplarDAO.countByIdClientId(1L);
        assertEquals(1L, count1);
        long count2 = issuedExemplarDAO.countByIdClientId(2L);
        assertEquals(0L, count2);
        long count99 = issuedExemplarDAO.countByIdClientId(99L);
        assertEquals(0L, count99);
    }
}