package com.vini.transactions.service;

import com.vini.transactions.DTO.ConvertedTransactionDto;
import com.vini.transactions.DTO.ExchangeRateDto;
import com.vini.transactions.DTO.ExchangeRateResponse;
import com.vini.transactions.exceptions.ApiError;
import com.vini.transactions.model.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ExchangeRateApiClientTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private TransactionService transactionService;

    private ExchangeRateApiClient exchangeRateApiClient;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        exchangeRateApiClient = new ExchangeRateApiClient(restTemplate, transactionService);
    }

    @Test
    void getExchangeRate_success() {
        Long transactionId = 1L;
        String currency = "USD";
        LocalDate transactionDate = LocalDate.now();
        Transaction mockTransaction = new Transaction("Test Transaction", BigDecimal.valueOf(100));
        mockTransaction.setTransactionDate(transactionDate);

        ExchangeRateDto exchangeRateDto = new ExchangeRateDto();
        exchangeRateDto.setCurrency(currency);
        exchangeRateDto.setRecord_date(transactionDate);
        exchangeRateDto.setExchange_rate(BigDecimal.valueOf(1.5));

        ExchangeRateResponse exchangeRateResponse = new ExchangeRateResponse();
        exchangeRateResponse.setData(Collections.singletonList(exchangeRateDto));

        when(transactionService.getTransactionById(transactionId)).thenReturn(Optional.of(mockTransaction));
        when(restTemplate.getForEntity(any(), eq(ExchangeRateResponse.class))).thenReturn(new ResponseEntity<>(exchangeRateResponse, HttpStatus.OK));

        ResponseEntity<Object> response = exchangeRateApiClient.getExchangeRate(currency, transactionId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof ConvertedTransactionDto);
        ConvertedTransactionDto convertedDto = (ConvertedTransactionDto) response.getBody();
        assertEquals(BigDecimal.valueOf(150.00).setScale(2), convertedDto.getConvertedAmount());
    }

    @Test
    void getExchangeRate_transactionNotFound() {
        Long transactionId = 1L;
        when(transactionService.getTransactionById(transactionId)).thenReturn(Optional.empty());

        ResponseEntity<Object> response = exchangeRateApiClient.getExchangeRate("USD", transactionId);

        assertEquals(404, response.getStatusCodeValue());
        verify(transactionService).getTransactionById(transactionId);
    }

    @Test
    void getExchangeRate_apiError() {
        Long transactionId = 1L;
        String currency = "USD";
        LocalDate transactionDate = LocalDate.now();
        Transaction mockTransaction = new Transaction("Test Transaction", BigDecimal.valueOf(100));
        mockTransaction.setTransactionDate(transactionDate);

        when(transactionService.getTransactionById(transactionId)).thenReturn(Optional.of(mockTransaction));
        when(restTemplate.getForEntity(any(), eq(ExchangeRateResponse.class))).thenReturn(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));

        ResponseEntity<Object> response = exchangeRateApiClient.getExchangeRate(currency, transactionId);

        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof ApiError);
        ApiError apiError = (ApiError) response.getBody();
        assertEquals("Could not connect to exchange API", apiError.getMessage());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, apiError.getStatus());
    }

}
