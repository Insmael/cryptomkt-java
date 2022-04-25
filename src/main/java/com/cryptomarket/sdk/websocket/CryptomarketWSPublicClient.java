package com.cryptomarket.sdk.websocket;

import java.util.List;
import java.util.Map;

import com.cryptomarket.params.TickerSpeed;
import com.cryptomarket.params.Depth;
import com.cryptomarket.params.OBSpeed;
import com.cryptomarket.params.Period;
import com.cryptomarket.sdk.Callback;
import com.cryptomarket.sdk.models.WSCandle;
import com.cryptomarket.sdk.models.WSOrderBook;
import com.cryptomarket.sdk.models.WSOrderBookTop;
import com.cryptomarket.sdk.models.WSPublicTrade;
import com.cryptomarket.sdk.models.WSTicker;

import org.jetbrains.annotations.Nullable;

/**
 * PublicClient connects via websocket to cryptomarket to get market information
 * of the exchange.
 * <p>
 * Requires no API keys to make socket calls
 */
public interface CryptomarketWSPublicClient extends CryptomarketWS {

  /**
   * subscribe to a feed of trades
   * <p>
   * subscription is for the specified symbols
   * <p>
   * normal subscriptions have one update message per symbol
   * <p>
   * https://api.exchange.cryptomkt.com/#subscribe-to-trades
   *
   * @param callback       recieves a feed of trades as a map of them, indexed by
   *                       symbol id.
   * @param symbols        A list of symbol ids
   * @param limit          Number of historical entries returned in the first
   *                       feed. Min
   *                       is 0. Max is 1000. Default is 0
   * @param resultCallback Optional. recieves a list of successfully subscribed
   *                       symbols
   */
  public void subscribeToTrades(
      Callback<Map<String, List<WSPublicTrade>>> callback,
      List<String> symbols,
      Integer limit,
      @Nullable Callback<List<String>> resultCallback);

  /**
   * subscribe to a feed of candles
   * <p>
   * subscription is for the specified symbols
   * <p>
   * normal subscriptions have one update message per symbol
   * <p>
   * https://api.exchange.cryptomkt.com/#subscribe-to-candles
   *
   * @param callback       recieves a feed of candles as a map of them, indexed by
   *                       symbol id.
   * @param period         Optional. A valid tick interval. 'M1' (one minute),
   *                       'M3', 'M5', 'M15', 'M30', 'H1' (one hour), 'H4', 'D1'
   *                       (one day), 'D7', '1M' (one month). Default is 'M30'
   * @param symbols        A list of symbol ids
   * @param limit          Optional. Number of historical entries returned in the
   *                       first feed. Min
   *                       is 0. Max is 1000. Default is 0
   * @param resultCallback Optional. recieves a list of successfully subscribed
   *                       symbols
   */
  public void subscribeToCandles(
      Callback<Map<String, List<WSCandle>>> callback,
      Period period,
      @Nullable List<String> symbols,
      @Nullable Integer limit,
      @Nullable Callback<List<String>> resultCallback);

  /**
   * subscribe to a feed of mini tickers
   * <p>
   * subscription is for all symbols or for the specified symbols
   * <p>
   * normal subscriptions have one update message per symbol
   * <p>
   * https://api.exchange.cryptomkt.com/#subscribe-to-mini-ticker
   *
   * @param callback       recieves a feed of mini tickers as a map of them,
   *                       indexed by symbol id.
   * @param speed          The speed of the feed. '1s' or '3s'
   * @param symbols        Optional. A list of symbol ids
   * @param resultCallback Optional. recieves a list of successfully subscribed
   *                       symbols
   */
  public void subscribeToMiniTicker(
      Callback<Map<String, WSCandle>> callback,
      TickerSpeed speed,
      @Nullable List<String> symbols,
      @Nullable Callback<List<String>> resultCallback);

  /**
   * subscribe to a feed of mini tickers
   * <p>
   * subscription is for all symbols or for the specified symbols
   * <p>
   * batch subscriptions have a joined update for all symbols
   * <p>
   * https://api.exchange.cryptomkt.com/#subscribe-to-mini-ticker-in-batches
   *
   * @param callback       recieves a feed of mini tickers as a map of them,
   *                       indexed by symbol id.
   * @param speed          The speed of the feed. '1s' or '3s'
   * @param symbols        Optional. A list of symbol ids
   * @param resultCallback Optional. recieves a list of successfully subscribed
   *                       symbols
   */

  public void subscribeToMiniTickerInBatches(
      Callback<Map<String, WSCandle>> callback,
      TickerSpeed speed,
      @Nullable List<String> symbols,
      @Nullable Callback<List<String>> resultCallback);

  /**
   * subscribe to a feed of tickers
   * <p>
   * subscription is for all symbols or for the specified symbols
   * <p>
   * normal subscriptions have one update message per symbol
   * <p>
   * Requires no API key Access Rights
   * <p>
   * https://api.exchange.cryptomkt.com/#subscribe-to-ticker
   *
   * @param callback       recieves a feed of tickers as a map of them, indexed
   *                       by symbol id.
   * @param speed          The speed of the feed. '1s' or '3s'
   * @param symbols        Optional. A list of symbol ids
   * @param resultCallback Optional. recieves a list of successfully subscribed
   *                       symbols
   */
  public void subscribeToTicker(
      Callback<Map<String, WSTicker>> callback,
      TickerSpeed speed,
      @Nullable List<String> symbols,
      @Nullable Callback<List<String>> resultCallback);

