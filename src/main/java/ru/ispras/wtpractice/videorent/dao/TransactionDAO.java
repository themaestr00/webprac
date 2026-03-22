package ru.ispras.wtpractice.videorent.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ispras.wtpractice.videorent.dbtypes.TransactionType;
import ru.ispras.wtpractice.videorent.entity.Transaction;

import java.util.List;

public interface TransactionDAO extends JpaRepository<Transaction, Long> {
    // История выдач/возвратов клиента в его карточке
    List<Transaction> findByClientIdOrderByTimeDesc(Long clientId);

    // История выдач/возвратов экземпляров фильма в его карточке
    List<Transaction> findByExemplarMovieIdOrderByTimeDesc(Long movieId);

    // Транзакции по определенному экземпляру фильма в его карточке
    List<Transaction> findByExemplarMovieIdAndExemplarIdOrderByTimeDesc(Long exemplarMovieId, Long exemplarId);

    // Фильтрация транзакций (выдача/возврат)
    List<Transaction> findByTypeOrderByTimeDesc(TransactionType type);
}
