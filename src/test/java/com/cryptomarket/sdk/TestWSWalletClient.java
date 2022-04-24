package com.cryptomarket.sdk;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;
import java.util.concurrent.TimeUnit;

import com.cryptomarket.sdk.models.Balance;
import com.cryptomarket.sdk.models.Transaction;
import com.cryptomarket.sdk.websocket.CryptomarketWSWalletClient;
import com.cryptomarket.sdk.websocket.CryptomarketWSWalletClientImpl;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestWSWalletClient {

  CryptomarketWSWalletClient wsClient;
  Boolean authenticated = false;

  @Before
  public void before() {
    try {
      wsClient = new CryptomarketWSWalletClientImpl(KeyLoader.getApiKey(), KeyLoader.getApiSecret());
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
  public void testGetWalletBalances() {
    wsClient.getWalletBalances(new Callback<List<Balance>>() {
      @Override
      public void resolve(List<Balance> result) {
        result.forEach(Checker.checkBalance);
      }
    });
    try {
      TimeUnit.SECONDS.sleep(3);
    } catch (InterruptedException e) {
      fail();
    }
  }

  @Test
  public void testGetWalletBalance() {
    wsClient.getWalletBalance(
        "EOS",
        new Callback<Balance>() {
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
  public void testGetTransactions() {
    wsClient.getTransactions(null, new Callback<List<Transaction>>() {
      @Override
      public void resolve(List<Transaction> result) {
        result.forEach(Checker.checkTransaction);
      }
    });
    try {
      TimeUnit.SECONDS.sleep(3);
    } catch (InterruptedException e) {
      fail();
    }
  }

}
