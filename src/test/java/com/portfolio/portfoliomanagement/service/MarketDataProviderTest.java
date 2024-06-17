package com.portfolio.portfoliomanagement.service;

import com.portfolio.portfoliomanagement.events.StockPriceUpdateEvent;
import com.portfolio.portfoliomanagement.model.Stock;
import com.portfolio.portfoliomanagement.model.ProductType;
import com.portfolio.portfoliomanagement.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class MarketDataProviderTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @InjectMocks
    private MarketDataProvider marketDataProvider;

    @BeforeEach
    void setUp() {
        Random random = new Random(42); // Seed the randomness
        marketDataProvider = new MarketDataProvider(random);
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testUpdateValues() throws Exception {
        // Arrange
        Stock stock1 = new Stock();
        stock1.setTicker("AAPL");
        stock1.setPrice(150.0);
        stock1.setExpectedReturn(0.05);
        stock1.setVolatility(0.2);
        stock1.setProductType(ProductType.STOCK);

        Stock stock2 = new Stock();
        stock2.setTicker("GOOGL");
        stock2.setPrice(1000.0);
        stock2.setExpectedReturn(0.04);
        stock2.setVolatility(0.25);
        stock2.setProductType(ProductType.STOCK);

        List<Stock> stocks = Arrays.asList(stock1, stock2);

        when(productRepository.findAllStocks()).thenReturn(stocks);

        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.initialize();
        taskExecutor.submit(() -> marketDataProvider.updateValues()).get();

        verify(productRepository, times(1)).findAllStocks();
        verify(productRepository, times(1)).save(stock1);
        verify(productRepository, times(1)).save(stock2);
        verify(eventPublisher, times(2)).publishEvent(any(StockPriceUpdateEvent.class));

        // Calculate expected new prices
        double expectedNewPrice1 = 150.0061004936136;
        double expectedNewPrice2 = 1000.0409315713707;

        // Ensure stock prices have been updated
        assertEquals(expectedNewPrice1, stock1.getPrice(), 0.0000001);
        assertEquals(expectedNewPrice2, stock2.getPrice(), 0.0000001);
    }

    @Test
    void testSimulatePriceUpdate() {
        // Arrange
        Stock stock = new Stock();
        stock.setTicker("AAPL");
        stock.setPrice(150.0);
        stock.setExpectedReturn(0.05);
        stock.setVolatility(0.2);

        // Act
        double newPrice = marketDataProvider.simulatePriceUpdate(stock);

        // Assert
        double expectedNewPrice = 150.006100493613; // Expected new price with seeded randomness
        assertEquals(expectedNewPrice, newPrice, 0.0000001);
    }
}
