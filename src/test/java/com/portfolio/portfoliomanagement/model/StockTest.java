package com.portfolio.portfoliomanagement.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class StockTest {

    private Stock stock;

    @BeforeEach
    public void setUp() {
        stock = new Stock();
        stock.setTicker("AAPL");
        stock.setPrice(150.00);
        stock.setExpectedReturn(0.05);
        stock.setVolatility(0.2);
    }

    @Test
    public void testCalculateValue() {
        int quantity = 100;
        double expectedValue = 150.00 * 100;
        assertEquals(expectedValue, stock.calculateValue(quantity), 0.001);
    }
}
