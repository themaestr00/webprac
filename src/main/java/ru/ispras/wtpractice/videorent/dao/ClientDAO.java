package ru.ispras.wtpractice.videorent.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ispras.wtpractice.videorent.entity.Client;

import java.util.List;

public interface ClientDAO extends JpaRepository<Client, Long> {
    // Фильтрация клиентов по имени и фамилии или их частях на странице клиентов
    List<Client> findByFullNameContainingIgnoreCase(String fullName);

    // Фильтрация клиентов по номеру телефона или его части на странице клиентов
    List<Client> findByPhoneNumberContaining(String phoneNumber);

    // Фильтрация клиентов по адресу или его части на странице клиентов
    List<Client> findByAddressContainingIgnoreCase(String address);
}
