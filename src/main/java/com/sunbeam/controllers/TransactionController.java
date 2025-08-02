package com.sunbeam.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.sunbeam.entities.Seller;
import com.sunbeam.entities.Transaction;
import com.sunbeam.services.SellerService;
import com.sunbeam.services.TransactionService;
import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;
    private final SellerService sellerService;

    @Autowired
    public TransactionController(TransactionService transactionService, SellerService sellerService) {
        this.transactionService = transactionService;
        this.sellerService = sellerService;
    }

    @GetMapping("/seller")
    public ResponseEntity<List<Transaction>> getTransactionBySeller(@RequestHeader("Authorization") String jwt) throws Exception {
        Seller seller = sellerService.getSellerProfile(jwt);
        List<Transaction> transactions = transactionService.getTransactionBySeller(seller);
        return ResponseEntity.ok(transactions);
    }

    @GetMapping
    public ResponseEntity<List<Transaction>> getAllTransactions() {
        List<Transaction> transactions = transactionService.getAllTransactions();
        return ResponseEntity.ok(transactions);
    }
}