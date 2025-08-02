package com.sunbeam.services;

import java.util.List;
import com.sunbeam.entities.Orders;
import com.sunbeam.entities.Seller;
import com.sunbeam.entities.Transaction;

public interface TransactionService {
    List<Transaction> getTransactionBySeller(Seller seller);
    List<Transaction> getAllTransactions();
    Transaction createTransaction(Orders order);
}