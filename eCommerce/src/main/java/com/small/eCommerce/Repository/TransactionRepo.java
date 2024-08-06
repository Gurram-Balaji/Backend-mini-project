package com.small.eCommerce.Repository;

import com.small.eCommerce.Model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepo extends JpaRepository<Transaction,Integer> {

    @Query(value = "SELECT COUNT(*) FROM transaction t Where t.transaction_type=:type", nativeQuery = true)
    Integer getCountByType(boolean type);
}
