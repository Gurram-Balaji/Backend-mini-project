package com.small.eCommerce.Service;

import com.small.eCommerce.Exception.FoundException;
import com.small.eCommerce.Model.Orders;
import com.small.eCommerce.Model.Products;
import com.small.eCommerce.Repository.ProductsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PurchaseServices {


    @Autowired
    ProductsRepo productsRepo;

    private final HelperServices helperServices;

    // Autowiring the HelperService through constructor injection
    @Autowired
    public PurchaseServices(HelperServices helperService) {
        this.helperServices = helperService;
    }

    //Loops through each object and fetch there present qty and updates the products qty + present qty
    public List<Orders> AddPurchase(List<Orders> addPurchasesList) {

        addPurchasesList.forEach(orders -> {
            //Check for the required qty is not available i.e., Out Of Stock and ID is not found
            Optional<Products> product=productsRepo.findById(orders.getId());
            if(product.isEmpty())
                throw new FoundException("Product id: "+ orders.getId()+" not found.");
            if (orders.getQty()<=0)
                throw new FoundException("Product id: "+ orders.getId()+" quantity should be positive.");
        });

            //Store transaction in history entity
            helperServices.StoreOrder(addPurchasesList,Boolean.FALSE);
        return addPurchasesList;
    }




}
