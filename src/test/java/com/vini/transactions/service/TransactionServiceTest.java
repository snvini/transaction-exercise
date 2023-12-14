package com.vini.transactions.service;

import com.vini.transactions.model.Transaction;
import com.vini.transactions.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    private TransactionService transactionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        transactionService = new TransactionService(transactionRepository);
    }

    @Test
    void getAllTransactions_success() {
        Transaction transaction1 = new Transaction("Description 1", BigDecimal.valueOf(100));
        Transaction transaction2 = new Transaction("Description 2", BigDecimal.valueOf(200));
        when(transactionRepository.findAll()).thenReturn(Arrays.asList(transaction1, transaction2));

        List<Transaction> transactions = transactionService.getAllTransactions();

        assertNotNull(transactions);
        assertEquals(2, transactions.size());
        verify(transactionRepository).findAll();
    }

    @Test
    void getTransactionById_found() {
        Long transactionId = 1L;
        Transaction transaction = new Transaction("Description", BigDecimal.valueOf(100));
        when(transactionRepository.findById(transactionId)).thenReturn(Optional.of(transaction));

        Optional<Transaction> foundTransaction = transactionService.getTransactionById(transactionId);

        assertTrue(foundTransaction.isPresent());
        assertEquals(transaction, foundTransaction.get());
        verify(transactionRepository).findById(transactionId);
    }

    @Test
    void saveTransaction_success() {
        Transaction transaction = new Transaction("Description", BigDecimal.valueOf(100));
        when(transactionRepository.save(transaction)).thenReturn(transaction);

        Transaction savedTransaction = transactionService.saveTransaction(transaction);

        assertNotNull(savedTransaction);
        assertEquals(transaction, savedTransaction);
        verify(transactionRepository).save(transaction);
    }

    @Test
    void getTransactionById_notFound() {
        Long transactionId = 99L;
        when(transactionRepository.findById(transactionId)).thenReturn(Optional.empty());

        Optional<Transaction> foundTransaction = transactionService.getTransactionById(transactionId);

        assertTrue(foundTransaction.isEmpty());
        verify(transactionRepository).findById(transactionId);
    }

    @Test
    void saveTransaction_nullTransaction() {
        Transaction transaction = null;
        assertThrows(IllegalArgumentException.class, () -> {
            transactionService.saveTransaction(transaction);
        });
        verify(transactionRepository, never()).save(any(Transaction.class));
    }

    @Test
    void getAllTransactions_empty() {
        when(transactionRepository.findAll()).thenReturn(Collections.emptyList());

        List<Transaction> transactions = transactionService.getAllTransactions();

        assertTrue(transactions.isEmpty());
        verify(transactionRepository).findAll();
    }
}
