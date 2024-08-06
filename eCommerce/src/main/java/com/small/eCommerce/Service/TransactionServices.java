package com.small.eCommerce.Service;
import com.small.eCommerce.Exception.FoundException;
import com.small.eCommerce.Repository.HistoryRepo;
import com.small.eCommerce.Repository.TransactionRepo;
import com.small.eCommerce.Model.History;
import com.small.eCommerce.Model.Transaction;
import com.small.eCommerce.Model.TransactionCountWrapper;
import com.small.eCommerce.Model.TransactionWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class TransactionServices {

    @Autowired
    HistoryRepo HistoryRepo;
    @Autowired
    TransactionRepo TransactionRepo;

    //Finding Transaction by id and also list ordered items in that transaction
    public TransactionWrapper FindTransactionById(Integer id) {
        Transaction transaction = TransactionRepo.findById(id)
                .orElseThrow(() -> new FoundException("Transaction not found for ID: " + id));
        List<History> history= HistoryRepo.findAllByTransactionId(id);
        //Created a wrapper to hold both Transaction and List of Ordered items in the Transaction
        return new TransactionWrapper(transaction,history);
    }

    //Getting all transactions in transaction Wrapper List
    public List<TransactionWrapper> GetAllTransaction() {
        List<TransactionWrapper> ListOfTransactionWrapper = new ArrayList<>();
        TransactionRepo.findAll().forEach(transaction -> {
            TransactionWrapper tw=FindTransactionById(transaction.getTransactionId());
            ListOfTransactionWrapper.add(tw);
        });
        return ListOfTransactionWrapper;
    }

    //Getting total transaction count and also total purchase and sales order count
    public TransactionCountWrapper TransactionCount() {
        TransactionCountWrapper TransactionCountWrapper =new TransactionCountWrapper();
        TransactionCountWrapper.setTotalTransactionOfOrders(TransactionRepo.getCountByType(true));
        TransactionCountWrapper.setTotalTransactionOfPurchases(TransactionRepo.getCountByType(false));
        TransactionCountWrapper.setTotalTransactions(TransactionRepo.count());
        return TransactionCountWrapper;
    }

    //Deleting all Transactions
    public void DeleteTransaction() {
        TransactionRepo.deleteAll();
        HistoryRepo.deleteAll();
    }

}
