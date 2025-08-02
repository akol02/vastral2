package com.sunbeam.services.impl;

import com.sunbeam.daos.SellerRepository;
import com.sunbeam.daos.TransactionRepository;
import com.sunbeam.entities.Orders;
import com.sunbeam.entities.Seller;
import com.sunbeam.entities.Transaction;
import com.sunbeam.services.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final SellerRepository sellerRepository;

    @Override
    public Transaction createTransaction(Orders order) {
        Seller seller = sellerRepository.findById(order.getSellerId()).orElse(null);
        if (seller == null) {
            // This case should ideally not happen if data integrity is maintained
            return null;
        }
        Transaction transaction = new Transaction();
        transaction.setCustomer(order.getUser());
        transaction.setOrder(order);
        transaction.setSeller(seller);
        return transactionRepository.save(transaction);
    }

    @Override
    public List<Transaction> getTransactionBySeller(Seller seller) {
        return transactionRepository.findBySellerId(seller.getId());
    }

    @Override
    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }
}