package com.small.eCommerce.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class History {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer historyId;
    private Integer historyTransactionId;
    private Integer historyProductId;
    private Integer historyProductQty;
    private Double historyProductCost;

    public History(Integer transactionId, Integer id, Integer qty, Double productsCost) {
        this.historyTransactionId=transactionId;
        this.historyProductId=id;
        this.historyProductQty=qty;
        this.historyProductCost=productsCost;
    }
}
