package com.vini.transactions.service;

import com.vini.transactions.model.Transaction;
import com.vini.transactions.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;

    @Autowired
    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
        System.out.println(transactionRepository);
    }

    public List<Transaction> getAllTransactions() {
        System.out.println(transactionRepository);
        return transactionRepository.findAll();
    }

    public Optional<Transaction> getTransactionById(Long id) {
        return transactionRepository.findById(id);
    }

    public Transaction saveTransaction(Transaction transaction) {
        if(transaction == null){
            throw new IllegalArgumentException("transaction cannot be null");
        }
        return transactionRepository.save(transaction);
    }

}
