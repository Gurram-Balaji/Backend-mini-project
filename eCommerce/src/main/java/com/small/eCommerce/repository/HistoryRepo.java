package com.small.eCommerce.repository;

import com.small.eCommerce.model.History;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface HistoryRepo extends JpaRepository<History,Integer> {
    @Query(value = "SELECT * FROM history h Where h.history_transaction_id=:id", nativeQuery = true)
    List<History> findAllByTransactionId(Integer id);



}
