package com.vini.transactions.controller;

import com.vini.transactions.model.Transaction;
import com.vini.transactions.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class TransactionControllerTest {

    private MockMvc mockMvc;

    @Mock
    private TransactionService transactionService;

    private TransactionController transactionController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        transactionController = new TransactionController(transactionService);
        mockMvc = MockMvcBuilders.standaloneSetup(transactionController).build();
    }

    @Test
    void getAllTransactions_success() throws Exception {
        when(transactionService.getAllTransactions()).thenReturn(Arrays.asList(
                new Transaction("Description 1", BigDecimal.valueOf(100)),
                new Transaction("Description 2", BigDecimal.valueOf(200))
        ));

        mockMvc.perform(get("/transactions"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void getTransactionById_found() throws Exception {
        Long id = 1L;
        Transaction transaction = new Transaction("Valid Description", BigDecimal.valueOf(100));
        when(transactionService.getTransactionById(id)).thenReturn(Optional.of(transaction));

        mockMvc.perform(get("/transactions/" + id))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void getTransactionById_notFound() throws Exception {
        Long id = 99L;
        when(transactionService.getTransactionById(id)).thenReturn(Optional.empty());

        mockMvc.perform(get("/transactions/" + id))
                .andExpect(status().isNotFound());
    }

    @Test
    void createTransaction_success() throws Exception {
        Transaction transaction = new Transaction("New Transaction", BigDecimal.valueOf(300));
        when(transactionService.saveTransaction(any(Transaction.class))).thenReturn(transaction);

        mockMvc.perform(post("/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"description\": \"New Transaction\", \"amount\": 300}"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }
}
