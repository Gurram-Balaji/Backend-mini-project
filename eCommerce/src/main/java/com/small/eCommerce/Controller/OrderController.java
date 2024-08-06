package com.small.eCommerce.Controller;

import com.small.eCommerce.Model.Orders;
import com.small.eCommerce.ResponseHandler.ApiResponse;
import com.small.eCommerce.Service.OrderServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class OrderController {

    @Autowired
    OrderServices OrderServices;

    @PostMapping("/SalesOrder")
    public ResponseEntity<ApiResponse<List<Orders>>> SalesOrder(@RequestBody List<Orders> saleOrders) {
        ApiResponse<List<Orders>> response;
        Optional<List<Orders>> Payload= Optional.ofNullable(OrderServices.SalesOrder(saleOrders));
        response = Payload
                .filter(eachPayload -> !eachPayload.isEmpty())
                .map(eachPayload -> new ApiResponse<>(true, "Ordered Placed.", eachPayload))
                .orElseGet(() -> new ApiResponse<>(false, "Order couldn't be placed.", new ArrayList<>()));
        return ResponseEntity.status(HttpStatus.OK).body(response);

    }
}
