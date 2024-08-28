package com.small.eCommerce.controller;

import com.small.eCommerce.model.*;
import com.small.eCommerce.responseHandler.ApiResponse;
import com.small.eCommerce.service.ProductsServices;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@Validated
public class ProductsController {

    @Autowired
    ProductsServices productsServices;

    //Adds List of Products to DB, Pass List of Products Object without id.
    @PostMapping("/AddProducts")
    public ResponseEntity<ApiResponse<List<Products>>> AddProducts(@Valid @RequestBody List<Products> products)
    {
        List<Products> Payload=productsServices.AddNewProducts(products);
        boolean Success=true;
        String Message="Products has been added.";
        ApiResponse<List<Products>> response = new ApiResponse<>(Success, Message, Payload);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    //Get product's Inventory
    @GetMapping("/Inventory")
    public ResponseEntity<ApiResponse<List<WrapperToInventory>>> getAllProducts() {
        ApiResponse<List<WrapperToInventory>> response;
        Optional<List<WrapperToInventory>> Payload= Optional.ofNullable(productsServices.fetchInventory());
        response = Payload
                .filter(wrapperToInventories -> !wrapperToInventories.isEmpty())
                .map(wrapperToInventories -> new ApiResponse<>(true, "Inventory fetched successfully.", wrapperToInventories))
                .orElseGet(() -> new ApiResponse<>(false, "Inventory is Empty.", new ArrayList<>()));
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    //Get product's by Query ID or Get all products without passing id
    @GetMapping("/GetProduct/{id}")
    public ResponseEntity<ApiResponse<Products>> getProductById(@PathVariable Integer id) {
        ApiResponse<Products> response;
        Optional<Products> Payload= productsServices.findProductById(id);
        response = Payload
                .map(eachPayload -> new ApiResponse<>(true, "Product id: "+id+", Found.", eachPayload))
                .orElseGet(() -> new ApiResponse<>(false, "Product id: "+id+", Not found.!", new Products()));
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    //Get product's by Query ID or Get all products without passing id
    @GetMapping("/GetAllProducts")
    public ResponseEntity<ApiResponse<List<Products>>> getAllProduct() {
        ApiResponse<List<Products>> response;
        Optional<List<Products>> Payload= Optional.ofNullable(productsServices.findAllProduct());
        response = Payload
                .filter(eachPayload -> !eachPayload.isEmpty())
                .map(eachPayload -> new ApiResponse<>(true, "Products Found.", eachPayload))
                .orElseGet(() -> new ApiResponse<>(false, "Products not Found.", new ArrayList<>()));
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    //Update the cost of products
    @PatchMapping("/UpdateCost/{id}")
    public ResponseEntity<ApiResponse<Products>> getProductById(@PathVariable Integer id, @RequestBody Products products) {
        ApiResponse<Products> response;
        Optional<Products> Payload= productsServices.updateProductPrice(id, products);
        response = Payload
                .map(eachPayload -> new ApiResponse<>(true, "Product id: "+id+" Found and Updated cost.", eachPayload))
                .orElseGet(() -> new ApiResponse<>(false, "Product id: "+id+" Not found, so could not update cost.!", new Products()));
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    //Deleting product's by Query ID or Deleting all products without passing id
    @DeleteMapping("/DeleteProduct/{id}")
    public ResponseEntity<ApiResponse<String>> DeleteProductById(@PathVariable Integer id) {
        productsServices.DeleteProductById(id);
        return ResponseEntity.status(HttpStatus.OK).body( new ApiResponse<>(true, "Product id: "+id+" Found.", "Product Deleted."));
    }

    //Deleting product's by Query ID or Deleting all products without passing id
    @DeleteMapping("/DeleteAllProducts")
    public ResponseEntity<ApiResponse<String>> DeleteProduct() {
            productsServices.DeleteAllProducts();
        return ResponseEntity.status(HttpStatus.OK).body( new ApiResponse<>(true, "Products Found.", "Product Deleted."));
    }
}
