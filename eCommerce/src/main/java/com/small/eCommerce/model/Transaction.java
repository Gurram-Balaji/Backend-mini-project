package com.small.eCommerce.model;

import jakarta.persistence.*;
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
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "transaction_generator")
    @SequenceGenerator(name="transaction_generator", sequenceName = "transaction_transactionid_seq", allocationSize=1)
    private Integer transactionId;
    private Boolean transactionType;
    private Double transactionTotalCost;
    private Integer transactionTotalItems;
    private Date transactionDate;
}
