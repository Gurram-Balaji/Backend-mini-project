package com.small.eCommerce.repository;

import com.small.eCommerce.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepo extends JpaRepository<Transaction,Integer> {

    //type is true for orders
    //type is false for purchase
    @Query(value = "SELECT COUNT(*) FROM transaction t Where t.transaction_type=:type", nativeQuery = true)
    Integer getCountByType(boolean type);

    @Query(value ="SELECT MAX(t.transaction_id) FROM transaction t", nativeQuery = true)
    Integer getLatestTransactionId();

    @Query(value = "SELECT nextval('Transaction_transactionId_seq')", nativeQuery = true)
    Integer getNextTransactionId();

}
