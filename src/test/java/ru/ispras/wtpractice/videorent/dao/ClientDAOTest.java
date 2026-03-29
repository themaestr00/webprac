package ru.ispras.wtpractice.videorent.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.ispras.wtpractice.videorent.entity.Client;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ClientDAOTest extends BaseDAOTest {

    @Autowired
    private ClientDAO clientDAO;

    @Test
    void shouldFindClientByFullName_Found() {
        List<Client> clients = clientDAO.findByFullNameContainingIgnoreCase("невский");
        assertEquals(1, clients.size());
        assertEquals("Александр Невский", clients.getFirst().getFullName());
    }

    @Test
    void shouldFindClientByFullName_NotFound() {
        List<Client> clients = clientDAO.findByFullNameContainingIgnoreCase("Киану Ривз");
        assertTrue(clients.isEmpty());
    }

    @Test
    void shouldFindClientByPhoneNumber_Found() {
        List<Client> clients = clientDAO.findByPhoneNumberContaining("0000002");
        assertEquals(1, clients.size());
        assertEquals("Сол Гудман", clients.getFirst().getFullName());
    }

    @Test
    void shouldFindClientByAddress_Found() {
        List<Client> clients = clientDAO.findByAddressContainingIgnoreCase("голливуд");
        assertEquals(1, clients.size());
    }

    @Test
    void shouldNotFindDeletedClient() {
        List<Client> clients = clientDAO.findByFullNameContainingIgnoreCase("Норрис");
        assertTrue(clients.isEmpty());
    }
}