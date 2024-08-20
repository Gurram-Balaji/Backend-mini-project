package com.small.eCommerce.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

import com.small.eCommerce.model.Orders;
import com.small.eCommerce.responseHandler.ApiResponse;
import com.small.eCommerce.service.OrderServices;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class OrderControllerTest {

    @Mock
    private OrderServices orderServices;

    @InjectMocks
    private OrderController orderController;

    public OrderControllerTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSalesOrder_Success() {
        // Arrange
        List<Orders> mockOrders = new ArrayList<>();
        mockOrders.add(new Orders()); // Add valid orders
        when(orderServices.SalesOrder(anyList())).thenReturn(mockOrders);

        // Act
        ResponseEntity<ApiResponse<List<Orders>>> response = orderController.SalesOrder(mockOrders);
        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(Objects.requireNonNull(response.getBody()).isSuccess());
        assertEquals("Ordered Placed.", response.getBody().getMessage());
        assertEquals(mockOrders, response.getBody().getPayload());
    }

    @Test
    public void testSalesOrder_EmptyList() {
        // Arrange
        List<Orders> emptyOrders = new ArrayList<>();
        when(orderServices.SalesOrder(emptyOrders)).thenReturn(emptyOrders);

        // Act
        ResponseEntity<ApiResponse<List<Orders>>> response = orderController.SalesOrder(emptyOrders);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertFalse(Objects.requireNonNull(response.getBody()).isSuccess());
        assertEquals("Order couldn't be placed.", response.getBody().getMessage());
        assertEquals(emptyOrders, response.getBody().getPayload());
    }

    @Test
    public void testSalesOrder_NullList() {
        // Arrange
        when(orderServices.SalesOrder(null)).thenReturn(null);
        // Act
        ResponseEntity<ApiResponse<List<Orders>>> response = orderController.SalesOrder(null);
        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertFalse(Objects.requireNonNull(response.getBody()).isSuccess());
        assertEquals("Order couldn't be placed.", response.getBody().getMessage());
        assertEquals(new ArrayList<>(), response.getBody().getPayload());
    }

    @Test
    public void testSalesOrder_InvalidData() {
        // Arrange
        List<Orders> invalidOrders = new ArrayList<>(); // Add invalid orders if needed
        when(orderServices.SalesOrder(invalidOrders)).thenReturn(new ArrayList<>());

        // Act
        ResponseEntity<ApiResponse<List<Orders>>> response = orderController.SalesOrder(invalidOrders);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertFalse(Objects.requireNonNull(response.getBody()).isSuccess());
        assertEquals("Order couldn't be placed.", response.getBody().getMessage());
        assertEquals(new ArrayList<>(), response.getBody().getPayload());
    }
}