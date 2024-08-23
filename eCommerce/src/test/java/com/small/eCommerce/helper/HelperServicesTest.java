package com.small.eCommerce.helper;

import com.small.eCommerce.dao.HistoryDao;
import com.small.eCommerce.dao.ProductDao;
import com.small.eCommerce.dao.TransactionDao;
import com.small.eCommerce.exception.FoundException;
import com.small.eCommerce.model.History;
import com.small.eCommerce.model.Orders;
import com.small.eCommerce.model.Products;
import com.small.eCommerce.model.Transaction;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class HelperServicesTest {

    @Mock
    private ProductDao productDao;

    @Mock
    private HistoryDao historyDao;

    @Mock
    private TransactionDao transactionDao;

    @InjectMocks
    private HelperServices helperServices;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        HelperServices.setCurrentTransactionId(null);
    }

    @AfterEach
    public void tearDown() {
        // Reset the static variable after each test to ensure no state is carried over
        HelperServices.setCurrentTransactionId(null);
    }

    @Test
    public void testGenerateTransactionId_InitialTransaction_ShouldStartFromOne() {
        when(transactionDao.getNextTransactionId()).thenReturn(null);

        Integer transactionId = HelperServices.generateTransactionId(transactionDao);

        assertEquals(1, transactionId);
    }

    @Test
    public void testGenerateTransactionId_SubsequentTransactions_ShouldIncrement() {
        when(transactionDao.getNextTransactionId()).thenReturn(100);

        Integer transactionId1 = HelperServices.generateTransactionId(transactionDao);
        Integer transactionId2 = HelperServices.generateTransactionId(transactionDao);

        assertEquals(101, transactionId1);
        assertEquals(102, transactionId2);
    }

    @Test
    public void testStoreOrder_NullOrderList_ShouldThrowException() {
        FoundException exception = assertThrows(FoundException.class, () -> helperServices.StoreOrder(null, true));
        assertEquals("Order list cannot be null or empty.", exception.getMessage());

        exception = assertThrows(FoundException.class, () -> helperServices.StoreOrder(List.of(), true));
        assertEquals("Order list cannot be null or empty.", exception.getMessage());
    }

    @Test
    public void testStoreOrder_ProductNotFound_ShouldThrowException() {
        Orders order = new Orders(1, 10);
        when(productDao.FindProductById(1)).thenReturn(Optional.empty());

        FoundException exception = assertThrows(FoundException.class, () -> helperServices.StoreOrder(List.of(order), true));
        assertEquals("Product not found for ID: 1", exception.getMessage());
    }

    @Test
    public void testStoreOrder_SuccessfulOrderProcessing() {
        Orders order1 = new Orders(1, 2);
        Orders order2 = new Orders(2, 3);
        Products product1 = new Products(1, "Product1", 10, 100.0);
        Products product2 = new Products(2, "Product2", 15, 200.0);

        when(productDao.FindProductById(1)).thenReturn(Optional.of(product1));
        when(productDao.FindProductById(2)).thenReturn(Optional.of(product2));
        when(transactionDao.getNextTransactionId()).thenReturn(100);

        helperServices.StoreOrder(Arrays.asList(order1, order2), true);

        verify(productDao, times(2)).SaveProduct(any(Products.class));
        verify(historyDao, times(2)).saveHistory(any(History.class));
        verify(transactionDao, times(1)).saveTransaction(any(Transaction.class));
    }

    @Test
    public void testStoreOrder_ValidOrders_ShouldUpdateProductAndHistory() {
        Orders order = new Orders(1, 5);
        Products product = new Products(1, "Test Product", 10, 100.0);
        when(productDao.FindProductById(order.getId())).thenReturn(Optional.of(product));
        when(transactionDao.getNextTransactionId()).thenReturn(null);

        helperServices.StoreOrder(Collections.singletonList(order), true);

        // Verify product quantity update and save
        verify(productDao, times(1)).SaveProduct(any(Products.class));
        assertEquals(5, product.getProductsQTY());

        // Verify history creation and save
        verify(historyDao, times(1)).saveHistory(any(History.class));

        // Verify transaction creation and save
        verify(transactionDao, times(1)).saveTransaction(any(Transaction.class));
    }

}
