package com.small.eCommerce.service;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import com.small.eCommerce.exception.FoundException;
import com.small.eCommerce.helper.HelperServices;
import com.small.eCommerce.model.Orders;
import com.small.eCommerce.model.Products;
import com.small.eCommerce.dao.ProductDao;
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
    private ProductDao ProductDao;

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
        // Verifies that with an empty order list, ProductDao and helperServices are not called
        List<Orders> emptyOrders = new ArrayList<>();
        assertThrows(FoundException.class, () -> purchaseServices.AddPurchase(emptyOrders));

        verify(ProductDao, never()).FindProductById(anyInt());
        verify(helperServices, never()).StoreOrder(anyList(), anyBoolean());
    }

    @Test
    void testAddPurchase_ProductNotFound() {
        // Checks that a FoundException is thrown if the product is not found in the repository
        Orders order = new Orders(1, 5);

        when(ProductDao.FindProductById(order.getId())).thenReturn(Optional.empty());

        assertThrows(FoundException.class, () -> purchaseServices.AddPurchase(List.of(order)));
    }

    @Test
    void testAddPurchase_NonPositiveQuantity() {
        // Ensures that a FoundException is thrown for orders with zero or negative quantities

        Orders orderZeroQuantity = new Orders(1, 0);
        when(ProductDao.FindProductById(orderZeroQuantity.getId())).thenReturn(Optional.of(new Products(1, "Product1", 10, 50.0)));
        assertThrows(FoundException.class, () -> purchaseServices.AddPurchase(List.of(orderZeroQuantity)));

        Orders orderNegativeQuantity = new Orders(1, -1);
        when(ProductDao.FindProductById(orderNegativeQuantity.getId())).thenReturn(Optional.of(new Products(1, "Product1", 10, 50.0)));
        assertThrows(FoundException.class, () -> purchaseServices.AddPurchase(List.of(orderNegativeQuantity)));
    }

    @Test
    void testAddPurchase_Success() {
        // Tests that the method successfully processes orders when valid input is provided
        Orders order = new Orders(1, 10);
        Products product = new Products(1, "Product1", 10, 50.0);

        when(ProductDao.FindProductById(order.getId())).thenReturn(Optional.of(product));

        List<Orders> ordersList = List.of(order);
        List<Orders> result = purchaseServices.AddPurchase(ordersList);

        verify(helperServices).StoreOrder(ordersList, Boolean.FALSE);
        assertEquals(ordersList, result);
    }
}
