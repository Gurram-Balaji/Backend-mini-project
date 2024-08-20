package com.small.eCommerce.controller;

import com.small.eCommerce.model.Orders;
import com.small.eCommerce.responseHandler.ApiResponse;
import com.small.eCommerce.service.PurchaseServices;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

public class PurchaseControllerTest {

    @Mock
    private PurchaseServices PurchaseServices;

    @InjectMocks
    private PurchaseController PurchaseController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Test for AddPurchase endpoint with successful purchase
    @Test
    public void testAddPurchase_Success() {
        List<Orders> ordersList = Arrays.asList(new Orders(1, 5), new Orders(2, 10));
        when(PurchaseServices.AddPurchase(anyList())).thenReturn(ordersList);

        ResponseEntity<ApiResponse<List<Orders>>> response = PurchaseController.AddPurchase(ordersList);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(Objects.requireNonNull(response.getBody()).isSuccess());
        assertEquals("Purchase Placed.", response.getBody().getMessage());
        assertEquals(ordersList, response.getBody().getPayload());
    }

    // Test for AddPurchase endpoint with empty purchase list
    @Test
    public void testAddPurchase_EmptyList() {
        List<Orders> emptyList = new ArrayList<>();
        when(PurchaseServices.AddPurchase(emptyList)).thenReturn(emptyList);

        ResponseEntity<ApiResponse<List<Orders>>> response = PurchaseController.AddPurchase(emptyList);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertFalse(Objects.requireNonNull(response.getBody()).isSuccess());
        assertEquals("Purchase couldn't be placed.", response.getBody().getMessage());
        assertEquals(emptyList, response.getBody().getPayload());
    }

    // Test for AddPurchase endpoint with null purchase list
    @Test
    public void testAddPurchase_NullList() {
        when(PurchaseServices.AddPurchase(null)).thenReturn(null);

        ResponseEntity<ApiResponse<List<Orders>>> response = PurchaseController.AddPurchase(null);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertFalse(Objects.requireNonNull(response.getBody()).isSuccess());
        assertEquals("Purchase couldn't be placed.", response.getBody().getMessage());
        assertEquals(new ArrayList<>(), response.getBody().getPayload());
    }

    // Test for AddPurchase endpoint with a purchase list containing invalid orders
    @Test
    public void testAddPurchase_InvalidOrders() {
        List<Orders> invalidOrdersList = Arrays.asList(new Orders(null, 5), new Orders(2, null));
        when(PurchaseServices.AddPurchase(invalidOrdersList)).thenReturn(new ArrayList<>());

        ResponseEntity<ApiResponse<List<Orders>>> response = PurchaseController.AddPurchase(invalidOrdersList);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertFalse(Objects.requireNonNull(response.getBody()).isSuccess());
        assertEquals("Purchase couldn't be placed.", response.getBody().getMessage());
        assertEquals(new ArrayList<>(), response.getBody().getPayload());
    }

    // Test for AddPurchase endpoint with a list of orders where the quantities are zero
    @Test
    public void testAddPurchase_ZeroQuantities() {
        List<Orders> zeroQuantityOrdersList = Arrays.asList(new Orders(1, 0), new Orders(2, 0));
        when(PurchaseServices.AddPurchase(zeroQuantityOrdersList)).thenReturn(zeroQuantityOrdersList);

        ResponseEntity<ApiResponse<List<Orders>>> response = PurchaseController.AddPurchase(zeroQuantityOrdersList);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(Objects.requireNonNull(response.getBody()).isSuccess());
        assertEquals("Purchase Placed.", response.getBody().getMessage());
        assertEquals(zeroQuantityOrdersList, response.getBody().getPayload());
    }

    // Test for AddPurchase endpoint with a large number of orders
    @Test
    public void testAddPurchase_LargeNumberOfOrders() {
        List<Orders> largeOrdersList = new ArrayList<>();
        for (int i = 1; i <= 1000; i++) {
            largeOrdersList.add(new Orders(i, i));
        }
        when(PurchaseServices.AddPurchase(largeOrdersList)).thenReturn(largeOrdersList);

        ResponseEntity<ApiResponse<List<Orders>>> response = PurchaseController.AddPurchase(largeOrdersList);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(Objects.requireNonNull(response.getBody()).isSuccess());
        assertEquals("Purchase Placed.", response.getBody().getMessage());
        assertEquals(largeOrdersList, response.getBody().getPayload());
    }
}
