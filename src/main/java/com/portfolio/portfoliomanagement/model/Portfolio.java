package com.portfolio.portfoliomanagement.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Portfolio {
    private List<Stock> stocks;
    private List<Option> options;
    private double totalValue;
    private Map<Product, Integer> quantities;

    public Portfolio() {
        this.stocks = new ArrayList<>();
        this.options = new ArrayList<>();
        this.quantities = new HashMap<>();
    }

    public List<Stock> getStocks() {
        return stocks;
    }

    public List<Option> getOptions() {
        return options;
    }

    public double getTotalValue() {
        return totalValue;
    }

    public void setTotalValue(double totalValue) {
        this.totalValue = totalValue;
    }

    public Map<Product, Integer> getQuantities() {
        return quantities;
    }

    public void setQuantities(Map<Product, Integer> quantities) {
        this.quantities = quantities;
    }

    public void addProduct(Product product, int quantity) {
        quantities.put(product, quantity);
        if (product instanceof Stock) {
            stocks.add((Stock) product);
        } else if (product instanceof Option) {
            options.add((Option) product);
        }
        updateTotalValue();
    }

    public void updateTotalValue() {
        double totalValue = 0.0;

        for (Stock stock : stocks) {
            int quantity = quantities.getOrDefault(stock, 0);
            totalValue += stock.getPrice() * quantity;
        }

        for (Option option : options) {
            int quantity = quantities.getOrDefault(option, 0);
            totalValue += option.calculateValue(quantity);
        }

        setTotalValue(totalValue);
    }

    public void updateProductPrices(Stock updatedStock, double newPrice) {
        for (Stock stock : stocks) {
            if (stock.getTicker().equals(updatedStock.getTicker())) {
                stock.setPrice(newPrice);
            }
        }

        for (Option option : options) {
            if (option.getUnderlyingStock().getTicker().equals(updatedStock.getTicker())) {
                option.getUnderlyingStock().setPrice(newPrice);
            }
        }
    }
}
