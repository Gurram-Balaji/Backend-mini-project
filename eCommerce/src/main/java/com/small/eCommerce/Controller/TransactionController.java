package com.small.eCommerce.Controller;

import com.small.eCommerce.Model.TransactionCountWrapper;
import com.small.eCommerce.Model.TransactionWrapper;
import com.small.eCommerce.ResponseHandler.ApiResponse;
import com.small.eCommerce.Service.TransactionServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class TransactionController {

    @Autowired
    TransactionServices transactionServices;

    //Get Transaction by TransactionId
    @GetMapping("/Transaction/{id}")
    public ResponseEntity<ApiResponse<TransactionWrapper>> FindTransactionById(@PathVariable Integer id){
        ApiResponse<TransactionWrapper> response;
        Optional<TransactionWrapper> Payload= Optional.ofNullable(transactionServices.FindTransactionById(id));
        response = Payload
                .map(eachPayload -> new ApiResponse<>(true, "Transaction id: "+id+" found.", eachPayload))
                .orElseGet(() -> new ApiResponse<>(false, "Transaction id: "+id+" not found.", new TransactionWrapper()));
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    //Get All Transaction by TransactionId
    @GetMapping("/AllTransaction")
    public ResponseEntity<ApiResponse<List<TransactionWrapper>>> GetAllTransaction(){
        ApiResponse<List<TransactionWrapper>> response;
        Optional<List<TransactionWrapper>> Payload= Optional.ofNullable(transactionServices.GetAllTransaction());
        response = Payload
                .filter(eachPayload -> !eachPayload.isEmpty())
                .map(eachPayload -> new ApiResponse<>(true, "Transaction Found.", eachPayload))
                .orElseGet(() -> new ApiResponse<>(false, "Transaction couldn't be found.", new ArrayList<>()));
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/TransactionCount")
    public ResponseEntity<ApiResponse<TransactionCountWrapper>> GetCount(){
        ApiResponse<TransactionCountWrapper> response;
        Optional<TransactionCountWrapper> Payload= Optional.ofNullable(transactionServices.TransactionCount());
        response = Payload
                .map(eachPayload -> new ApiResponse<>(true, "Transactions count.", eachPayload))
                .orElseGet(() -> new ApiResponse<>(false, "Transaction not found.", new TransactionCountWrapper()));
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/DeleteTransaction")
    public ResponseEntity<ApiResponse<String>> DeleteTransaction(){
        transactionServices.DeleteTransaction();
        return ResponseEntity.status(HttpStatus.OK).body( new ApiResponse<>(true, "Transactions Found.", "Transactions Deleted."));

    }
}
