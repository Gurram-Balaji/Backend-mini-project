package com.small.eCommerce.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer transactionId;
    private Boolean transactionType;
    private Double transactionTotalCost;
    private Integer transactionTotalItems;
    private Date transactionDate;

    public Transaction(Boolean transactionType,Date date) {
        this.transactionType=transactionType;
        this.transactionTotalCost=0.0;
        this.transactionTotalItems=0;
        this.transactionDate=date;
    }
}
