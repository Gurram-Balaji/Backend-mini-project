package com.small.eCommerce.Model;

import lombok.Data;

@Data
public class WrapperToInventory {
    private String productsName;
    private Integer productsQTY;

    public WrapperToInventory(String productsName, Integer productsQTY) {
        this.productsName=productsName;
        this.productsQTY=productsQTY;
    }
}
