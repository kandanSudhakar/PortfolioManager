package com.portfolio.portfoliomanagement.model;

import javax.persistence.*;
import java.time.Clock;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.function.Function;

@Entity
@DiscriminatorValue("OPTION")
public class Option extends Product {
    private double strike;

    private LocalDate maturity;

    @Enumerated(EnumType.STRING)
    private OptionType optionType;

    @ManyToOne
    @JoinColumn(name = "underlying_stock_ticker", referencedColumnName = "ticker")
    private Stock underlyingStock;

    private static final double RISK_FREE_RATE = 0.02; // 2% per annum
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Transient
    private Clock clock = Clock.systemDefaultZone(); // Default to system clock

    // Getters and Setters
    public double getStrike() {
        return strike;
    }

    public void setStrike(double strike) {
        this.strike = strike;
    }

    public LocalDate getMaturity() {
        return maturity;
    }

    public void setMaturity(LocalDate maturity) {
        this.maturity = maturity;
    }

    public OptionType getOptionType() {
        return optionType;
    }

    public void setOptionType(OptionType optionType) {
        this.optionType = optionType;
    }

    public Stock getUnderlyingStock() {
        return underlyingStock;
    }

    public void setUnderlyingStock(Stock underlyingStock) {
        this.underlyingStock = underlyingStock;
    }

    void setClock(Clock clock) {
        this.clock = clock;
    }

    @Override
    public String toString() {
        return "Option{" +
                "ticker='" + getTicker() + '\'' +
                ", strike=" + strike +
                ", maturity=" + maturity +
                ", optionType=" + optionType +
                ", underlyingStock=" + (underlyingStock != null ? underlyingStock.getTicker() : "null") +
                '}';
    }

    public double calculatePrice() {
        double S = underlyingStock.getPrice();
        double K = strike;
        double sigma = underlyingStock.getVolatility();
        double T = (double) LocalDate.now(clock).until(maturity, ChronoUnit.DAYS) / 365.0;

        double d1 = (Math.log(S / K) + (RISK_FREE_RATE + 0.5 * Math.pow(sigma, 2)) * T) / (sigma * Math.sqrt(T));
        double d2 = d1 - sigma * Math.sqrt(T);

        Function<Double, Double> N = this::cdf;

        if (optionType == OptionType.CALL) {
            return S * N.apply(d1) - K * Math.exp(-RISK_FREE_RATE * T) * N.apply(d2);
        } else {
            return K * Math.exp(-RISK_FREE_RATE * T) * N.apply(-d2) - S * N.apply(-d1);
        }
    }

    public double calculateValue(int quantity) {
        double optionPrice = calculatePrice();
        return optionPrice * quantity;
    }

    private double cdf(double x) {
        return (1.0 + erf(x / Math.sqrt(2.0))) / 2.0;
    }

    // Custom implementation of the error function (erf)
    private double erf(double x) {
        // save the sign of x
        int sign = (x >= 0) ? 1 : -1;
        x = Math.abs(x);

        // constants
        double a1 =  0.254829592;
        double a2 = -0.284496736;
        double a3 =  1.421413741;
        double a4 = -1.453152027;
        double a5 =  1.061405429;
        double p  =  0.3275911;

        // A&S formula 7.1.26
        double t = 1.0 / (1.0 + p * x);
        double y = 1.0 - (((((a5 * t + a4) * t) + a3) * t + a2) * t + a1) * t * Math.exp(-x * x);

        return sign * y;
    }
}
