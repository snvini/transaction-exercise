package com.vini.transactions.controller;

import com.vini.transactions.service.ExchangeRateApiClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/exchange")
public class ExchangeController {

    private final ExchangeRateApiClient exchangeRateApiClient;

    @Autowired
    public ExchangeController(ExchangeRateApiClient exchangeRateApiClient) {
        this.exchangeRateApiClient = exchangeRateApiClient;
    }

    @GetMapping
    public ResponseEntity<?> get(@RequestParam(value = "currency",  required = true) String currency,
                                 @RequestParam(value = "id",  required = true) Long id) {
        return exchangeRateApiClient.getExchangeRate(currency,id);
    }
}