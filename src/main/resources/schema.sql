DROP TABLE IF EXISTS option;
DROP TABLE IF EXISTS stock;
DROP TABLE IF EXISTS product;

CREATE TABLE product (
    ticker VARCHAR(50) NOT NULL PRIMARY KEY,
    product_type VARCHAR(31) NOT NULL
);

CREATE TABLE stock (
    ticker VARCHAR(50) NOT NULL PRIMARY KEY,
    price DOUBLE,
    expected_return DOUBLE,
    volatility DOUBLE,
    FOREIGN KEY (ticker) REFERENCES product (ticker)
);

CREATE TABLE option (
    ticker VARCHAR(50) NOT NULL PRIMARY KEY,
    strike DOUBLE,
    maturity VARCHAR(50),
    option_type VARCHAR(4),
    underlying_stock_ticker VARCHAR(50),
    FOREIGN KEY (ticker) REFERENCES product (ticker),
    FOREIGN KEY (underlying_stock_ticker) REFERENCES stock (ticker)
);