  /**
   * subscribe to a feed of tickers
   * <p>
   * subscription is for all symbols or for the specified symbols
   * <p>
   * batch subscriptions have a joined update for all symbols
   * <p>
   * Requires no API key Access Rights
   * <p>
   * https://api.exchange.cryptomkt.com/#subscribe-to-ticker-in-batches
   *
   * @param callback       recieves a feed of tickers as a map of them, indexed
   *                       by symbol id.
   * @param speed          The speed of the feed. '1s' or '3s'
   * @param symbols        Optional. A list of symbol ids
   * @param resultCallback Optional. recieves a list of successfully subscribed
   *                       symbols
   */
  public void subscribeToTickerInBatches(
      Callback<Map<String, WSTicker>> callback,
      TickerSpeed speed,
      @Nullable List<String> symbols,
      @Nullable Callback<List<String>> resultCallback);

  /**
   * subscribe to a feed of a full orderbook
   * <p>
   * subscription is for the specified symbols
   * <p>
   * normal subscriptions have one update message per symbol
   * <p>
   * https://api.exchange.cryptomkt.com/#subscribe-to-full-order-book
   *
   * @param callback       recieves a feed of full orderbooks as a map of them,
   *                       indexed
   *                       by symbol id.
   * @param symbols        A list of symbol ids
   * @param resultCallback Optional. recieves a list of successfully subscribed
   *                       symbols
   *
   */
  public void subscribeToFullOrderBook(
      Callback<Map<String, WSOrderBook>> callback,
      List<String> symbols,
      @Nullable Callback<List<String>> resultCallback);

  /**
   * subscribe to a feed of a partial orderbook
   * <p>
   * subscription is for all symbols or for the specified symbols
   * <p>
   * normal subscriptions have one update message per symbol
   * <p>
   * https://api.exchange.cryptomkt.com/#subscribe-to-partial-order-book
   *
   * @param callback       recieves a feed of partial orderbooks as a map of them,
   *                       indexed
   *                       by symbol id.
   * @param depth          The depth of the partial orderbook
   * @param speed          The speed of the feed. '100ms', '500ms' or '1000ms'
   * @param symbols        Optional. A list of symbol ids
   * @param resultCallback Optional. recieves a list of successfully subscribed
   *                       symbols
   */
  public void subscribeToPartialOrderBook(
      Callback<Map<String, WSOrderBook>> callback,
      Depth depth,
      OBSpeed speed,
      @Nullable List<String> symbols,
      @Nullable Callback<List<String>> resultCallback);

  /**
   * subscribe to a feed of a partial orderbook in batches
   * <p>
   * subscription is for all symbols or for the specified symbols
   * <p>
   * batch subscriptions have a joined update for all symbols
   * <p>
   * https://api.exchange.cryptomkt.com/#subscribe-to-partial-order-book-in-batches
   *
   * @param callback       recieves a feed of partial orderbooks as a map of them,
   *                       indexed
   *                       by symbol id.
   * @param depth          The depth of the partial orderbook
   * @param speed          The speed of the feed. '100ms', '500ms' or '1000ms'
   * @param symbols        Optional. A list of symbol ids
   * @param resultCallback Optional. recieves a list of successfully subscribed
   *                       symbols
   *
   */
  public void subscribeToPartialOrderBookInBatches(
      Callback<Map<String, WSOrderBook>> callback,
      Depth depth,
      OBSpeed speed,
      @Nullable List<String> symbols,
      @Nullable Callback<List<String>> resultCallback);

  /**
   * subscribe to a feed of the top of the orderbook
   * <p>
   * subscription is for all symbols or for the specified symbols
   * <p>
   * normal subscriptions have one update message per symbol
   * <p>
   * https://api.exchange.cryptomkt.com/#subscribe-to-top-of-book
   *
   * @param callback       recieves a feed of the top of orderbooks as a map of
   *                       them, indexed
   *                       by symbol id.
   * @param speed          The speed of the feed. '100ms', '500ms' or '1000ms'
   * @param symbols        Optional. A list of symbol ids
   * @param resultCallback Optional. recieves a list of successfully subscribed
   *                       symbols
   */
  public void subscribeToTopOfOrderBook(
      Callback<Map<String, WSOrderBookTop>> callback,
      OBSpeed speed,
      @Nullable List<String> symbols,
      @Nullable Callback<List<String>> resultCallback);

  /**
   * subscribe to a feed of the top of the orderbook
   * <p>
   * subscription is for all symbols or for the specified symbols
   * <p>
   * batch subscriptions have a joined update for all symbols
   * <p>
   * https://api.exchange.cryptomkt.com/#subscribe-to-top-of-book-in-batches
   *
   * @param callback       recieves a feed of the top orderbooks as a map of them,
   *                       indexed
   *                       by symbol id.
   * @param speed          The speed of the feed. '100ms', '500ms' or '1000ms'
   * @param symbols        Optional. A list of symbol ids
   * @param resultCallback Optional. recieves a list of successfully subscribed
   *                       symbols
   */
  public void subscribeToTopOfOrderBookInBatches(
      Callback<Map<String, WSOrderBookTop>> callback,
      OBSpeed speed,
      @Nullable List<String> symbols,
      @Nullable Callback<List<String>> resultCallback);
}
