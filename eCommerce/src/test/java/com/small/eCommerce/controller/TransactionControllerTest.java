package com.small.eCommerce.controller;

import com.small.eCommerce.model.Transaction;
import com.small.eCommerce.model.TransactionCountWrapper;
import com.small.eCommerce.model.TransactionWrapper;
import com.small.eCommerce.model.History;
import com.small.eCommerce.responseHandler.ApiResponse;
import com.small.eCommerce.service.TransactionServices;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

public class TransactionControllerTest {

    @Mock
    private TransactionServices transactionServices;

    @InjectMocks
    private TransactionController transactionController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Test for FindTransactionById with a valid ID
    @Test
    public void testFindTransactionById_Success() {
        Transaction transaction = new Transaction(1, true, 100.0, 2, new java.util.Date());
        History history1 = new History(1, 1, 2, 50.0);
        History history2 = new History(1, 2, 1, 50.0);
        List<History> histories = Arrays.asList(history1, history2);
        TransactionWrapper transactionWrapper = new TransactionWrapper(transaction, histories);

        when(transactionServices.FindTransactionById(anyInt())).thenReturn(transactionWrapper);

        ResponseEntity<ApiResponse<TransactionWrapper>> response = transactionController.FindTransactionById(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(Objects.requireNonNull(response.getBody()).isSuccess());
        assertEquals("Transaction id: 1 found.", response.getBody().getMessage());
        assertEquals(transactionWrapper, response.getBody().getPayload());
    }

    // Test for FindTransactionById with an invalid ID
    @Test
    public void testFindTransactionById_NotFound() {
        when(transactionServices.FindTransactionById(anyInt())).thenReturn(null);

        ResponseEntity<ApiResponse<TransactionWrapper>> response = transactionController.FindTransactionById(999);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertFalse(Objects.requireNonNull(response.getBody()).isSuccess());
        assertEquals("Transaction id: 999 not found.", response.getBody().getMessage());
        assertEquals(new TransactionWrapper(), response.getBody().getPayload());
    }

    // Test for GetAllTransaction with transactions present
    @Test
    public void testGetAllTransaction_Success() {
        List<TransactionWrapper> transactionWrappers = getTransactionWrappers();

        when(transactionServices.GetAllTransaction()).thenReturn(transactionWrappers);

        ResponseEntity<ApiResponse<List<TransactionWrapper>>> response = transactionController.GetAllTransaction();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(Objects.requireNonNull(response.getBody()).isSuccess());
        assertEquals("Transaction Found.", response.getBody().getMessage());
        assertEquals(transactionWrappers, response.getBody().getPayload());
    }

    private static List<TransactionWrapper> getTransactionWrappers() {
        Transaction transaction1 = new Transaction(1, true, 100.0, 2, new java.util.Date());
        Transaction transaction2 = new Transaction(2, false, 200.0, 3, new java.util.Date());
        History history1 = new History(1, 1, 2, 50.0);
        History history2 = new History(2, 1, 3, 200.0);

        List<History> histories1 = List.of(history1);
        List<History> histories2 = List.of(history2);

        TransactionWrapper transactionWrapper1 = new TransactionWrapper(transaction1, histories1);
        TransactionWrapper transactionWrapper2 = new TransactionWrapper(transaction2, histories2);

        return Arrays.asList(transactionWrapper1, transactionWrapper2);
    }

    // Test for GetAllTransaction with no transactions
    @Test
    public void testGetAllTransaction_Empty() {
        when(transactionServices.GetAllTransaction()).thenReturn(new ArrayList<>());

        ResponseEntity<ApiResponse<List<TransactionWrapper>>> response = transactionController.GetAllTransaction();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertFalse(Objects.requireNonNull(response.getBody()).isSuccess());
        assertEquals("Transaction couldn't be found.", response.getBody().getMessage());
        assertEquals(new ArrayList<>(), response.getBody().getPayload());
    }

    // Test for GetCount with successful transaction count retrieval
    @Test
    public void testGetCount_Success() {
        TransactionCountWrapper countWrapper = new TransactionCountWrapper();
        countWrapper.setTotalTransactionOfOrders(10);
        countWrapper.setTotalTransactionOfPurchases(5);
        countWrapper.setTotalTransactions(15L);

        when(transactionServices.TransactionCount()).thenReturn(countWrapper);

        ResponseEntity<ApiResponse<TransactionCountWrapper>> response = transactionController.GetCount();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(Objects.requireNonNull(response.getBody()).isSuccess());
        assertEquals("Transactions count.", response.getBody().getMessage());
        assertEquals(countWrapper, response.getBody().getPayload());
    }

    // Test for DeleteTransaction
    @Test
    public void testDeleteTransaction() {
        ResponseEntity<ApiResponse<String>> response = transactionController.DeleteTransaction();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(Objects.requireNonNull(response.getBody()).isSuccess());
        assertEquals("Transactions Found.", response.getBody().getMessage());
        assertEquals("Transactions Deleted.", response.getBody().getPayload());
    }
}
