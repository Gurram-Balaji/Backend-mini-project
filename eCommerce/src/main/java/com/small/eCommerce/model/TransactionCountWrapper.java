package com.small.eCommerce.model;

import lombok.Data;

@Data
public class TransactionCountWrapper {
    private Long TotalTransactions;
    private Integer TotalTransactionOfPurchases;
    private Integer TotalTransactionOfOrders;
}
