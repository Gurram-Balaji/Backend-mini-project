package com.small.eCommerce.service;

import com.small.eCommerce.exception.FoundException;
import com.small.eCommerce.model.History;
import com.small.eCommerce.model.Transaction;
import com.small.eCommerce.model.TransactionCountWrapper;
import com.small.eCommerce.model.TransactionWrapper;
import com.small.eCommerce.dao.HistoryDao;
import com.small.eCommerce.dao.TransactionDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
class TransactionServicesTest {

    private TransactionDao transactionDao;
    private HistoryDao HistoryDao;
    private TransactionServices transactionServices;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        transactionDao = mock(TransactionDao.class);
        HistoryDao = mock(HistoryDao.class);
        transactionServices = new TransactionServices();
        transactionServices.TransactionDao = transactionDao;
        transactionServices.HistoryDao = HistoryDao;
    }

    @Test
    void testFindTransactionById_TransactionNotFound() {
        when(transactionDao.findTransactionById(anyInt())).thenReturn(Optional.empty());

        FoundException thrown = assertThrows(FoundException.class, () -> transactionServices.FindTransactionById(1));

        assertEquals("Transaction not found for ID: 1", thrown.getMessage());
    }

    @Test
    void testFindTransactionById_Success() {
        Transaction transaction = new Transaction();
        transaction.setTransactionId(1);
        List<History> historyList = new ArrayList<>();
        historyList.add(new History());

        when(transactionDao.findTransactionById(anyInt())).thenReturn(Optional.of(transaction));
        when(HistoryDao.findAllTransactionByTransactionId(anyInt())).thenReturn(historyList);

        TransactionWrapper result = transactionServices.FindTransactionById(1);

        assertNotNull(result);
        assertEquals(transaction, result.getTransaction());
    }

    @Test
    void testGetAllTransaction_EmptyList() {
        when(transactionDao.findAllTransactions()).thenReturn(new ArrayList<>());

        List<TransactionWrapper> result = transactionServices.GetAllTransaction();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetAllTransaction_Success() {
        Transaction transaction = new Transaction();
        transaction.setTransactionId(1);
        List<History> historyList = new ArrayList<>();
        historyList.add(new History());
        TransactionWrapper transactionWrapper = new TransactionWrapper(transaction, historyList);

        when(transactionDao.findAllTransactions()).thenReturn(List.of(transaction));
        when(transactionDao.findTransactionById(anyInt())).thenReturn(Optional.of(transaction));
        when(HistoryDao.findAllTransactionByTransactionId(anyInt())).thenReturn(historyList);

        List<TransactionWrapper> result = transactionServices.GetAllTransaction();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(transactionWrapper, result.getFirst());
    }

    @Test
    void testTransactionCount_Success() {
        when(transactionDao.getAllOrdersCount()).thenReturn(10);
        when(transactionDao.getAllPurchasesCount()).thenReturn(5);
        when(transactionDao.getAllTransactionCount()).thenReturn(15L);

        TransactionCountWrapper result = transactionServices.TransactionCount();

        assertNotNull(result);
        assertEquals(10, result.getTotalTransactionOfOrders());
        assertEquals(5, result.getTotalTransactionOfPurchases());
        assertEquals(15, result.getTotalTransactions());
    }

    @Test
    void testDeleteTransaction_Success() {
        transactionServices.DeleteTransaction();
        verify(transactionDao, times(1)).deleteAllTransaction();
        verify(HistoryDao, times(1)).deleteAllTransaction();
    }

}
