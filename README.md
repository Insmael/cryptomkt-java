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
List<String> symbolIds = new ArrayList<String>(Arrays.asList("eoseth","ethbtc"));
Map<String, Symbol> symbols = client.getSymbols(symbolIds);

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
*work in progress*

## exception handling
```java

// rest exceptions
CryptomarketRestClient client = new CryptomarketRestClientImpl(api_key, api_secret);

// all rest client methods can throw a CryptomarketApiException
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
Here is the full list

TODO: update

```java
import com.cryptomarket.params.*;
// use for candle intervals
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

// used for creating orders
OrderType.LIMIT;
OrderType.MARKET;
OrderType.STOPLIMIT;
OrderType.STOPMARKET;

// used for transfer to another user
TransferBy.EMAIL;
TransferBy.USERNAME;
```
# OrderRequest
An OrderRequest is used to create orders. Here is an example with all posible parameters
```java
import com.cryptomarket.params.*;
OrderRequest request = new OrderRequest.Builder()
    .clientOrderId("123123123")
    .symbol("eoseth")
    .side(Side.SELL)
    .quantity("11111")
    .orderType(OrderType.STOPLIMIT)
    .price("123")
    .stopPrice("123")
    .timeInForce(TimeInForce.GDT)
    .expireTime("2020-12-16T17:30:00.000Z")
    .strictValidate(true)
    .postOnly(false)
    .build();
```

# Pagination
A Pagination is used to request some data.
Here is an example with all posible parameters, sorting by id
```java
import com.cryptomarket.params.*;
new Pagination.Builder()
    .sort(Sort.ASC)
    .by(By.ID)
    .from("1000000000")
    .till("1000002000")
    .limit(20)
    .offset(40)
    .build()
```

# Checkout our other SDKs
<!-- agregar links -->
[node sdk](https://github.com/cryptomkt/cryptomkt-node)

[ruby sdk](https://github.com/cryptomkt/cryptomkt-ruby)

[go sdk](https://github.com/cryptomkt/cryptomkt-go)

[python sdk](https://github.com/cryptomkt/cryptomkt-python)