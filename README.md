# CryptoMarket-Java
[main page](https://www.cryptomkt.com/)


[sign up in CryptoMarket](https://www.cryptomkt.com/account/register).

# Installation
add the Maven dependency
```xml
<dependency>
    <groupId>com.cryptomkt.api</groupId>
    <artifactId>cryptomarket</artifactId>
    <version>1.1.0</version>
</dependency>
```
# Documentation

This sdk makes use of the [api version 2](https://api.exchange.cryptomkt.com/v2) of cryptomarket.

# Quick Start

## rest client
```java

// instance a client
String api_key = "AB32B3201";
String api_secret= "21b12401";
CryptomarketRestClient client = new CryptomarketRestClientImpl(api_key, api_secret);

// get all currencies
Map<String, Currency> currencies = client.getCurrencies(null);

// get some symbols
List<String> symbolIDs = new ArrayList<String>(Arrays.asList("eoseth","ethbtc"));
Map<String, Symbol> symbols = client.getSymbols(symbolIDs);

// get an order book
OrderBook  orderbook = client.getOrderbookOfSymbol("eoseth");

// get some candles
List<String> symbols = new ArrayList<String>(Arrays.asList("eoseth", "ethbtc"));
Map<String, List<Candle>> candles = client.getCandles(symbols, Period._4_HOURS, Sort.ASC, null, null, null);

// get your account balances
List<Balance> balances = client.getAccountBalance();

// get your trading balances
List<Balance> balances = client.getTradingBalance();

// move balance from account to trading
String result = client.transferBetweenTradingAndAccountBalance('eth', '3.2', TransferType.BANK_TO_EXCHANGE);

// get your active orders
List<Order> orders = client.getActiveOrders('eoseth');


// create a new order
Order order = client.createOrder(new ParamsBuilder()
            .symbol("EOSETH")
            .side(Side.SELL)
            .quantity("2")
            .price("100000")
            .timeInForce(TimeInForce.DAY));
```
## Using the ParamsBuilder
most client methods have a version that accepts a ParamBuilder. This class makes easier to pass parameters.
```java
import com.cryptomarket.params.ParamsBuilder;

// get candles
Map<String, List<Candle>> candles client.getCandles(new ParamsBuilder()
          .symbols(symbols)
          .period(Period._4_HOURS)
          .sort(Sort.ASC));
```

## websocket client
There are three websocket clients, the market data client, the spot trading client and the wallet client.
The market data client requires no authentication, while the spot trading client and the wallet client do require it.

All websocket methods accept a callback class with two methods, one to handle the incoming data (resolve) and one to handle a
possible incomming error (reject). The callback base class is:
```java
public abstract class Callback<T> {
    abstract public void resolve(T result);
    public void reject(Throwable exception) {};
}
```

Example of use of the websocket client
```java


// instantiate a market data client
CryptomarketWSMarketDataClient marketDataClient = CryptomarketWSMarketDataClientImpl();

// make a partial orderbook subscription
//    prepare args
List<String> symbols = Arrays.asList("EOSETH");

Callback<Map<String, WSOrderBook>> callback = new Callback<Map<String, WSOrderBook>>() {
  @Override
  public void resolve(Map<String, WSOrderBook> result) {
    System.out.println(result);
    result.forEach((symbol, orderbook) -> {
      System.out.println("symbol:" + symbol);
      System.out.println(orderbook);
    });
  }
};
//    make subscription
marketDataClient.subscribeToPartialOrderBook(
    callback,
    Depth._5,
    OBSpeed._500_MILISECONDS,
    symbols,
    null);


// instantiate a spot trading websocket client
String apiKey = "AB32B3201";
String apiSecret= "21b12401";
CryptomarketWSSpotTradingClient tradingClient = new CryptomarketWSSpotTradingClientImpl(apiKey, apiSecret);

// get all the spot trading balances
tradingClient.getSpotTradingBalances(
    new Callback<List<Balance>>() {
      @Override
      public void resolve(List<Balance> result) {
        result.forEach(System.out::println);
      }

      @Override
      public void reject(Throwable exception) {
        // handle the error
      }
    });

// instantiate a wallet websocket client
CryptomarketWSWalletClient walletClient = new CryptomarketWSWalletClientImpl(apiKey, apiSecret);

// get a list of transactions
walletClient.getTransactions(new Callback<List<Transaction>>() {
      @Override
      public void resolve(List<Transaction> result) {
        result.forEach(System.out::println);
      }
    }, null);
```


## exception handling
```java

// rest exceptions
CryptomarketRestClient client = new CryptomarketRestClientImpl(api_key, api_secret);

// all rest client methods can throw a CryptomarketSDKException
try {
    List<String> symbolIds = new ArrayList<String>(Arrays.asList("eoseth","ethbtc"));
    List<Ticker> tickers = client.getTickers(symbolIds);
    assertTrue(tickers.size() == 2);
} catch (CryptomarketApiException e) {
    e.printStackTrace();
}

```

# Constants

All constants required for calls are in `com.cryptomarket.sdk.params`.
each enum has the name of the argument that needs it.
Here is a comprehensive list.

```java
import com.cryptomarket.params.*;
// use for candle intervals and websocket candles subscriptions
Period._1_MINUTES;
Period._3_MINUTES;
Period._5_MINUTES;
Period._15_MINUTES;
Period._30_MINUTES;
Period._1_HOURS;
Period._4_HOURS;
Period._1_DAYS;
Period._7_DAYS;
Period._1_MONTHS;

// used for pagination sorting direction
Sort.DESC;
Sort.ASC;

// used for pagination field for sorting
By.TIMESTAMP;
By.ID;

// used for creating orders
Side.BUY;
Side.SELL;

// used for creating orders
TimeInForce.GTC; // Good till canceled
TimeInForce.IOC; // Immediate or cancell
TimeInForce.FOK; // Fill or kill
TimeInForce.DAY; // Good for the day
TimeInForce.GTD; // Good till date

// used for order creation
OrderType.LIMIT;
OrderType.MARKET;
OrderType.STOP_LIMIT;
OrderType.STOP_MARKET;
OrderType.TAKE_PROFIT_LIMIT;
OrderType.TAKE_PROFIT_MARKET;

// used for transfer to another user
IdentifyBy.EMAIL;
IdentifyBy.USERNAME;

// used for sorting data
SortBy.TIMESTAMP;
SortBy.ID;
SortBy.DATETIME;

// used for transfer
AccountType.WALLET;
AccountType.SPOT;

// used for querying transactions
TransactionStatus.CREATED;
TransactionStatus.PENDING;
TransactionStatus.FAILED;
TransactionStatus.SUCCESS;
TransactionStatus.ROLLED_BACK;

// used for querying transactions
TransactionsSubtype.UNCLASSIFIED;
TransactionsSubtype.BLOCKCHAIN;
TransactionsSubtype.AIRDROP;
TransactionsSubtype.AFFILIATE;
TransactionsSubtype.STAKING;
TransactionsSubtype.BUY_CRYPTO;
TransactionsSubtype.OFFCHAIN;
TransactionsSubtype.FIAT;
TransactionsSubtype.SUB_ACCOUNT;
TransactionsSubtype.WALLET_TO_SPOT;
TransactionsSubtype.SPOT_TO_WALLET;
TransactionsSubtype.WALLET_TO_DERIVATIVES;
TransactionsSubtype.DERIVATIVES_TO_WALLET;
TransactionsSubtype.CHAIN_SWITCH_FROM;
TransactionsSubtype.CHAIN_SWITCH_TO;
TransactionsSubtype.INSTANT_EXCHANGE;

// used for querying transactions
TransactionType.DEPOSIT;
TransactionType.WITHDRAW;
TransactionType.TRANSFER;
TransactionType.SWAP;

// used for withrdawing crypto
UseOffchain.NEVER;
UseOffchain.OPTIONALLY;
UseOffchain.REQUIRED;

// used for ticker websocket subscriptions
TickerSpeed._1_SECONDS;
TickerSpeed._3_SECONDS;

// used for orderbook websocket subscriptions
OBSpeed._100_MILISECONDS;
OBSpeed._500_MILISECONDS;
OBSpeed._1000_MILISECONDS;

// used for orderbook subscriptions
Depth._5;
Depth._10;
Depth._20;

// used for order list creation
ContingencyType.ALL_OR_NONE;
ContingencyType.ONE_CANCEL_OTHER;
ContingencyType.ONE_TRIGGER_ONE_CANCEL_OTHER;
```

# Checkout our other SDKs
<!-- agregar links -->
[node sdk](https://github.com/cryptomkt/cryptomkt-node)

[ruby sdk](https://github.com/cryptomkt/cryptomkt-ruby)

[go sdk](https://github.com/cryptomkt/cryptomkt-go)

[python sdk](https://github.com/cryptomkt/cryptomkt-python)