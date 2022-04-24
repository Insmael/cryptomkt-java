package com.cryptomarket.sdk;

import static org.junit.Assert.fail;

import java.util.concurrent.TimeUnit;

import com.cryptomarket.params.ParamsBuilder;
import com.cryptomarket.params.Side;
import com.cryptomarket.sdk.models.Report;
import com.cryptomarket.sdk.websocket.CryptomarketWSTradingClient;
import com.cryptomarket.sdk.websocket.CryptomarketWSTradingClientImpl;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestWSTradingClientSubs {
  CryptomarketWSTradingClient wsClient;
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
      wsClient = new CryptomarketWSTradingClientImpl(KeyLoader.getApiKey(), KeyLoader.getApiSecret());
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
  public void testReportSubscription() {
    wsClient.subscribeToReports(new Callback<Report>() {
      @Override
      public void resolve(Report result) {
        Checker.checkReport.accept(result);
      }
    }, new Callback<Boolean>() {
      @Override
      public void resolve(Boolean result) {
        if (!result) {
          fail();
        }
      }
    });
    try {
      TimeUnit.SECONDS.sleep(3);
    } catch (InterruptedException e) {
      fail();
    }

    String clientOrderID = String.format("%d", System.currentTimeMillis());
    wsClient.createSpotOrder(
        new ParamsBuilder()
            .clientOrderID(clientOrderID)
            .symbol("EOSETH")
            .side(Side.SELL)
            .price("1000")
            .quantity("0.01"),
        null);
    try {
      TimeUnit.SECONDS.sleep(3);
    } catch (InterruptedException e) {
      fail();
    }
    wsClient.cancelOrder(clientOrderID, null);
    try {
      TimeUnit.SECONDS.sleep(3);
    } catch (InterruptedException e) {
      fail();
    }
    wsClient.unsubscribeToReports(new Callback<Boolean>() {
      @Override
      public void resolve(Boolean result) {
        if (!result) {
          fail();
        }
      }
    });
    try {
      TimeUnit.SECONDS.sleep(3);
    } catch (InterruptedException e) {
      fail();
    }
  }
}
