package com.small.eCommerce.service;
import com.small.eCommerce.exception.FoundException;
import com.small.eCommerce.helper.HelperServices;
import com.small.eCommerce.model.Orders;
import com.small.eCommerce.model.Products;
import com.small.eCommerce.dao.ProductDao;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

@Service
public class PurchaseServices {

    @Autowired
    ProductDao productsDao;

    @Autowired
    HelperServices helperServices;

    // Loops through each order, fetches the current quantity, and updates the product's quantity
    public List<Orders> AddPurchase(List<Orders> addPurchasesList) {

        // Handle null input list
        if (addPurchasesList == null) throw new NullPointerException("Order list cannot be null.");
        if (addPurchasesList.isEmpty()) throw new FoundException("Order list cannot be empty.");

        // Iterate through each order
        addPurchasesList.forEach(order -> {
            // Check if the product exists in the repository
            Optional<Products> productOptional = productsDao.FindProductById(order.getId());

            // If the product is not found, throw an exception
            if (productOptional.isEmpty()) {
                throw new FoundException("Product id: " + order.getId() + " not found.");
            }

            // Validate the quantity of the order
            if (order.getQty() <= 0) {
                throw new FoundException("Product id: " + order.getId() + " quantity should be positive.");
            }
        });

        // Store transaction in history entity
        helperServices.StoreOrder(addPurchasesList, Boolean.FALSE);

        return addPurchasesList;
    }
}
