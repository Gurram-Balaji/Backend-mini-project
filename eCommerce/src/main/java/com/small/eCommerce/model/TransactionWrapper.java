package com.small.eCommerce.model;

import jakarta.persistence.OneToMany;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionWrapper {
    private Transaction Transaction;
    @OneToMany
    private List<History> Orders;
}
