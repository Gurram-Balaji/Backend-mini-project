package com.small.eCommerce.daoImp;

import com.small.eCommerce.dao.HistoryDao;
import com.small.eCommerce.model.History;
import com.small.eCommerce.repository.HistoryRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HistoryDaoImp implements HistoryDao {

    @Autowired
    HistoryRepo HistoryRepo;

    @Override
    public List<History> findAllTransactionByTransactionId(Integer id) {
        return HistoryRepo.findAllByTransactionId(id);
    }

    @Override
    public void deleteAllTransaction() {
        HistoryRepo.deleteAll();
    }

    @Override
    public void saveHistory(History history) {
        HistoryRepo.save(history);
    }
}
