package ru.ispras.wtpractice.videorent.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.ispras.wtpractice.videorent.dbtypes.TransactionType;
import ru.ispras.wtpractice.videorent.entity.Transaction;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TransactionDAOTest extends BaseDAOTest {

    @Autowired
    private TransactionDAO transactionDAO;

    @Test
    void shouldFindClientHistoryOrderedByTimeDesc() {
        List<Transaction> transactions = transactionDAO.findByClientIdOrderByTimeDesc(1L);
        assertEquals(3, transactions.size());
        assertEquals(3L, transactions.getFirst().getId());
        assertEquals(TransactionType.issue, transactions.getFirst().getType());
    }

    @Test
    void shouldFindTransactionsByExemplarId() {
        List<Transaction> transactions = transactionDAO.findByExemplarMovieIdAndExemplarIdOrderByTimeDesc(1L, 1L);
        assertEquals(2, transactions.size());
        assertEquals(TransactionType.returning, transactions.getFirst().getType());
    }

    @Test
    void shouldFilterByType() {
        List<Transaction> issues = transactionDAO.findByTypeOrderByTimeDesc(TransactionType.issue);
        assertEquals(3, issues.size());
        List<Transaction> returns = transactionDAO.findByTypeOrderByTimeDesc(TransactionType.returning);
        assertEquals(2, returns.size());
    }

    @Test
    void shouldFindTransactionsByMovieId() {
        List<Transaction> transactions = transactionDAO.findByExemplarMovieIdOrderByTimeDesc(1L);
        assertEquals(3, transactions.size());
    }
}