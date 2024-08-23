package com.small.eCommerce.helper;

import com.small.eCommerce.dao.HistoryDao;
import com.small.eCommerce.dao.ProductDao;
import com.small.eCommerce.dao.TransactionDao;
import com.small.eCommerce.exception.FoundException;
import com.small.eCommerce.model.History;
import com.small.eCommerce.model.Orders;
import com.small.eCommerce.model.Products;
import com.small.eCommerce.model.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class HelperServices {

    @Autowired
    HistoryDao HistoryDao;
    @Autowired
    TransactionDao TransactionDao;
    @Autowired
    ProductDao productsDao;

    private static Integer currentTransactionId = null;

    public static void setCurrentTransactionId(Integer currentTransactionId) {
        HelperServices.currentTransactionId = currentTransactionId;
    }

    public void StoreOrder(List<Orders> saleOrders, Boolean transactionType) {
        if (saleOrders == null || saleOrders.isEmpty()) {
            throw new FoundException("Order list cannot be null or empty.");
        }
        // Creating new transaction and getting transactionId for the whole order
        Integer transactionId = generateTransactionId(TransactionDao);

        // Memory to store total count and cost of items from Transaction
        AtomicReference<Integer> itemCount = new AtomicReference<>(0);
        AtomicReference<Double> totalCost = new AtomicReference<>(0.0);
        // Looping through each order and storing each with one TransactionId
        for (Orders order : saleOrders) {
            itemCount.getAndSet(itemCount.get() + 1);
            Optional<Products> optionalProduct = productsDao.FindProductById(order.getId());

            if (optionalProduct.isEmpty()) {
                throw new FoundException("Product not found for ID: " + order.getId());
            }

            Products product = optionalProduct.get();

            if (transactionType) {
                product.setProductsQTY(product.getProductsQTY() - order.getQty());
            } else {
                product.setProductsQTY(product.getProductsQTY() + order.getQty());
            }
            productsDao.SaveProduct(product);

            Double itemPrice = product.getProductsCost();
            totalCost.updateAndGet(v -> v + itemPrice * order.getQty());

            HistoryDao.saveHistory(new History(transactionId, order.getId(), order.getQty(), itemPrice));
        }

        // Updating Items count and Cost in transaction
        Transaction transactionObject = new Transaction(transactionId,transactionType, totalCost.get(), itemCount.get(), new Date());
        TransactionDao.saveTransaction(transactionObject);
    }

    // Synchronized method to handle transaction ID generation and storage
    public static synchronized Integer generateTransactionId(TransactionDao TransactionDao) {
        if (currentTransactionId == null) {
            // Fetch the latest transaction ID from the database
            currentTransactionId = TransactionDao.getNextTransactionId();
            if (currentTransactionId == null) {
                currentTransactionId = 0; // Start from 1 if no transactions exist
            }
        }
        // Return the new transaction ID
        return ++currentTransactionId;
    }

}




