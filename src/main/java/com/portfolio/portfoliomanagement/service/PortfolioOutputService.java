package com.portfolio.portfoliomanagement.service;

import com.portfolio.portfoliomanagement.events.PortfolioUpdateEvent;
import com.portfolio.portfoliomanagement.model.Option;
import com.portfolio.portfoliomanagement.model.Portfolio;
import com.portfolio.portfoliomanagement.model.Stock;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class PortfolioOutputService {

    private static final int SYMBOL_WIDTH = 25;
    private static final int VALUE_WIDTH = 15;

    @Async
    @EventListener
    public void handlePortfolioUpdateEvent(PortfolioUpdateEvent event) {
        Portfolio portfolio = event.getPortfolio();
        double lastPortfolioValue = event.getLastPortfolioValue();
        System.out.println(formatPortfolioUpdate(portfolio, lastPortfolioValue));
    }

    private String formatPortfolioUpdate(Portfolio portfolio, double lastPortfolioValue) {
        StringBuilder output = new StringBuilder();
        double totalValue = portfolio.getTotalValue();
        String direction = totalValue > lastPortfolioValue ? "↑" : "↓";

        output.append("## Portfolio Update\n");
        output.append(String.format("Previous Value: %.2f\n", lastPortfolioValue));
        output.append(String.format("Total Value: %.2f %s\n", totalValue, direction));

        output.append(String.format("%-" + SYMBOL_WIDTH + "s %-" + VALUE_WIDTH + "s %-" + VALUE_WIDTH + "s %-" + VALUE_WIDTH + "s\n", "Symbol", "Price", "Qty", "Value"));
        output.append("Stocks:\n");
        for (Stock stock : portfolio.getStocks()) {
            int quantity = portfolio.getQuantities().get(stock);
            double currentPrice = stock.getPrice();
            double value = currentPrice * quantity;
            output.append(String.format("%-" + SYMBOL_WIDTH + "s %-" + VALUE_WIDTH + ".4f %-" + VALUE_WIDTH + "d %-" + VALUE_WIDTH + ".4f\n", stock.getTicker(), currentPrice, quantity, value));
        }
        output.append("Options:\n");
        for (Option option : portfolio.getOptions()) {
            int quantity = portfolio.getQuantities().get(option);
            double currentPrice = option.calculatePrice();
            double value = currentPrice * quantity;
            output.append(String.format("%-" + SYMBOL_WIDTH + "s %-" + VALUE_WIDTH + ".4f %-" + VALUE_WIDTH + "d %-" + VALUE_WIDTH + ".4f\n", option.getTicker(), currentPrice, quantity, value));
        }
        return output.toString();
    }
}
