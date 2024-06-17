package com.portfolio.portfoliomanagement.events;

import com.portfolio.portfoliomanagement.model.Stock;
import org.springframework.context.ApplicationEvent;

public class StockPriceUpdateEvent extends ApplicationEvent {
    private final Stock stock;
    private final double previousPrice;
    private final double newPrice;

    public StockPriceUpdateEvent(Object source, Stock stock, double newPrice, double previousPrice) {
        super(source);
        this.stock = stock;
        this.previousPrice = previousPrice;
        this.newPrice = newPrice;
    }

    public Stock getStock() {
        return stock;
    }

    public double getPreviousPrice() {
        return previousPrice;
    }

    public double getNewPrice() {
        return newPrice;
    }
}
