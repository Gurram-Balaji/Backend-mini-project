package com.small.eCommerce.dao;

import com.small.eCommerce.model.History;

import java.util.List;

public interface HistoryDao {
    List<History> findAllTransactionByTransactionId(Integer id);
    void deleteAllTransaction();
    void saveHistory(History history);
}
