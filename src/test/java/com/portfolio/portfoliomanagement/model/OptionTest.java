package com.portfolio.portfoliomanagement.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class OptionTest {

    private Option callOption;
    private Option putOption;
    private Stock underlyingStock;
    private Clock fixedClock;

    @BeforeEach
    public void setUp() {
        // Fix the current date to 2023-01-01
        fixedClock = Clock.fixed(Instant.parse("2023-01-01T00:00:00Z"), ZoneId.of("UTC"));

        underlyingStock = new Stock();
        underlyingStock.setTicker("AAPL");
        underlyingStock.setPrice(150.00);
        underlyingStock.setExpectedReturn(0.05);
        underlyingStock.setVolatility(0.2);

        callOption = new Option();
        callOption.setTicker("AAPL-OCT-2025-110-C");
        callOption.setStrike(110.00);
        callOption.setMaturity(LocalDate.of(2025, 10, 1));
        callOption.setOptionType(OptionType.CALL);
        callOption.setUnderlyingStock(underlyingStock);
        callOption.setClock(fixedClock);

        putOption = new Option();
        putOption.setTicker("AAPL-OCT-2025-110-P");
        putOption.setStrike(110.00);
        putOption.setMaturity(LocalDate.of(2025, 10, 1));
        putOption.setOptionType(OptionType.PUT);
        putOption.setUnderlyingStock(underlyingStock);
        putOption.setClock(fixedClock);
    }

    @Test
    public void testCalculateCallOptionPrice() {
        double expectedCallPrice = 48.70102; // Expected price using options price calculator
        double actualCallPrice = callOption.calculatePrice();
        assertEquals(expectedCallPrice, actualCallPrice, 0.01);
    }

    @Test
    public void testCalculatePutOptionPrice() {
        double expectedPutPrice = 2.81311;
        double actualPutPrice = putOption.calculatePrice();
        assertEquals(expectedPutPrice, actualPutPrice, 0.01);
    }

    @Test
    public void testCalculateCallOptionValue() {
        int quantity = 100;
        double expectedCallValue = 48.70102 * quantity;
        double actualCallValue = callOption.calculateValue(quantity);
        assertEquals(expectedCallValue, actualCallValue, 1);
    }

    @Test
    public void testCalculatePutOptionValue() {
        int quantity = 100;
        double expectedPutValue = 2.81311 * quantity;
        double actualPutValue = putOption.calculateValue(quantity);
        assertEquals(expectedPutValue, actualPutValue, 1);
    }
}
