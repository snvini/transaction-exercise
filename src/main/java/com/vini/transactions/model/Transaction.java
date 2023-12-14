package com.vini.transactions.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;


import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Entity
@NoArgsConstructor
public class Transaction {
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String description;
    private LocalDate transactionDate;
    private BigDecimal amount;


    public Transaction(String description, BigDecimal amount) {
        setDescription(description);
        setAmount(amount);
    }

    public void setDescription(String description) {
        if (description == null || description.isEmpty() || description.length() > 50) {
            throw new IllegalArgumentException("Description must not be null or empty and must be less than or equal to 50 characters");
        }
        this.description = description;
    }

    public void setAmount(BigDecimal amount) {
        if (amount.signum() < 0) {
            throw new IllegalArgumentException("Amount must be a positive value");
        }
        this.amount = amount;
    }
}