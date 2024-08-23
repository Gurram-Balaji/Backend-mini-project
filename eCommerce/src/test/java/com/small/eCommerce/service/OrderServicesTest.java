package com.small.eCommerce.service;

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

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


@RunWith(MockitoJUnitRunner.class)
class OrderServicesTest {

    @Mock
    private ProductDao ProductDao;

    @Mock
    private HelperServices helperServices;

    @InjectMocks
    private OrderServices orderServices;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSalesOrder_ProductNotFound() {
        Orders order = new Orders(1, 5);
        when(ProductDao.FindProductById(order.getId())).thenReturn(Optional.empty());

        assertThrows(FoundException.class, () -> orderServices.SalesOrder(List.of(order)));

        verify(ProductDao, times(1)).FindProductById(order.getId());
        verify(helperServices, never()).StoreOrder(anyList(), anyBoolean());
    }

    @Test
    void testSalesOrder_ProductOutOfStock() {
        Orders order = new Orders(1, 5);
        Products product = new Products(1, "Product1", 2, 50.0);
        when(ProductDao.FindProductById(order.getId())).thenReturn(Optional.of(product));

        assertThrows(FoundException.class, () -> orderServices.SalesOrder(List.of(order)));

        verify(ProductDao, times(1)).FindProductById(order.getId());
        verify(helperServices, never()).StoreOrder(anyList(), anyBoolean());
    }

    @Test
    void testSalesOrder_Success() {
        List<Orders> order = new ArrayList<>();
        order.add(new Orders(1, 10));

        Products product = new Products(1, "Product1", 10, 50.0);
        when(ProductDao.FindProductById(1)).thenReturn(Optional.of(product));
        orderServices.SalesOrder(order);
        verify(ProductDao, times(1)).FindProductById(1);
        verify(helperServices, times(1)).StoreOrder(anyList(), eq(Boolean.TRUE));
    }


    @Test
    void testSalesOrder_NullInput() {
        assertThrows(NullPointerException.class, () -> orderServices.SalesOrder(null));
    }

    @Test
    void testSalesOrder_EmptyInput() {
        List<Orders> emptyOrders = new ArrayList<>();

        // When provided with an empty order list, it should not interact with ProductDao or helperServices
        assertThrows(FoundException.class, () -> orderServices.SalesOrder(emptyOrders));

        verify(ProductDao, never()).FindProductById(anyInt());
        verify(helperServices, never()).StoreOrder(anyList(), anyBoolean());
    }

    @Test
    void testSalesOrder_DuplicateProductIDs() {
        Orders order1 = new Orders(1, 2);
        Orders order2 = new Orders(1, 3); // Duplicate product ID

        when(ProductDao.FindProductById(order1.getId())).thenReturn(Optional.of(new Products(1, "Product1", 10, 50.0)));

        assertThrows(FoundException.class, () -> orderServices.SalesOrder(List.of(order1, order2)));
    }

    @Test
    void testSalesOrder_ZeroQuantity() {
        Orders orderZeroQuantity = new Orders(1, 0);

        when(ProductDao.FindProductById(orderZeroQuantity.getId())).thenReturn(Optional.of(new Products(1, "Product1", 10, 50.0)));

        assertThrows(FoundException.class, () -> orderServices.SalesOrder(List.of(orderZeroQuantity)));
    }

    @Test
    void testSalesOrder_NegativeQuantity() {
        Orders orderNegativeQuantity = new Orders(1, -1);

        when(ProductDao.FindProductById(orderNegativeQuantity.getId())).thenReturn(Optional.of(new Products(1, "Product1", 10, 50.0)));

        assertThrows(FoundException.class, () -> orderServices.SalesOrder(List.of(orderNegativeQuantity)));
    }

}