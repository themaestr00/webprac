package ru.ispras.wtpractice.videorent.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ispras.wtpractice.videorent.dbtypes.IssuedExemplarKey;
import ru.ispras.wtpractice.videorent.entity.IssuedExemplar;

import java.util.List;

public interface IssuedExemplarDAO extends JpaRepository<IssuedExemplar, IssuedExemplarKey> {
    // Список взятых экземпляров на странице клиента
    List<IssuedExemplar> findByIdClientId(Long idClientId);

    // Количество экземпляров на руках у клиента
    long countByIdClientId(Long idClientId);
}
