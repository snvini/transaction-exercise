package com.vini.transactions.DTO;

import lombok.Data;

import java.util.List;

@Data
public class ExchangeRateResponse {
    private List<ExchangeRateDto> data;
}