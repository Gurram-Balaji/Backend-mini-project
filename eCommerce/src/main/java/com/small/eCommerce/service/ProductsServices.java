package com.small.eCommerce.service;

import com.small.eCommerce.dao.ProductDao;
import com.small.eCommerce.exception.FoundException;
import com.small.eCommerce.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ProductsServices {

    @Autowired
    ProductDao productsDao;

    //Stores the list of objects passed to it.
    public List<Products> AddNewProducts(List<Products> products) {
        return productsDao.SaveAllProducts(products);
    }

    //Get Product Inventory only
    public List<WrapperToInventory> fetchInventory() {
        List<Products> Products = productsDao.FindAllProducts();
        if (!Products.isEmpty()) {
            List<WrapperToInventory> Inventory = new ArrayList<>();
            //looping all products and converting to wrapper to display only name and qty
            for (Products product : Products) {
                WrapperToInventory inv = new WrapperToInventory(product.getProductsName(), product.getProductsQTY());
                Inventory.add(inv);
            }
            return Inventory;
        } else {
            throw new FoundException("Inventory is empty.");
        }
    }


    //Get Product by query ID and converted single product to list a not present return empty list
    public Optional<Products> findProductById(Integer id) {
        Optional<Products> product = productsDao.FindProductById(id);
        if (product.isPresent()) return product;
        else throw new FoundException("Product id " + id + " didn't found.");
    }

    //Get All Products, and it is called when query id is not passed
    public List<Products> findAllProduct() {
        List<Products> products = productsDao.FindAllProducts();
        if (products.isEmpty()) throw new FoundException("No Products Available.");
        else return products;
    }

    //Updating the cost of products
    public Optional<Products> updateProductPrice(Integer id, Products products) {
        if (products.getProductsCost() != null) {
            if (products.getProductsCost() > 0) {
                Optional<Products> products1 = productsDao.FindProductById(id).map(existingUser -> {
                    existingUser.setProductsCost(products.getProductsCost());
                    return productsDao.SaveProduct(existingUser);
                });
                if (products1.isEmpty()) throw new FoundException("Product id: " + id + " not found.");
                return products1;
            } else throw new FoundException("Cost should be positive.");
        } else throw new FoundException("Cost is mandatory.");
    }

    //Delete Products by id
    public void DeleteProductById(Integer id) {
        if (productsDao.FindProductById(id).isPresent()) productsDao.DeleteProductById(id);
        else throw new FoundException("Product id: " + id + " not found.");
    }

    //Delete All products
    public void DeleteAllProducts() {
        productsDao.DeleteAllProduct();
    }


}
