package com.portfolio.portfoliomanagement.service;

import com.portfolio.portfoliomanagement.events.PortfolioUpdateEvent;
import com.portfolio.portfoliomanagement.events.StockPriceUpdateEvent;
import com.portfolio.portfoliomanagement.model.Portfolio;
import com.portfolio.portfoliomanagement.model.Stock;
import com.portfolio.portfoliomanagement.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class PortfolioManagementService {

    private Portfolio portfolio;
    private final ApplicationEventPublisher eventPublisher;
    private final ProductRepository productRepository;

    @Autowired
    public PortfolioManagementService(ApplicationEventPublisher eventPublisher, ProductRepository productRepository) {
        this.eventPublisher = eventPublisher;
        this.productRepository = productRepository;
    }

    public void setPortfolio(Portfolio portfolio) {
        this.portfolio = portfolio;
    }

    public Optional<Portfolio> getPortfolio() {
        return Optional.ofNullable(portfolio);
    }

    public void publishPortfolioUpdateEvent(Portfolio portfolio, double lastPortfolioValue) {
        eventPublisher.publishEvent(new PortfolioUpdateEvent(this, portfolio, lastPortfolioValue));
    }

    @Async
    @EventListener
    @Transactional
    public void handleStockPriceUpdate(StockPriceUpdateEvent event) {
        Stock updatedStock = event.getStock();
        double newPrice = event.getNewPrice();
        double previousPrice = event.getPreviousPrice();

        System.out.println(formatStockUpdate(updatedStock.getTicker(), previousPrice, newPrice));

        // Persist the updated stock price
        updatedStock.setPrice(newPrice);
        productRepository.save(updatedStock);

        // Update the portfolio's stock and option objects with the new prices
        if (portfolio != null) {
            portfolio.updateProductPrices(updatedStock, newPrice);
            double lastPortfolioValue = portfolio.getTotalValue();
            portfolio.updateTotalValue();
            publishPortfolioUpdateEvent(portfolio, lastPortfolioValue);
        }
    }

    private String formatStockUpdate(String ticker, double oldPrice, double newPrice) {
        String direction = newPrice > oldPrice ? "↑" : "↓";
        return String.format("%s %s change from %.4f to %.4f",
                ticker, direction, oldPrice, newPrice);
    }
}
