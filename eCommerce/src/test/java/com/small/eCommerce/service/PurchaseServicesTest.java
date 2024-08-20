package com.small.eCommerce.service;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import com.small.eCommerce.exception.FoundException;
import com.small.eCommerce.helper.HelperServices;
import com.small.eCommerce.model.Orders;
import com.small.eCommerce.model.Products;
import com.small.eCommerce.repository.ProductsRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
class PurchaseServicesTest {

    @Mock
    private ProductsRepo productsRepo;

    @Mock
    private HelperServices helperServices;

    @InjectMocks
    private PurchaseServices purchaseServices;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddPurchase_NullInput() {
        // Tests that the method throws a NullPointerException when passed a null input list
        assertThrows(NullPointerException.class, () -> purchaseServices.AddPurchase(null));
    }

    @Test
    void testAddPurchase_EmptyInput() {
        // Verifies that with an empty order list, productsRepo and helperServices are not called
        List<Orders> emptyOrders = new ArrayList<>();
        assertThrows(FoundException.class, () -> purchaseServices.AddPurchase(emptyOrders));

        verify(productsRepo, never()).findById(anyInt());
        verify(helperServices, never()).StoreOrder(anyList(), anyBoolean());
    }

    @Test
    void testAddPurchase_ProductNotFound() {
        // Checks that a FoundException is thrown if the product is not found in the repository
        Orders order = new Orders(1, 5);

        when(productsRepo.findById(order.getId())).thenReturn(Optional.empty());

        assertThrows(FoundException.class, () -> purchaseServices.AddPurchase(List.of(order)));
    }

    @Test
    void testAddPurchase_NonPositiveQuantity() {
        // Ensures that a FoundException is thrown for orders with zero or negative quantities

        Orders orderZeroQuantity = new Orders(1, 0);
        when(productsRepo.findById(orderZeroQuantity.getId())).thenReturn(Optional.of(new Products(1, "Product1", 10, 50.0)));
        assertThrows(FoundException.class, () -> purchaseServices.AddPurchase(List.of(orderZeroQuantity)));

        Orders orderNegativeQuantity = new Orders(1, -1);
        when(productsRepo.findById(orderNegativeQuantity.getId())).thenReturn(Optional.of(new Products(1, "Product1", 10, 50.0)));
        assertThrows(FoundException.class, () -> purchaseServices.AddPurchase(List.of(orderNegativeQuantity)));
    }

    @Test
    void testAddPurchase_Success() {
        // Tests that the method successfully processes orders when valid input is provided
        Orders order = new Orders(1, 10);
        Products product = new Products(1, "Product1", 10, 50.0);

        when(productsRepo.findById(order.getId())).thenReturn(Optional.of(product));

        List<Orders> ordersList = List.of(order);
        List<Orders> result = purchaseServices.AddPurchase(ordersList);

        verify(helperServices).StoreOrder(ordersList, Boolean.FALSE);
        assertEquals(ordersList, result);
    }
}
