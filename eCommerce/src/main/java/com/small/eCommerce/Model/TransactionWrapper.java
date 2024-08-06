package com.small.eCommerce.Model;

import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class TransactionWrapper {
    private Transaction Transaction;
    @OneToMany
    private List<History> Orders;

    public TransactionWrapper(Transaction t, List<History> h) {
        this.Transaction=t;
        this.Orders=h;
    }
}
