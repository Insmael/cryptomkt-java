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
 */
public interface CryptomarketWSPublicClient extends CryptomarketWS {

  public void subscribeToTrades(
      List<String> symbols,
      Callback<Map<String, List<WSPublicTrade>>> callback,
      @Nullable Callback<List<String>> resultCallback);

  public void subscribeToCandles(
      List<String> symbols,
      Period period,
      @Nullable Integer limit,
      Callback<Map<String, List<WSCandle>>> callback,
      @Nullable Callback<List<String>> resultCallback);

  public void subscribeToMiniTicker(
      List<String> symbols,
      TickerSpeed speed,
      Callback<Map<String, WSCandle>> callback,
      @Nullable Callback<List<String>> resultCallback);

  public void subscribeToMiniTickerInBatches(
      List<String> symbols,
      TickerSpeed speed,
      Callback<Map<String, WSCandle>> callback,
      @Nullable Callback<List<String>> resultCallback);

  public void subscribeToTicker(
      List<String> symbols,
      TickerSpeed speed,
      Callback<Map<String, WSTicker>> callback,
      @Nullable Callback<List<String>> resultCallback);

  public void subscribeToTickerInBatches(
      List<String> symbols,
      TickerSpeed speed,
      Callback<Map<String, WSTicker>> callback,
      @Nullable Callback<List<String>> resultCallback);

  public void subscribeToFullOrderBook(
      List<String> symbols,
      Callback<Map<String, WSOrderBook>> callback,
      @Nullable Callback<List<String>> resultCallback);

  public void subscribeToPartialOrderBook(
      List<String> symbols,
      Depth depth,
      OBSpeed speed,
      Callback<Map<String, WSOrderBook>> callback,
      @Nullable Callback<List<String>> resultCallback);

  public void subscribeToPartialOrderBookInBatches(
      List<String> symbols,
      Depth depth,
      OBSpeed speed,
      Callback<Map<String, WSOrderBook>> callback,
      @Nullable Callback<List<String>> resultCallback);

  public void subscribeToTopOfOrderBook(
      List<String> symbols,
      OBSpeed speed,
      Callback<Map<String, WSOrderBookTop>> callback,
      @Nullable Callback<List<String>> resultCallback);

  public void subscribeToTopOfOrderBookInBatches(
      List<String> symbols,
      OBSpeed speed,
      Callback<Map<String, WSOrderBookTop>> callback,
      @Nullable Callback<List<String>> resultCallback);
}
