# Portfolio Management Application

## Overview

This application is designed to manage and monitor a portfolio of stocks and options. The portfolio is dynamically updated based on simulated market data, and the changes are reflected in real-time. The program reads portfolio data from a CSV file, updates the market data periodically, and displays the portfolio value changes in the console.

## Features

- Read portfolio data from a CSV file.
- Periodically update stock prices using a simulated market data provider.
- Recalculate portfolio values based on updated stock prices.
- Display portfolio updates in a formatted table in the console.

## Input

The program expects a CSV file containing the portfolio data with the following format:

symbol,positionSize
AAPL,1000
AAPL-OCT-2025-110-C,-20000
AAPL-OCT-2025-110-P,20000
TSLA,-500
TSLA-NOV-2025-400-C,10000
TSLA-DEC-2025-400-P,-10000

### Columns

- `symbol`: The ticker symbol of the stock or option.
- `positionSize`: The number of shares (for stocks) or contracts (for options) held in the portfolio.

## Output

The program displays the portfolio updates in the console with the following format:

Portfolio Update

Previous Value: -35725141.96

Total Value: -35725137.61 ↑

Symbol                    Price           Qty             Value          
Stocks:
AAPL                      99.9956         1000            99995.6189     
TSLA                      100.0006        -500            -50000.3111
Options:
AAPL-OCT-2025-110-C       6.2021          -20000          -124041.6066   
AAPL-OCT-2025-110-P       13.4039         20000           268077.5553    
TSLA-NOV-2025-400-C       0.0000          10000           0.0003         
TSLA-DEC-2025-400-P       288.5074        -10000          -2885074.1686


### Running the Application

#### From IntelliJ

1. **Clone the Repository**:
   Clone the repository to your local machine using Git or download the source code.

2. **Open the Project in IntelliJ**:
    - Open IntelliJ IDEA.
    - Select `Open` and navigate to the project directory.
    - Select the project to open it.

3. **Configure the Application**:
    - Go to `Run` > `Edit Configurations`.
    - Select your application configuration or create a new one.
    - In the `Program arguments` field, enter the path to your CSV file, for example:
      ```
      /Users/yourusername/IdeaProjects/PortfolioManager/src/main/resources/example_portfolio.csv
      ```
    - Click `Apply` and then `OK`.

4. **Run the Application**:
    - Click the `Run` button to start the application.

#### From the Command Line

1. **Clone the Repository**:
   Clone the repository to your local machine using Git or download the source code.

2. **Build the Application**:
   Navigate to the project directory and build the application using Gradle:
   ./gradlew build

3. **Run the Application**:
   java -jar build/libs/PortfolioManager-0.0.1-SNAPSHOT.jar {CSVFilePath}

