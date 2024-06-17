package com.portfolio.portfoliomanagement.model;

import javax.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "product_type", discriminatorType = DiscriminatorType.STRING)
public abstract class Product {
    @Id
    private String ticker;

    @Enumerated(EnumType.STRING)
    @Column(name = "product_type", insertable = false, updatable = false)
    private ProductType productType;

    // Getters and Setters
    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public ProductType getProductType() {
        return productType;
    }

    public void setProductType(ProductType productType) {
        this.productType = productType;
    }
}
