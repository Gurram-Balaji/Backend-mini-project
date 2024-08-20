package com.small.eCommerce.service;

import com.small.eCommerce.exception.FoundException;
import com.small.eCommerce.helper.HelperServices;
import com.small.eCommerce.model.Products;
import com.small.eCommerce.repository.ProductsRepo;
import com.small.eCommerce.model.Orders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class OrderServices {

    @Autowired
    ProductsRepo productsRepo;

    @Autowired
    HelperServices helperServices;

    public List<Orders> SalesOrder(List<Orders> saleOrders) {
        if (saleOrders == null) throw new NullPointerException("saleOrders cannot be null");
        if (saleOrders.isEmpty()) throw new FoundException("Order list cannot be empty.");

        Set<Integer> productIds = new HashSet<>();

        saleOrders.forEach(orders -> {
            if (!productIds.add(orders.getId()))
                throw new FoundException("Duplicate product ID found: " + orders.getId());

            Optional<Products> product = productsRepo.findById(orders.getId());
            if (product.isEmpty()) throw new FoundException("Product id: " + orders.getId() + " not found.");

            if (orders.getQty() <= 0)
                throw new FoundException("Quantity must be greater than zero for product id: " + orders.getId());

            if (product.get().getProductsQTY() < orders.getQty())
                throw new FoundException("Product id: " + orders.getId() + " is Out Of Stock.");
        });

        helperServices.StoreOrder(saleOrders, Boolean.TRUE);
        return saleOrders;
    }

}
