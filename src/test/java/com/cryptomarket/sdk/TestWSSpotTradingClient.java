package com.cryptomarket.sdk;

import static org.junit.Assert.fail;

import java.util.List;
import java.util.concurrent.TimeUnit;

import com.cryptomarket.params.ParamsBuilder;
import com.cryptomarket.params.Side;
import com.cryptomarket.sdk.models.Balance;
import com.cryptomarket.sdk.models.Commission;
import com.cryptomarket.sdk.models.OrderStatus;
import com.cryptomarket.sdk.models.Report;
import com.cryptomarket.sdk.websocket.CryptomarketWSSpotTradingClient;
import com.cryptomarket.sdk.websocket.CryptomarketWSSpotTradingClientImpl;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestWSSpotTradingClient {

  CryptomarketWSSpotTradingClient wsClient;
  Boolean authenticated = false;

  @Before
  public void before() {
    try {
      wsClient = new CryptomarketWSSpotTradingClientImpl(KeyLoader.getApiKey(), KeyLoader.getApiSecret(), 10);
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
  public void testGetSpotTradingBalances() {
    wsClient.getSpotTradingBalances(
        new Callback<List<Balance>>() {
          @Override
          public void resolve(List<Balance> result) {
            result.forEach(Checker.checkBalance);
          }

          @Override
          public void reject(Throwable exception) {
            fail();
          }
        });
    try {
      TimeUnit.SECONDS.sleep(3);
    } catch (InterruptedException e) {
      fail();
    }
  }

  @Test
  public void testSpotTradingBalance() {
    wsClient.getSpotTradingBalanceOfSymbol("EOSETH", new Callback<Balance>() {
      @Override
      public void resolve(Balance result) {
        Checker.checkBalance.accept(result);
      }
    });
    try {
      TimeUnit.SECONDS.sleep(3);
    } catch (InterruptedException e) {
      fail();
    }
  }

  @Test
  public void testOrderFlow() {
    String oldClientOrderID = String.format("%d", System.currentTimeMillis()) + "11";
    String newClientOrderID = String.format("%d", System.currentTimeMillis()) + "22";

    // create
    wsClient.createSpotOrder(
        new ParamsBuilder()
            .side(Side.SELL)
            .symbol("EOSETH")
            .price("10000")
            .quantity("0.01")
            .clientOrderID(oldClientOrderID),
        new Callback<Report>() {
          @Override
          public void resolve(Report result) {
            Checker.checkReport.accept(result);
          }

          @Override
          public void reject(Throwable exception) {
            fail();
          }
        });

    try {
      TimeUnit.SECONDS.sleep(3);
    } catch (InterruptedException e) {
      fail();
    }
    // check
    wsClient.getAllActiveOrders(
        new Callback<List<Report>>() {
          @Override
          public void resolve(List<Report> result) {
            Boolean present = false;
            for (Report order : result) {
              if (order.getClientOrderID().equals(oldClientOrderID))
                present = true;
            }
            if (!present)
              fail("could not find");
          }

          @Override
          public void reject(Throwable exception) {
            fail();
          }
        });

    try {
      TimeUnit.SECONDS.sleep(3);
    } catch (InterruptedException e) {
      fail();
    }

    // replace
    wsClient.replaceSpotOrder(
        oldClientOrderID,
        newClientOrderID,
        "0.02",
        "2000",
        null,
        new Callback<Report>() {
          @Override
          public void resolve(Report result) {
            if (!result.getOriginalClientOrderID().equals(oldClientOrderID))
              fail();
          }

          @Override
          public void reject(Throwable exception) {
            exception.printStackTrace();
            fail("could not replace");
          }
        });

    try {
      TimeUnit.SECONDS.sleep(3);
    } catch (InterruptedException e) {
      fail();
    }

    // cancel
    wsClient.cancelSpotOrder(
        newClientOrderID,
        new Callback<Report>() {
          @Override
          public void resolve(Report result) {
            if (!result.getStatus().equals(OrderStatus.CANCELED))
              fail();
            if (result.getClientOrderID().equals(oldClientOrderID))
              fail();
          }

          @Override
          public void reject(Throwable exception) {
            exception.printStackTrace();
            fail("could not cancel");
          }
        });

    try {
      TimeUnit.SECONDS.sleep(3);
    } catch (InterruptedException e) {
      fail();
    }
  }

  @Test
  public void testGetActiveSpotOrdersAndCancelAllSpotOrders() {
    wsClient.cancelAllSpotOrders(null);
    for (int i = 0; i < 5; i++) {
      wsClient.createSpotOrder(
          new ParamsBuilder()
              .symbol("EOSETH")
              .side(Side.SELL)
              .price("1000")
              .quantity("0.01"),
          null);
    }
    Callback<List<Report>> callback = new Callback<List<Report>>() {
      @Override
      public void resolve(List<Report> result) {
        if (result.size() != 5) {
          fail();
        }
        result.forEach(Checker.checkReport);
      }

      @Override
      public void reject(Throwable exception) {
        fail();
      }
    };
    try {
      TimeUnit.SECONDS.sleep(3);
    } catch (InterruptedException e) {
      fail();
    }
    wsClient.getAllActiveOrders(callback);
    try {
      TimeUnit.SECONDS.sleep(3);
    } catch (InterruptedException e) {
      fail();
    }
    wsClient.cancelAllSpotOrders(callback);
    try {
      TimeUnit.SECONDS.sleep(3);
    } catch (InterruptedException e) {
      fail();
    }
  }

  @Test
  public void testGetSpotTradingCommissions() {
    wsClient.getSpotCommissions(new Callback<List<Commission>>() {
      @Override
      public void resolve(List<Commission> result) {
        result.forEach(Checker.checkCommission);
      }
    });
    try {
      TimeUnit.SECONDS.sleep(3);
    } catch (InterruptedException e) {
      fail();
    }
  }

  @Test
  public void testGetSpotTradingCommission() {
    wsClient.getSpotCommissionOfSymbol("EOSETH", new Callback<Commission>() {
      @Override
      public void resolve(Commission result) {
        Checker.checkCommission.accept(result);
      }
    });
    try {
      TimeUnit.SECONDS.sleep(3);
    } catch (InterruptedException e) {
      fail();
    }
  }

  @Test
  public void testCreateOrderList() {
    // TODO
  }
}
