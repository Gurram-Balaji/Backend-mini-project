package com.small.eCommerce.Service;
import com.small.eCommerce.Exception.FoundException;
import com.small.eCommerce.Repository.ProductsRepo;
import com.small.eCommerce.Model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ProductsServices {

    @Autowired
    ProductsRepo productsRepo;

    //Stores the list of objects passed to it.
    public List<Products> AddNewProducts(List<Products> products) {
            return productsRepo.saveAll(products);
    }

    //Get Product Inventory only
    public List<WrapperToInventory> fetchInventory() {
        List<Products> Products = productsRepo.findAll();
        if(!Products.isEmpty()) {
            List<WrapperToInventory> Inventory = new ArrayList<>();
            //looping all products and converting to wrapper to display only name and qty
            for (Products product : Products) {
                WrapperToInventory inv = new WrapperToInventory(product.getProductsName(), product.getProductsQTY());
                Inventory.add(inv);
            }
            return Inventory;
        }else{
            throw new FoundException("Inventory is empty.");
        }
    }



    //Get Product by query ID and converted single product to list a not present return empty list
    public Optional<Products> findProductById(Integer id) {
        Optional<Products> product= productsRepo.findById(id);
        if(product.isPresent())
            return product;
        else
            throw new FoundException("Product id "+id+" didn't found.");
    }

    //Get All Products, and it is called when query id is not passed
    public List<Products> findAllProduct() {
        List<Products> products=productsRepo.findAll();
        if(products.isEmpty())
            throw new FoundException("No Products Available.");
        else
            return products;
    }

    //Updating the cost of products
    public Optional<Products> updateProductPrice(Integer id, Products products) {
        Optional<Products> products1= productsRepo.findById(id).map(existingUser -> {
            if (products.getProductsCost() != null)
                if(products.getProductsCost()>0)
                    existingUser.setProductsCost(products.getProductsCost());
                else
                    throw new FoundException("Cost should be positive.");
            else
                throw new FoundException("Cost is mandatory.");
            return productsRepo.save(existingUser);
        });
        if(products1.isEmpty())
            throw new FoundException("Product id: "+id+" not found.");
        return products1;
    }

    //Delete Products by id
    public void DeleteProductById(Integer id) {
        if(productsRepo.findById(id).isPresent())
            productsRepo.deleteById(id);
        else
            throw new FoundException("Product id: "+id+" not found.");
    }

    //Delete All products
    public void DeleteAllProducts() {
        productsRepo.deleteAll();
    }


}
