package com.cryptomkt.api;

import com.cryptomkt.api.entity.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import junit.framework.TestCase;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class PublicEndpointsTest extends TestCase{

    protected CryptoMarket cryptoMarket;

    private ObjectMapper mapper = new ObjectMapper().configure(SerializationFeature.INDENT_OUTPUT, true);

    protected void printObject(Object object) {
        try {
            String jsonString = this.mapper.writeValueAsString(object);
            System.out.println(jsonString);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    protected void setUp() {
        String apiKey = "";
        String apiSecret = "";
        try {
            List<String> allLines = Files.readAllLines(Paths.get("/home/ismael/cptmkt/keys.txt"));
            apiKey = allLines.get(0);
            apiSecret = allLines.get(1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        cryptoMarket= new CryptoMarketImpl(apiKey, apiSecret);

    }

    public void testGetMarkets() {
        try {
            List<Market> markets = cryptoMarket.getMarkets().getMarkets();
            this.printObject(markets);

        } catch (Exception e) {
            e.printStackTrace();
        }
        assertTrue(true);
    }

    public void testGetAllTickers() {
        try {
            List<Ticker> tickers = cryptoMarket.getTickers().getTickers();
            this.printObject(tickers);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertTrue(true);
    }

    public void testGetOneTicker() {
        try {
            List<Ticker> tickers = cryptoMarket.getTickers("BTCBRL").getTickers();
            this.printObject(tickers);

        } catch (Exception e) {
            e.printStackTrace();
        }
        assertTrue(true);
    }

    public void testBook() {
        try {
            List<Book> books = cryptoMarket.getBook("ETHCLP", "buy").getBooks();
            this.printObject(books);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertTrue(true);
    }

    public void testGetTrades() {
        try {
            List<Trade> trades = cryptoMarket.getTrades("ETHARS").getTrades();
            this.printObject(trades);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertTrue(true);
    }

    public void testGetPrices() {
        try {
            Prices prices = cryptoMarket.getPrices("XLMCLP", "240", 1, 5).getPrices();
            this.printObject(prices);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertTrue(true);
    }

}
