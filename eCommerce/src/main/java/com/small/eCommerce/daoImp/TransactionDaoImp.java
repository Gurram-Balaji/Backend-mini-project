package com.small.eCommerce.daoImp;

import com.small.eCommerce.dao.TransactionDao;
import com.small.eCommerce.model.Transaction;
import com.small.eCommerce.repository.TransactionRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TransactionDaoImp implements TransactionDao {
    @Autowired
    TransactionRepo TransactionRepo;

    @Override
    public Optional<Transaction> findTransactionById(Integer id) {
        return TransactionRepo.findById(id);
    }

    @Override
    public List<Transaction> findAllTransactions() {
        return TransactionRepo.findAll();
    }

    @Override
    public void deleteAllTransaction() {
        TransactionRepo.deleteAll();
    }

    @Override
    public Integer getAllOrdersCount() {
        return TransactionRepo.getCountByType(true);
    }

    @Override
    public Integer getAllPurchasesCount() {
        return TransactionRepo.getCountByType(false);
    }

    @Override
    public long getAllTransactionCount() {
        return TransactionRepo.count();
    }

    @Override
    public void saveTransaction(Transaction transactionObject) {
        TransactionRepo.save(transactionObject);
    }

    @Override
    public Integer getNextTransactionId() {
        return TransactionRepo.getNextTransactionId();
    }
}
