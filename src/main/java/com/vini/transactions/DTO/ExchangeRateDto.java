package com.vini.transactions.DTO;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class ExchangeRateDto {
    private String currency;
    private LocalDate record_date;
    private BigDecimal exchange_rate;
}
