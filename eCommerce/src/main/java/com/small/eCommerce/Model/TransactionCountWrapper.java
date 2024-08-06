package com.small.eCommerce.Model;

import lombok.Data;

@Data
public class TransactionCountWrapper {
    private Long TotalTransactions;
    private Integer TotalTransactionOfPurchases;
    private Integer TotalTransactionOfOrders;
}
