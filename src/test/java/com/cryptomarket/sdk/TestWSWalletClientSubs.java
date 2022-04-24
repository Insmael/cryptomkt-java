package com.cryptomarket.sdk;

import static org.junit.Assert.fail;

import java.util.List;
import java.util.concurrent.TimeUnit;

import com.cryptomarket.sdk.models.Transaction;
import com.cryptomarket.params.AccountType;
import com.cryptomarket.params.ParamsBuilder;
import com.cryptomarket.sdk.models.Balance;
import com.cryptomarket.sdk.websocket.CryptomarketWSWalletClient;
import com.cryptomarket.sdk.websocket.CryptomarketWSWalletClientImpl;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestWSWalletClientSubs {
  CryptomarketWSWalletClient wsClient;
  CryptomarketRestClient restClient;
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
      wsClient = new CryptomarketWSWalletClientImpl(KeyLoader.getApiKey(), KeyLoader.getApiSecret());
      wsClient.connect();
      restClient = new CryptomarketRestClientImpl(KeyLoader.getApiKey(), KeyLoader.getApiSecret());
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
  public void testSubscribeToTransactions() {
    wsClient.subscribeToTransactions(new Callback<Transaction>() {
      @Override
      public void resolve(Transaction result) {
        Checker.checkTransaction.accept(result);
      }

      @Override
      public void reject(Throwable exception) {
        fail();
      }
    }, new Callback<Boolean>() {
      @Override
      public void resolve(Boolean result) {
        if (!result) {
          fail();
        }
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
    try {
      restClient.transferBetweenWalletAndExchange(new ParamsBuilder()
          .currency("EOS")
          .amount("0.01")
          .source(AccountType.WALLET)
          .destination(AccountType.SPOT));
    } catch (Exception e) {
      e.printStackTrace();
      fail();
    }
    try {
      TimeUnit.SECONDS.sleep(3);
    } catch (InterruptedException e) {
      fail();
    }
    try {
      restClient.transferBetweenWalletAndExchange(new ParamsBuilder()
          .currency("EOS")
          .amount("0.01")
          .destination(AccountType.WALLET)
          .source(AccountType.SPOT));
    } catch (Exception e) {
      e.printStackTrace();
      fail();
    }
    try {
      TimeUnit.SECONDS.sleep(3);
    } catch (InterruptedException e) {
      fail();
    }
    wsClient.unsubscribeToTransactions(new Callback<Boolean>() {
      @Override
      public void resolve(Boolean result) {
        if (!result) {
          fail();
        }
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
  public void testSubscribeToWalletBalances() {
    wsClient.subscribeToWalletBalances(new Callback<List<Balance>>() {
      @Override
      public void resolve(List<Balance> result) {
        result.forEach(Checker.checkBalance);
      }

      @Override
      public void reject(Throwable exception) {
        fail();
      }
    }, new Callback<Boolean>() {
      @Override
      public void resolve(Boolean result) {
        if (!result) {
          fail();
        }
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
    try {
      restClient.transferBetweenWalletAndExchange(new ParamsBuilder()
          .currency("EOS")
          .amount("0.01")
          .source(AccountType.WALLET)
          .destination(AccountType.SPOT));
    } catch (Exception e) {
      e.printStackTrace();
      fail();
    }
    try {
      TimeUnit.SECONDS.sleep(3);
    } catch (InterruptedException e) {
      fail();
    }
    try {
      restClient.transferBetweenWalletAndExchange(new ParamsBuilder()
          .currency("EOS")
          .amount("0.01")
          .destination(AccountType.WALLET)
          .source(AccountType.SPOT));
    } catch (Exception e) {
      e.printStackTrace();
      fail();
    }
    try {
      TimeUnit.SECONDS.sleep(3);
    } catch (InterruptedException e) {
      fail();
    }
    wsClient.unsubscribeToWalletBalances(new Callback<Boolean>() {
      @Override
      public void resolve(Boolean result) {
        if (!result) {
          fail();
        }
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
}
