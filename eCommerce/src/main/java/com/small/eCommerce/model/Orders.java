package com.small.eCommerce.model;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Orders {

    private Integer id;
    private Integer qty;

}
