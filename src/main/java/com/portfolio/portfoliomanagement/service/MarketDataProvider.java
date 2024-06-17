package com.portfolio.portfoliomanagement.service;

import com.portfolio.portfoliomanagement.events.StockPriceUpdateEvent;
import com.portfolio.portfoliomanagement.model.ProductType;
import com.portfolio.portfoliomanagement.model.Stock;
import com.portfolio.portfoliomanagement.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
public class MarketDataProvider {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    private Random random;

    private Logger LOG = LoggerFactory.getLogger(MarketDataProvider.class);

    public MarketDataProvider(Random random) {
        this.random = random;
    }

    @Async
    @Scheduled(fixedDelay = 1000, initialDelay = 1000)
    public void updateValues() {
        List<Stock> stocks = productRepository.findAllStocks();

        for (Stock stock : stocks) {
            double oldPrice = stock.getPrice();
            double newPrice = simulatePriceUpdate(stock);
            stock.setPrice(newPrice);
            productRepository.save(stock);
            eventPublisher.publishEvent(new StockPriceUpdateEvent(this, stock, newPrice, oldPrice));
        }
    }

    double simulatePriceUpdate(Stock stock) {
        double S = stock.getPrice();
        double mu = stock.getExpectedReturn();
        double sigma = stock.getVolatility();
        double epsilon = random.nextGaussian();
        double deltaT = 1.0 / (365 * 24 * 60 * 60); // assuming updates are per second

        double deltaS = S * (mu * deltaT + sigma * epsilon * Math.sqrt(deltaT));
        return Math.max(0, S + deltaS);
    }
}
