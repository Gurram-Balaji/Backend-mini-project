package com.small.eCommerce.dao;

import com.small.eCommerce.model.Products;

import java.util.List;
import java.util.Optional;

public interface ProductDao {
    List<Products> SaveAllProducts(List<Products> products);
    List<Products> FindAllProducts();
    Optional<Products> FindProductById(Integer id);
    Products SaveProduct(Products products);
    void DeleteProductById(Integer id);
    void DeleteAllProduct();
}
