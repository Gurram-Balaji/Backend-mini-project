package com.small.eCommerce.helper;

import com.small.eCommerce.exception.FoundException;
import com.small.eCommerce.model.History;
import com.small.eCommerce.model.Orders;
import com.small.eCommerce.model.Products;
import com.small.eCommerce.model.Transaction;
import com.small.eCommerce.repository.HistoryRepo;
import com.small.eCommerce.repository.ProductsRepo;
import com.small.eCommerce.repository.TransactionRepo;
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
    private ProductsRepo productsRepo;

    @Mock
    private HistoryRepo historyRepo;

    @Mock
    private TransactionRepo transactionRepo;

    @InjectMocks
    private HelperServices helperServices;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        // Reset the static variable before each test
        HelperServices.setCurrentTransactionId(null);
    }

    @AfterEach
    public void tearDown() {
        // Reset the static variable after each test to ensure no state is carried over
        HelperServices.setCurrentTransactionId(null);
    }

    @Test
    public void testGenerateTransactionId_InitialTransaction_ShouldStartFromOne() {
        when(transactionRepo.getLatestTransactionId()).thenReturn(null);

        Integer transactionId = HelperServices.generateTransactionId(transactionRepo);

        assertEquals(1, transactionId);
    }

    @Test
    public void testGenerateTransactionId_SubsequentTransactions_ShouldIncrement() {
        when(transactionRepo.getLatestTransactionId()).thenReturn(100);

        Integer transactionId1 = HelperServices.generateTransactionId(transactionRepo);
        Integer transactionId2 = HelperServices.generateTransactionId(transactionRepo);

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
        when(productsRepo.findById(1)).thenReturn(Optional.empty());

        FoundException exception = assertThrows(FoundException.class, () -> helperServices.StoreOrder(List.of(order), true));
        assertEquals("Product not found for ID: 1", exception.getMessage());
    }

    @Test
    public void testStoreOrder_SuccessfulOrderProcessing() {
        Orders order1 = new Orders(1, 2);
        Orders order2 = new Orders(2, 3);
        Products product1 = new Products(1, "Product1", 10, 100.0);
        Products product2 = new Products(2, "Product2", 15, 200.0);

        when(productsRepo.findById(1)).thenReturn(Optional.of(product1));
        when(productsRepo.findById(2)).thenReturn(Optional.of(product2));
        when(transactionRepo.getLatestTransactionId()).thenReturn(100);

        helperServices.StoreOrder(Arrays.asList(order1, order2), true);

        verify(productsRepo, times(2)).save(any(Products.class));
        verify(historyRepo, times(2)).save(any(History.class));
        verify(transactionRepo, times(1)).save(any(Transaction.class));
    }

    @Test
    public void testStoreOrder_ValidOrders_ShouldUpdateProductAndHistory() {
        Orders order = new Orders(1, 5);
        Products product = new Products(1, "Test Product", 10, 100.0);
        when(productsRepo.findById(order.getId())).thenReturn(Optional.of(product));
        when(transactionRepo.getLatestTransactionId()).thenReturn(null);

        helperServices.StoreOrder(Collections.singletonList(order), true);

        // Verify product quantity update and save
        verify(productsRepo, times(1)).save(any(Products.class));
        assertEquals(5, product.getProductsQTY());

        // Verify history creation and save
        verify(historyRepo, times(1)).save(any(History.class));

        // Verify transaction creation and save
        verify(transactionRepo, times(1)).save(any(Transaction.class));
    }

}
