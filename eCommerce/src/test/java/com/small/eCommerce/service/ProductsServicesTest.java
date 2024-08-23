package com.small.eCommerce.service;

import com.small.eCommerce.exception.FoundException;
import com.small.eCommerce.model.Products;
import com.small.eCommerce.model.WrapperToInventory;
import com.small.eCommerce.dao.ProductDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ProductsServicesTest {

    @Mock
    private ProductDao ProductDao;

    @InjectMocks
    private ProductsServices productsServices;

    @Test
    public void testAddNewProducts_success() {
        // Arrange
        List<Products> products = new ArrayList<>();
        products.add(new Products("Product 1", 10, 10.99));
        products.add(new Products("Product 2", 20, 20.99));

        when(ProductDao.SaveAllProducts(products)).thenReturn(products);

        // Act
        List<Products> result = productsServices.AddNewProducts(products);

        // Assert
        assertEquals(products, result);
        verify(ProductDao, times(1)).SaveAllProducts(products);
    }

    @Test
    public void testFetchInventory_success() {
        // Arrange
        List<Products> products = new ArrayList<>();
        products.add(new Products("Product 1", 10, 10.99));
        products.add(new Products("Product 2", 20, 20.99));

        when(ProductDao.FindAllProducts()).thenReturn(products);

        // Act
        List<WrapperToInventory> result = productsServices.fetchInventory();

        // Assert
        assertEquals(2, result.size());
        assertThat(result.get(0).getProductsName(), is("Product 1"));
        assertThat(result.get(0).getProductsQTY(), is(10));
        assertThat(result.get(1).getProductsName(), is("Product 2"));
        assertThat(result.get(1).getProductsQTY(), is(20));
        verify(ProductDao, times(1)).FindAllProducts();
    }

    @Test
    public void testFetchInventory_empty() {
        // Arrange
        when(ProductDao.FindAllProducts()).thenReturn(new ArrayList<>());

        // Act and Assert
        assertThrows(FoundException.class, () -> productsServices.fetchInventory());
        verify(ProductDao, times(1)).FindAllProducts();
    }

    @Test
    public void testFindProductById_success() {
        // Arrange
        Integer id = 1;
        Products product = new Products("Product 1", 10, 10.99);

        when(ProductDao.FindProductById(id)).thenReturn(Optional.of(product));

        // Act
        Optional<Products> result = productsServices.findProductById(id);

        // Assert
        assertEquals(Optional.of(product), result);
        verify(ProductDao, times(1)).FindProductById(id);
    }

    @Test
    public void testFindProductById_notFound() {
        // Arrange
        Integer id = 1;

        when(ProductDao.FindProductById(id)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(FoundException.class, () -> productsServices.findProductById(id));
        verify(ProductDao, times(1)).FindProductById(id);
    }

    @Test
    public void testFindAllProduct_success() {
        // Arrange
        List<Products> products = new ArrayList<>();
        products.add(new Products("Product 1", 10, 10.99));
        products.add(new Products("Product 2", 20, 20.99));

        when(ProductDao.FindAllProducts()).thenReturn(products);

        // Act
        List<Products> result = productsServices.findAllProduct();

        // Assert
        assertEquals(products, result);
        verify(ProductDao, times(1)).FindAllProducts();
    }

    @Test
    public void testFindAllProduct_empty() {
        // Arrange
        when(ProductDao.FindAllProducts()).thenReturn(new ArrayList<>());

        // Act and Assert
        assertThrows(FoundException.class, () -> productsServices.findAllProduct());
        verify(ProductDao, times(1)).FindAllProducts();
    }

    @Test
    public void testUpdateProductPrice_success() {
        // Arrange
        Integer id = 1;
        Products product = new Products("Product 1", 10, 10.99);
        Products updatedProduct = new Products("Product 1", 10, 15.99);

        when(ProductDao.FindProductById(id)).thenReturn(Optional.of(product));
        when(ProductDao.SaveProduct(any(Products.class))).thenReturn(updatedProduct);

        // Act
        Optional<Products> result = productsServices.updateProductPrice(id, updatedProduct);

        // Assert
        assertEquals(Optional.of(updatedProduct), result);
        verify(ProductDao, times(1)).FindProductById(id);
        verify(ProductDao, times(1)).SaveProduct(any(Products.class));
    }

    @Test
    public void testUpdateProductPrice_costIsNull() {
        // Arrange
        Integer id = 1;
        Products product = new Products("Product 1", 10, null);

        // Act and Assert
        assertThrows(FoundException.class, () -> productsServices.updateProductPrice(id, product));
        verify(ProductDao, never()).FindProductById(id);
        verify(ProductDao, never()).SaveProduct(any(Products.class));
    }

    @Test
    public void testUpdateProductPrice_costIsNegative() {
        // Arrange
        Integer id = 1;
        Products product = new Products("Product 1", 10, -10.99);

        // Act and Assert
        assertThrows(FoundException.class, () -> productsServices.updateProductPrice(id, product));
        verify(ProductDao, never()).FindProductById(id);
        verify(ProductDao, never()).SaveProduct(any(Products.class));
    }

    @Test
    public void testUpdateProductPrice_productNotFound() {
        // Arrange
        Integer id = 1;
        Products product = new Products("Product 1", 10, 15.99);

        when(ProductDao.FindProductById(id)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(FoundException.class, () -> productsServices.updateProductPrice(id, product));
        verify(ProductDao, times(1)).FindProductById(id);
        verify(ProductDao, never()).SaveProduct(any(Products.class));
    }

    @Test
    public void testDeleteProductById_success() {
        // Arrange
        Integer id = 1;
        Products product = new Products("Product 1", 10, 10.99);

        when(ProductDao.FindProductById(id)).thenReturn(Optional.of(product));

        // Act
        productsServices.DeleteProductById(id);

        // Assert
        verify(ProductDao, times(1)).FindProductById(id);
        verify(ProductDao, times(1)).DeleteProductById(id);
    }

    @Test
    public void testDeleteProductById_notFound() {
        // Arrange
        Integer id = 1;

        when(ProductDao.FindProductById(id)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(FoundException.class, () -> productsServices.DeleteProductById(id));
        verify(ProductDao, times(1)).FindProductById(id);
        verify(ProductDao, never()).DeleteProductById(id);
    }

    @Test
    public void testDeleteAllProducts() {
        // Act
        productsServices.DeleteAllProducts();

        // Assert
        verify(ProductDao, times(1)).DeleteAllProduct();
    }
}