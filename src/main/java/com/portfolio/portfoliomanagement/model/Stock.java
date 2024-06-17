package com.portfolio.portfoliomanagement.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("STOCK")
public class Stock extends Product {
    private double price;
    private double expectedReturn;
    private double volatility;

    // Getters and Setters
    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getExpectedReturn() {
        return expectedReturn;
    }

    public void setExpectedReturn(double expectedReturn) {
        this.expectedReturn = expectedReturn;
    }

    public double getVolatility() {
        return volatility;
    }

    public void setVolatility(double volatility) {
        this.volatility = volatility;
    }

    public double calculateValue(int quantity) {
        return price * quantity;
    }

    @Override
    public String toString() {
        return "Stock{" +
                "ticker='" + getTicker() + '\'' +
                ", price=" + price +
                ", expectedReturn=" + expectedReturn +
                ", volatility=" + volatility +
                '}';
    }
}
