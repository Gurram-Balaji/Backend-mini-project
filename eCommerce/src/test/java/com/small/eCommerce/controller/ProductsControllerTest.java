package com.small.eCommerce.controller;

import com.small.eCommerce.model.Products;
import com.small.eCommerce.model.WrapperToInventory;
import com.small.eCommerce.responseHandler.ApiResponse;
import com.small.eCommerce.service.ProductsServices;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

public class ProductsControllerTest {

    @Mock
    private ProductsServices productsServices;

    @InjectMocks
    private ProductsController productsController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Test for AddProducts endpoint
    @Test
    public void testAddProducts_Success() {
        List<Products> productsList = List.of(new Products(1, "Product 1", 10, 100.0));
        when(productsServices.AddNewProducts(anyList())).thenReturn(productsList);

        ResponseEntity<ApiResponse<List<Products>>> response = productsController.AddProducts(productsList);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertTrue(Objects.requireNonNull(response.getBody()).isSuccess());
        assertEquals("Products has been added.", response.getBody().getMessage());
        assertEquals(productsList, response.getBody().getPayload());
    }

    // Test for getAllProducts (Inventory) endpoint
    @Test
    public void testGetAllProducts_InventoryNotEmpty() {
        List<WrapperToInventory> inventoryList = List.of(new WrapperToInventory("Product 1", 10));
        when(productsServices.fetchInventory()).thenReturn(inventoryList);

        ResponseEntity<ApiResponse<List<WrapperToInventory>>> response = productsController.getAllProducts();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(Objects.requireNonNull(response.getBody()).isSuccess());
        assertEquals("Inventory fetched successfully.", response.getBody().getMessage());
        assertEquals(inventoryList, response.getBody().getPayload());
    }

    @Test
    public void testGetAllProducts_InventoryEmpty() {
        when(productsServices.fetchInventory()).thenReturn(new ArrayList<>());

        ResponseEntity<ApiResponse<List<WrapperToInventory>>> response = productsController.getAllProducts();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertFalse(Objects.requireNonNull(response.getBody()).isSuccess());
        assertEquals("Inventory is Empty.", response.getBody().getMessage());
        assertEquals(new ArrayList<>(), response.getBody().getPayload());
    }

    // Test for getProductById endpoint
    @Test
    public void testGetProductById_ProductFound() {
        Products product = new Products(1, "Product 1", 10, 100.0);
        when(productsServices.findProductById(anyInt())).thenReturn(Optional.of(product));

        ResponseEntity<ApiResponse<Products>> response = productsController.getProductById(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(Objects.requireNonNull(response.getBody()).isSuccess());
        assertEquals("Product id: 1, Found.", response.getBody().getMessage());
        assertEquals(product, response.getBody().getPayload());
    }

    @Test
    public void testGetProductById_ProductNotFound() {
        when(productsServices.findProductById(anyInt())).thenReturn(Optional.empty());

        ResponseEntity<ApiResponse<Products>> response = productsController.getProductById(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertFalse(Objects.requireNonNull(response.getBody()).isSuccess());
        assertEquals("Product id: 1, Not found.!", response.getBody().getMessage());
        assertEquals(new Products(), response.getBody().getPayload());
    }

    // Test for getAllProduct endpoint
    @Test
    public void testGetAllProduct_ProductsFound() {
        List<Products> productsList = List.of(new Products(1, "Product 1", 10, 100.0));
        when(productsServices.findAllProduct()).thenReturn(productsList);

        ResponseEntity<ApiResponse<List<Products>>> response = productsController.getAllProduct();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(Objects.requireNonNull(response.getBody()).isSuccess());
        assertEquals("Products Found.", response.getBody().getMessage());
        assertEquals(productsList, response.getBody().getPayload());
    }

    @Test
    public void testGetAllProduct_ProductsNotFound() {
        when(productsServices.findAllProduct()).thenReturn(new ArrayList<>());

        ResponseEntity<ApiResponse<List<Products>>> response = productsController.getAllProduct();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertFalse(Objects.requireNonNull(response.getBody()).isSuccess());
        assertEquals("Products not Found.", response.getBody().getMessage());
        assertEquals(new ArrayList<>(), response.getBody().getPayload());
    }

    // Test for updateProductPrice endpoint
    @Test
    public void testUpdateProductPrice_ProductFound() {
        Products updatedProduct = new Products(1, "Product 1", 10, 120.0);
        when(productsServices.updateProductPrice(anyInt(), any(Products.class))).thenReturn(Optional.of(updatedProduct));

        ResponseEntity<ApiResponse<Products>> response = productsController.getProductById(1, new Products());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(Objects.requireNonNull(response.getBody()).isSuccess());
        assertEquals("Product id: 1 Found and Updated cost.", response.getBody().getMessage());
        assertEquals(updatedProduct, response.getBody().getPayload());
    }

    @Test
    public void testUpdateProductPrice_ProductNotFound() {
        when(productsServices.updateProductPrice(anyInt(), any(Products.class))).thenReturn(Optional.empty());

        ResponseEntity<ApiResponse<Products>> response = productsController.getProductById(1, new Products());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertFalse(Objects.requireNonNull(response.getBody()).isSuccess());
        assertEquals("Product id: 1 Not found, so could not update cost.!", response.getBody().getMessage());
        assertEquals(new Products(), response.getBody().getPayload());
    }

    // Test for DeleteProductById endpoint
    @Test
    public void testDeleteProductById_Success() {
        doNothing().when(productsServices).DeleteProductById(anyInt());

        ResponseEntity<ApiResponse<String>> response = productsController.DeleteProductById(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(Objects.requireNonNull(response.getBody()).isSuccess());
        assertEquals("Product id: 1 Found.", response.getBody().getMessage());
        assertEquals("Product Deleted.", response.getBody().getPayload());
    }

    // Test for DeleteAllProducts endpoint
    @Test
    public void testDeleteAllProducts_Success() {
        doNothing().when(productsServices).DeleteAllProducts();

        ResponseEntity<ApiResponse<String>> response = productsController.DeleteProduct();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(Objects.requireNonNull(response.getBody()).isSuccess());
        assertEquals("Products Found.", response.getBody().getMessage());
        assertEquals("Product Deleted.", response.getBody().getPayload());
    }
}
