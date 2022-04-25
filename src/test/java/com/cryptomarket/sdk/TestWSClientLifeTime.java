package com.cryptomarket.sdk;

import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.cryptomarket.sdk.models.Balance;
import com.cryptomarket.sdk.models.WSOrderBook;
import com.cryptomarket.sdk.websocket.CryptomarketWSWalletClient;
import com.cryptomarket.sdk.websocket.CryptomarketWSWalletClientImpl;
import com.cryptomarket.sdk.websocket.CryptomarketWSMarketDataClient;
import com.cryptomarket.sdk.websocket.CryptomarketWSMarketDataClientImpl;
import com.cryptomarket.sdk.websocket.CryptomarketWSSpotTradingClient;
import com.cryptomarket.sdk.websocket.CryptomarketWSSpotTradingClientImpl;

import org.junit.Test;

public class TestWSClientLifeTime {

  @Test
  public void testPublicClientLifetime() {
    try {
      CryptomarketWSMarketDataClient wsClient;
      wsClient = new CryptomarketWSMarketDataClientImpl() {
        @Override
        public void onClose(String reason) {
          System.out.println("closing");
        }

        @Override
        public void onConnect() {
          System.out.println("connected");
          this.subscribeToFullOrderBook(
              new Callback<Map<String, WSOrderBook>>() {
                public void resolve(Map<String, WSOrderBook> result) {
                  System.out.println("subscription feed");
                };

                @Override
                public void reject(Throwable exception) {
                  System.out.println("error in subscription feed");
                }
              },
              Arrays.asList("EOSETH"),
              new Callback<List<String>>() {
                @Override
                public void resolve(List<String> result) {
                  System.out.println("subscribed successfully");
                }

                @Override
                public void reject(Throwable exception) {
                  System.out.println("failed to subscribe");
                }
              });
          try {
            TimeUnit.SECONDS.sleep(3);
          } catch (InterruptedException e) {
            fail();
          }
          this.close();
        }

        @Override
        public void onFailure(Throwable t) {
          System.out.print("failed connection");
          t.printStackTrace();
        }
      };
      wsClient.connect();
      try {
        TimeUnit.SECONDS.sleep(8);
      } catch (InterruptedException e) {
        fail();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testTradingClientLifetime() {
    try {
      CryptomarketWSSpotTradingClient wsClient;
      wsClient = new CryptomarketWSSpotTradingClientImpl(KeyLoader.getApiKey(), KeyLoader.getApiSecret()) {
        @Override
        public void onClose(String reason) {
          System.out.println("closing");

        }

        @Override
        public void onConnect() {
          System.out.println("connected");
          try {
            TimeUnit.SECONDS.sleep(3);
          } catch (InterruptedException e) {
            fail();
          }
          this.close();
        }

        @Override
        public void onFailure(Throwable t) {
          t.printStackTrace();
        }
      };
      wsClient.connect();
      try {
        TimeUnit.SECONDS.sleep(8);
      } catch (

      InterruptedException e) {
        fail();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testAccountClientLifetime() {
    try {
      CryptomarketWSWalletClient wsClient;
      wsClient = new CryptomarketWSWalletClientImpl(KeyLoader.getApiKey(), KeyLoader.getApiSecret()) {
        @Override
        public void onClose(String reason) {
          System.out.println("closing");

        }

        @Override
        public void onConnect() {
          System.out.println("connected");
          this.getWalletBalances(new Callback<List<Balance>>() {
            @Override
            public void resolve(List<Balance> result) {
              System.out.println("request returning");
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
          this.close();
        }

        @Override
        public void onFailure(Throwable t) {
          t.printStackTrace();
        }
      };
      wsClient.connect();
      try {
        TimeUnit.SECONDS.sleep(8);
      } catch (InterruptedException e) {
        fail();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testFailedAuth() {
    try {
      CryptomarketWSWalletClient wsClient = new CryptomarketWSWalletClientImpl("uno", "dois");
      wsClient.connect();
      try {
        TimeUnit.SECONDS.sleep(8);
      } catch (InterruptedException e) {
        fail();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
