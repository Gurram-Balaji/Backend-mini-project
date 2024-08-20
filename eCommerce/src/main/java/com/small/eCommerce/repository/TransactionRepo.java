package com.small.eCommerce.repository;

import com.small.eCommerce.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepo extends JpaRepository<Transaction,Integer> {

    @Query(value = "SELECT COUNT(*) FROM transaction t Where t.transaction_type=:type", nativeQuery = true)
    Integer getCountByType(boolean type);

    @Query(value ="SELECT MAX(t.transaction_id) FROM transaction t", nativeQuery = true)
    Integer getLatestTransactionId();
}
