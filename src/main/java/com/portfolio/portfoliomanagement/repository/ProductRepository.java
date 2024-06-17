package com.portfolio.portfoliomanagement.repository;

import com.portfolio.portfoliomanagement.model.Option;
import com.portfolio.portfoliomanagement.model.Product;
import com.portfolio.portfoliomanagement.model.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {

    @Query("SELECT s FROM Stock s")
    List<Stock> findAllStocks();

    @Query("SELECT o FROM Option o")
    List<Option> findAllOptions();

    @Query("SELECT o FROM Option o WHERE o.underlyingStock.ticker = :ticker")
    List<Option> findAllOptionsByUnderlyingStockTicker(String ticker);

    @Query("SELECT s FROM Stock s WHERE s.ticker = :ticker")
    Stock findStockByTicker(String ticker);
}
