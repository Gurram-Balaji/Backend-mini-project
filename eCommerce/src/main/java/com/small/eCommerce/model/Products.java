package com.small.eCommerce.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Products {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer productsId;
    @NotBlank(message = "Product name is mandatory")
    @Size(min = 2, max = 20, message = "Product name must be between 2 and 20 characters")
    private String productsName;
    @NotNull(message = "Quantity is mandatory")
    @Min(value = 0, message = "Quantity must be a positive number")
    private Integer productsQTY;
    @NotNull(message = "Cost is mandatory")
    @Min(value = 0, message = "Cost must be a positive number.")
    private Double productsCost;

    public Products(String productsName, Integer productsQTY, Double productsCost) {
        this.productsName = productsName;
        this.productsQTY = productsQTY;
        this.productsCost = productsCost;
    }
}
