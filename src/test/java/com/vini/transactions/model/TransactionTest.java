package com.vini.transactions.model;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class TransactionTest {

    @Test
    void setAmount_valid() {
        Transaction transaction = new Transaction();
        BigDecimal validAmount = BigDecimal.valueOf(100);
        assertDoesNotThrow(() -> transaction.setAmount(validAmount));
        assertEquals(validAmount, transaction.getAmount());
    }

    @Test
    void setAmount_negative() {
        Transaction transaction = new Transaction();
        BigDecimal negativeAmount = BigDecimal.valueOf(-100);
        assertThrows(IllegalArgumentException.class, () -> transaction.setAmount(negativeAmount));
    }

    @Test
    void constructor_withArgs_valid() {
        String description = "Valid Description";
        BigDecimal amount = BigDecimal.valueOf(100);

        Transaction transaction = new Transaction(description, amount);

        assertEquals(description, transaction.getDescription());
        assertEquals(amount, transaction.getAmount());
    }

    @Test
    void constructor_withArgs_invalidDescription() {
        String invalidDescription = "";
        BigDecimal amount = BigDecimal.valueOf(100);

        assertThrows(IllegalArgumentException.class, () -> new Transaction(invalidDescription, amount));
    }

    @Test
    void constructor_withArgs_invalidAmount() {
        String description = "Valid Description";
        BigDecimal invalidAmount = BigDecimal.valueOf(-100);

        assertThrows(IllegalArgumentException.class, () -> new Transaction(description, invalidAmount));
    }
}
