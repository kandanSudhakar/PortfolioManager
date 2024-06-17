package com.portfolio.portfoliomanagement.events;

import com.portfolio.portfoliomanagement.model.Portfolio;
import org.springframework.context.ApplicationEvent;

public class PortfolioUpdateEvent extends ApplicationEvent {
    private final Portfolio portfolio;
    private final double lastPortfolioValue;

    public PortfolioUpdateEvent(Object source, Portfolio portfolio, double lastPortfolioValue) {
        super(source);
        this.portfolio = portfolio;
        this.lastPortfolioValue = lastPortfolioValue;
    }

    public Portfolio getPortfolio() {
        return portfolio;
    }

    public double getLastPortfolioValue() {
        return lastPortfolioValue;
    }
}
