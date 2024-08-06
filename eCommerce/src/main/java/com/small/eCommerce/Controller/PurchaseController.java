package com.small.eCommerce.Controller;

import com.small.eCommerce.Model.Orders;
import com.small.eCommerce.ResponseHandler.ApiResponse;
import com.small.eCommerce.Service.PurchaseServices;
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
public class PurchaseController {

    @Autowired
    PurchaseServices PurchaseServices;

    //Updates the qty to present_qty of products using products_id, pass list of ids and qty
    @PostMapping("/AddPurchase")
    public ResponseEntity<ApiResponse<List<Orders>>> AddPurchase(@RequestBody List<Orders> addPurchases)
    {
        ApiResponse<List<Orders>> response;
        Optional<List<Orders>> Payload= Optional.ofNullable(PurchaseServices.AddPurchase(addPurchases));
        response = Payload
                .filter(eachPayload -> !eachPayload.isEmpty())
                .map(eachPayload -> new ApiResponse<>(true, "Purchase Placed.", eachPayload))
                .orElseGet(() -> new ApiResponse<>(false, "Purchase couldn't be placed.", new ArrayList<>()));
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
