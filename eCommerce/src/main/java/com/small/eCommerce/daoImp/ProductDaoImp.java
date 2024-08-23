package com.small.eCommerce.daoImp;

import com.small.eCommerce.dao.ProductDao;
import com.small.eCommerce.model.Products;
import com.small.eCommerce.repository.ProductsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductDaoImp implements ProductDao {

    @Autowired
    ProductsRepo productsRepo;

    @Override
    public List<Products> SaveAllProducts(List<Products> products) {
        return productsRepo.saveAll(products);
    }

    @Override
    public List<Products> FindAllProducts() {
        return productsRepo.findAll();
    }

    @Override
    public Optional<Products> FindProductById(Integer id) {
        return productsRepo.findById(id);
    }

    @Override
    public Products SaveProduct(Products products) {
        return productsRepo.save(products);
    }

    @Override
    public void DeleteProductById(Integer id) {
        productsRepo.deleteById(id);
    }

    @Override
    public void DeleteAllProduct() {
        productsRepo.deleteAll();
    }
}
