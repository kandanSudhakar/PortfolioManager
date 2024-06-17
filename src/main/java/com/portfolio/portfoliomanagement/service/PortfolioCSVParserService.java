package com.portfolio.portfoliomanagement.service;

import com.portfolio.portfoliomanagement.model.Option;
import com.portfolio.portfoliomanagement.model.OptionType;
import com.portfolio.portfoliomanagement.model.Portfolio;
import com.portfolio.portfoliomanagement.model.ProductType;
import com.portfolio.portfoliomanagement.model.Stock;
import com.portfolio.portfoliomanagement.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class PortfolioCSVParserService {

    private final ProductRepository productRepository;
    private final PortfolioManagementService portfolioManagementService;
    private final Map<String, Stock> stockCache = new HashMap<>();

    @Autowired
    public PortfolioCSVParserService(ProductRepository productRepository, PortfolioManagementService portfolioManagementService) {
        this.productRepository = productRepository;
        this.portfolioManagementService = portfolioManagementService;
    }

    private static final Pattern OPTION_PATTERN = Pattern.compile("([A-Z]+)-([A-Z]+)-([0-9]{4})-([0-9]+)-([CP])");

    public void createAndRegisterPortfolioFromCSV(String filePath) {
        Portfolio portfolio = new Portfolio();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("symbol")) {
                    continue; // Skip header
                }
                String[] fields = line.split(",");
                String ticker = fields[0];
                int quantity = Integer.parseInt(fields[1]);

                Matcher matcher = OPTION_PATTERN.matcher(ticker);
                if (matcher.matches()) {
                    Option option = createOption(matcher);
                    portfolio.addProduct(option, quantity);
                } else {
                    Stock stock = stockCache.computeIfAbsent(ticker, this::createStock);
                    portfolio.addProduct(stock, quantity);
                }
            }
        } catch (Exception e) {
            System.err.println("Error parsing the input CSV file: " + e.getMessage());
        }

        portfolioManagementService.setPortfolio(portfolio);
    }

    private Option createOption(Matcher matcher) {
        String underlyingTicker = matcher.group(1);
        Stock underlyingStock = stockCache.computeIfAbsent(underlyingTicker, this::createStock);

        Option option = new Option();
        option.setTicker(matcher.group(0));
        option.setProductType(ProductType.OPTION);
        option.setUnderlyingStock(underlyingStock);
        DateTimeFormatter formatter = new DateTimeFormatterBuilder().parseCaseInsensitive().appendPattern("yyyy-MMM-dd").toFormatter();
        option.setMaturity(LocalDate.parse(matcher.group(3) + "-" + matcher.group(2) + "-01", formatter));
        option.setStrike(Double.parseDouble(matcher.group(4)));
        option.setOptionType(matcher.group(5).equals("C") ? OptionType.CALL : OptionType.PUT);

        productRepository.save(option);
        return option;
    }

    private Stock createStock(String ticker) {
        Stock stock = new Stock();
        stock.setTicker(ticker);
        stock.setProductType(ProductType.STOCK);
        stock.setPrice(100.0); // Default price, should be updated by market data provider
        stock.setExpectedReturn(0.05); // Default expected return
        stock.setVolatility(0.2); // Default volatility

        productRepository.save(stock);
        return stock;
    }
}
