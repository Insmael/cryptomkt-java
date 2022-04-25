package com.cryptomarket.sdk;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.cryptomarket.params.Depth;
import com.cryptomarket.params.OBSpeed;
import com.cryptomarket.params.Period;
import com.cryptomarket.params.TickerSpeed;
import com.cryptomarket.sdk.models.WSCandle;
import com.cryptomarket.sdk.models.WSOrderBook;
import com.cryptomarket.sdk.models.WSOrderBookTop;
import com.cryptomarket.sdk.models.WSPublicTrade;
import com.cryptomarket.sdk.models.WSTicker;
import com.cryptomarket.sdk.websocket.CryptomarketWSPublicClient;
import com.cryptomarket.sdk.websocket.CryptomarketWSPublicClientImpl;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestWSPublicClientSubs {
  CryptomarketWSPublicClient wsClient;
  Boolean authenticated = false;
  Callback<Boolean> resultCallback = new Callback<Boolean>() {
    @Override
    public void resolve(Boolean result) {
      ;
    }

    @Override
    public void reject(Throwable exception) {
      fail();
    }
  };

  @Before
  public void before() {
    try {
      wsClient = new CryptomarketWSPublicClientImpl();
      wsClient.connect();
      try {
        TimeUnit.SECONDS.sleep(3);
      } catch (InterruptedException e) {
        fail();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @After
  public void after() {
    wsClient.close();
  }

  @Test
  public void TestTimeFlow() {
    TimeFlow.reset();
    Boolean goodFLow;
    goodFLow = TimeFlow.checkNextTimestamp("2021-01-27T15:47:54.418Z");
    if (!goodFLow) {
      fail();
    }
    goodFLow = TimeFlow.checkNextTimestamp("2021-01-27T15:47:55.118Z");
    if (!goodFLow) {
      fail();
    }
    goodFLow = TimeFlow.checkNextTimestamp("2021-01-27T15:47:54.418Z");
    if (goodFLow) {
      fail();
    }
  }

  @Test
  public void testTradesSubscription() {
    List<String> symbols = Arrays.asList("EOSETH");
    Callback<Map<String, List<WSPublicTrade>>> callback = new Callback<Map<String, List<WSPublicTrade>>>() {
      @Override
      public void resolve(Map<String, List<WSPublicTrade>> result) {
        System.out.println(result);
      }
    };
    Callback<List<String>> resultCallback = new Callback<List<String>>() {
      @Override
      public void resolve(List<String> result) {
        System.out.println(result);
      }

      @Override
      public void reject(Throwable exception) {
        System.out.println(exception);
        fail();
      }
    };

    wsClient.subscribeToTrades(
        callback,
        symbols,
        null,
        resultCallback);

    try {
      TimeUnit.SECONDS.sleep(30);
    } catch (InterruptedException e) {
      fail();
    }
    try {
      TimeUnit.SECONDS.sleep(5);
    } catch (InterruptedException e) {
      fail();
    }
  }

  @Test
  public void testSubscribeToCandles() {
    List<String> symbols = Arrays.asList("EOSETH");

    Callback<Map<String, List<WSCandle>>> callback = new Callback<Map<String, List<WSCandle>>>() {
      @Override
      public void resolve(Map<String, List<WSCandle>> result) {
        assertTrue(result.size() != 0);
        result.forEach((k, v) -> v.forEach(Checker.checkWSCandle));
      }
    };

    wsClient.subscribeToCandles(
        callback,
        Period._1_MINUTES,
        symbols,
        null,
        null);

    try {
      TimeUnit.SECONDS.sleep(30);
    } catch (InterruptedException e) {
      fail();
    }
  }

  @Test
  public void testSubscribeToMiniTicker() {
    List<String> symbols = Arrays.asList("EOSETH");

    Callback<Map<String, WSCandle>> callback = new Callback<Map<String, WSCandle>>() {
      @Override
      public void resolve(Map<String, WSCandle> result) {
        System.out.println(result);
        result.forEach((k, v) -> Checker.checkWSCandle.accept(v));
      }
    };

    wsClient.subscribeToMiniTicker(
        callback,
        TickerSpeed._1_SECONDS,
        symbols,
        null);

    try {
      TimeUnit.SECONDS.sleep(30);
    } catch (InterruptedException e) {
      fail();
    }
  }

  @Test
  public void testSubscribeToTicker() {
    List<String> symbols = Arrays.asList("EOSETH");

    Callback<Map<String, WSTicker>> callback = new Callback<Map<String, WSTicker>>() {
      @Override
      public void resolve(Map<String, WSTicker> result) {
        System.out.println(result);
        result.forEach((k, v) -> Checker.checkWSTicker.accept(v));
      }
    };

    wsClient.subscribeToTicker(
        callback,
        TickerSpeed._1_SECONDS,
        symbols,
        null);

    try {
      TimeUnit.SECONDS.sleep(30);
    } catch (InterruptedException e) {
      fail();
    }
  }

  @Test
  public void testFullOrderbook() {
    List<String> symbols = Arrays.asList("EOSETH");

    Callback<Map<String, WSOrderBook>> callback = new Callback<Map<String, WSOrderBook>>() {
      @Override
      public void resolve(Map<String, WSOrderBook> result) {
        System.out.println(result);
        result.forEach((k, v) -> Checker.checkWSOrderBook.accept(v));
      }
    };

    wsClient.subscribeToFullOrderBook(
        callback,
        symbols,
        null);

    try {
      TimeUnit.SECONDS.sleep(30);
    } catch (InterruptedException e) {
      fail();
    }
  }

  @Test
  public void testPartialOrderbook() {
    List<String> symbols = Arrays.asList("EOSETH");

    Callback<Map<String, WSOrderBook>> callback = new Callback<Map<String, WSOrderBook>>() {
      @Override
      public void resolve(Map<String, WSOrderBook> result) {
        System.out.println(result);
        result.forEach((k, v) -> Checker.checkWSOrderBook.accept(v));
      }
    };

    wsClient.subscribeToPartialOrderBook(
        callback,
        Depth._5,
        OBSpeed._500_MILISECONDS,
        symbols,
        null);

    try {
      TimeUnit.SECONDS.sleep(30);
    } catch (InterruptedException e) {
      fail();
    }
  }

  @Test
  public void testOrderbookTop() {
    List<String> symbols = Arrays.asList("EOSETH");

    Callback<Map<String, WSOrderBookTop>> callback = new Callback<Map<String, WSOrderBookTop>>() {
      @Override
      public void resolve(Map<String, WSOrderBookTop> result) {
        System.out.println(result);
        result.forEach((k, v) -> Checker.checkWSOrderBookTop.accept(v));
      }
    };

    wsClient.subscribeToTopOfOrderBook(
        callback,
        OBSpeed._500_MILISECONDS,
        symbols,
        null);

    try {
      TimeUnit.SECONDS.sleep(30);
    } catch (InterruptedException e) {
      fail();
    }
  }

}
