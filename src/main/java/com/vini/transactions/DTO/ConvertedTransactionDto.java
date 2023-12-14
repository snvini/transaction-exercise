package com.vini.transactions.DTO;

import com.vini.transactions.model.Transaction;
import lombok.Data;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

@Data
public class ConvertedTransactionDto {
    private String description;
    private LocalDate transactionDate;
    private BigDecimal usdAmount;
    private BigDecimal exchangeRate;
    private BigDecimal convertedAmount;

    public ConvertedTransactionDto(Transaction transaction, ExchangeRateDto exchangeRateDto){
        this.description = transaction.getDescription();
        this.transactionDate = transaction.getTransactionDate();
        this.usdAmount = transaction.getAmount();
        this.exchangeRate = exchangeRateDto.getExchange_rate();
        this.convertedAmount = usdAmount.multiply(exchangeRate).setScale(2, RoundingMode.HALF_UP);
    }

    public ConvertedTransactionDto(){}
}
