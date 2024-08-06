package com.small.eCommerce.Service;

import com.small.eCommerce.Exception.FoundException;
import com.small.eCommerce.Model.History;
import com.small.eCommerce.Model.Orders;
import com.small.eCommerce.Model.Products;
import com.small.eCommerce.Model.Transaction;
import com.small.eCommerce.Repository.HistoryRepo;
import com.small.eCommerce.Repository.ProductsRepo;
import com.small.eCommerce.Repository.TransactionRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class HelperServices {

    @Autowired
    ProductsRepo productsRepo;
    @Autowired
    HistoryRepo HistoryRepo;
    @Autowired
    TransactionRepo TransactionRepo;

    //Storing all types of transaction in DB
    //True for sales Order
    //False for Purchase Order
    public void StoreOrder(List<Orders> saleOrders,Boolean TransactionType) {
        //Creating new transaction and getting transactionId for whole order
        Transaction TransactionObject=new Transaction(TransactionType, new Date());
        Integer TransactionId = TransactionRepo.save(TransactionObject).getTransactionId();

        //Memory to store total count and cost of items from Transaction
        AtomicReference<Integer> ItemCount= new AtomicReference<>(0);
        AtomicReference<Double> TotalCost= new AtomicReference<>(0.0);

        //Looping through each Transaction and storing each with one TransactionId
        saleOrders.forEach(order ->{
            ItemCount.getAndSet(ItemCount.get() + 1);
            Products product = productsRepo.findById(order.getId())
                    .orElseThrow(() -> new FoundException("Product not found for ID: " + order.getId()));
            if(TransactionType)
                product.setProductsQTY(product.getProductsQTY()-order.getQty());
            else
                product.setProductsQTY(product.getProductsQTY()+order.getQty());
            productsRepo.save(product);
            Double itemPrice = product.getProductsCost();
            // Use itemPrice as needed
            TotalCost.updateAndGet(v -> v + itemPrice * order.getQty());
            HistoryRepo.save(new History(TransactionId,order.getId(),order.getQty(),itemPrice));
        } );

        //Updating Items count and Cost in transaction
        TransactionObject.setTransactionId(TransactionId);
        TransactionObject.setTransactionTotalCost(TotalCost.get());
        TransactionObject.setTransactionTotalItems(ItemCount.get());
        TransactionRepo.save(TransactionObject);
    }
}
