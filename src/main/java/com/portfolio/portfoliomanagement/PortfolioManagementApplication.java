package com.portfolio.portfoliomanagement;

import com.portfolio.portfoliomanagement.service.PortfolioCSVParserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.io.IOException;

@SpringBootApplication
@EnableAsync
@EnableScheduling
public class PortfolioManagementApplication implements CommandLineRunner {

    @Autowired
    private PortfolioCSVParserService csvParserService;

    public static void main(String[] args) {
        SpringApplication.run(PortfolioManagementApplication.class, args);
    }

    @Override
    public void run(String... args) throws IOException {
        if (args.length < 1) {
            System.err.println("Please provide the path to the CSV file as a program argument.");
            System.exit(1);
        }

        String csvFilePath = args[0];
        csvParserService.createAndRegisterPortfolioFromCSV(csvFilePath);
    }
}
