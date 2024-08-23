package com.small.eCommerce.dao;

import com.small.eCommerce.model.Transaction;

import java.util.List;
import java.util.Optional;

public interface TransactionDao {
    Optional<Transaction> findTransactionById(Integer id);
    List<Transaction> findAllTransactions();
    void deleteAllTransaction();
    Integer getAllOrdersCount();
    Integer getAllPurchasesCount();
    long getAllTransactionCount();
    void saveTransaction(Transaction transactionObject);
    Integer getNextTransactionId();
}
