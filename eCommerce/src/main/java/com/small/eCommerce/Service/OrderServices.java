package com.small.eCommerce.Service;

import com.small.eCommerce.Exception.FoundException;
import com.small.eCommerce.Model.Products;
import com.small.eCommerce.Repository.ProductsRepo;
import com.small.eCommerce.Model.Orders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderServices {

    @Autowired
    ProductsRepo productsRepo;

    private final HelperServices helperServices;
    // Autowiring the HelperService through constructor injection
    @Autowired
    public OrderServices(HelperServices helperService) {
        this.helperServices = helperService;
    }

    //Order placing by - qty by order qty
    public List<Orders> SalesOrder(List<Orders> saleOrders) {

            saleOrders.forEach(orders -> {
                //Check for the required qty is not available i.e., Out Of Stock and ID is not found
               Optional<Products> product=productsRepo.findById(orders.getId());
                if(product.isEmpty())
                    throw new FoundException("Product id: "+ orders.getId()+" not found.");
                if (product.get().getProductsQTY() < orders.getQty())
                    throw new FoundException("Product id: "+ orders.getId()+" is Out Of Stock.");
            });
                //Store transaction
                helperServices.StoreOrder(saleOrders,Boolean.TRUE);
        return saleOrders;

    }
}
