package com.vini.transactions.service;

import com.vini.transactions.DTO.ConvertedTransactionDto;
import com.vini.transactions.DTO.ExchangeRateDto;
import com.vini.transactions.DTO.ExchangeRateResponse;
import com.vini.transactions.exceptions.ApiError;
import com.vini.transactions.model.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class ExchangeRateApiClient {

    private final RestTemplate restTemplate;
    private final TransactionService transactionService;

    private final String urlApi = "https://api.fiscaldata.treasury.gov/services/api/fiscal_service/v1/accounting/od/rates_of_exchange";

    @Autowired
    public ExchangeRateApiClient(RestTemplate restTemplate, TransactionService transactionService) {
        this.restTemplate = restTemplate;
        this.transactionService = transactionService;
    }

    public ResponseEntity<Object> getExchangeRate(String currency, Long id) {
        Optional<Transaction> transaction = findTransaction(id);
        if (transaction.isEmpty()) {
            ApiError apiError = new ApiError(HttpStatus.NOT_FOUND, "Transaction with ID " + id + " not found.");
            return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);
        }

        // Define the parameters for the API request
        String filter = buildFilters(currency, transaction.get().getTransactionDate());

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(urlApi)
                .queryParam("filter", filter)
                .queryParam("sort", "-record_date");

        // Make a GET request to the API
        ResponseEntity<ExchangeRateResponse> response = restTemplate.getForEntity(uriBuilder.build().encode().toUri(), ExchangeRateResponse.class);

        if (!response.getStatusCode().is2xxSuccessful() && response.getBody() == null) {
            ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, "Could not connect to exchange API");
            return new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if (response.getBody().getData().isEmpty()) {
            ApiError apiError = new ApiError(HttpStatus.NOT_FOUND, "Currency data for " + currency + " not found on the specified date.");
            return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);
        }

        //getting 0 because its already getting sorted by record_date
        ExchangeRateDto exchangeRateDto = response.getBody().getData().get(0);

        // Return the exchange rate
        return ResponseEntity.ok(new ConvertedTransactionDto(transaction.get(), exchangeRateDto));
    }

    Optional<Transaction> findTransaction (Long id){
        return transactionService.getTransactionById(id);
    }

    String buildFilters(String currency, LocalDate date){
        LocalDate pastMonths = date.minusMonths(6);
        String capitalizedCurrency = StringUtils.capitalize(currency);
        return "record_date:gte:"+pastMonths.toString()
                +",record_date:lte:"+date.toString()
                +",currency:eq:"+capitalizedCurrency
                +",sort=-record_date";
    }

}
