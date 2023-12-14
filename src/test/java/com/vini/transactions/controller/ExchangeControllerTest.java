package com.vini.transactions.controller;

import com.vini.transactions.DTO.ConvertedTransactionDto;
import com.vini.transactions.exceptions.ApiError;
import com.vini.transactions.service.ExchangeRateApiClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ExchangeControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ExchangeRateApiClient exchangeRateApiClient;

    private ExchangeController exchangeController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        exchangeController = new ExchangeController(exchangeRateApiClient);
        mockMvc = MockMvcBuilders.standaloneSetup(exchangeController).build();
    }

    @Test
    void getExchangeRate_success() throws Exception {
        String currency = "USD";
        Long id = 1L;
        ResponseEntity<Object> expectedResponse = ResponseEntity.ok(new ConvertedTransactionDto());
        when(exchangeRateApiClient.getExchangeRate(any(String.class), any(Long.class))).thenReturn(expectedResponse);

        mockMvc.perform(get("/exchange")
                        .param("currency", currency)
                        .param("id", id.toString()))
                .andExpect(status().isOk());
    }

    @Test
    void getExchangeRate_notFound() throws Exception {
        String currency = "USD";
        Long id = 99L;
        ApiError apiError = new ApiError(HttpStatus.NOT_FOUND, "Transaction with ID " + id + " not found.");
        ResponseEntity<Object> expectedResponse = new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);

        when(exchangeRateApiClient.getExchangeRate(currency, id)).thenReturn(expectedResponse);

        mockMvc.perform(get("/exchange")
                        .param("currency", currency)
                        .param("id", id.toString()))
                .andExpect(status().isNotFound());
    }
}