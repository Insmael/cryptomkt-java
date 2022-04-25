package com.cryptomarket.sdk.websocket;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.cryptomarket.params.TickerSpeed;
import com.cryptomarket.params.Depth;
import com.cryptomarket.params.OBSpeed;
import com.cryptomarket.params.ParamsBuilder;
import com.cryptomarket.params.Period;
import com.cryptomarket.sdk.Adapter;
import com.cryptomarket.sdk.Callback;
import com.cryptomarket.sdk.exceptions.CryptomarketSDKException;
import com.cryptomarket.sdk.models.WSCandle;
import com.cryptomarket.sdk.models.WSJsonResponse;
import com.cryptomarket.sdk.models.WSOrderBook;
import com.cryptomarket.sdk.models.WSOrderBookTop;
import com.cryptomarket.sdk.models.WSPublicTrade;
import com.cryptomarket.sdk.models.WSTicker;
import com.cryptomarket.sdk.websocket.interceptor.Interceptor;
import com.cryptomarket.sdk.websocket.interceptor.InterceptorFactory;

public class CryptomarketWSMarketDataClientImpl extends ClientBase implements CryptomarketWSMarketDataClient {
  OrderbookCache OBCache = new OrderbookCache();
  protected Adapter adapter = new Adapter();

  public CryptomarketWSMarketDataClientImpl() throws IOException {
    super("wss://api.exchange.cryptomkt.com/api/3/ws/public");
  }

  @Override
  public void handle(String json) throws CryptomarketSDKException {
    WSJsonResponse response = adapter.objectFromJson(json, WSJsonResponse.class);
    if (response.getChannel() != null) {
      handleNotification(response);
    } else if (response.getId() != null) {
      handleResponse(response);
    } else {
      // do nothing
    }

  }

  @Override
  protected void handleNotification(WSJsonResponse response) {
    String channel = response.getChannel();
    String key = buildKey(channel);
    Interceptor interceptor = interceptorCache.getSubscriptionInterceptor(key);
    if (interceptor != null) {
      interceptor.makeCall(response);
    } else {
    }
  }

  private void subscriptionByChannel(String channel, Map<String, Object> params, Interceptor feedInterceptor,
      Interceptor resultInterceptor) {
    String key = buildKey(channel);
    interceptorCache.storeSubscriptionInterceptor(key, feedInterceptor);
    Payload payload = new Payload("subscribe", channel, params);
    if (resultInterceptor != null) {
      Integer id = interceptorCache.storeInterceptor(resultInterceptor);
      payload.id = id;
    }
    String json = payloadAdapter.toJson(payload);
    websocket.send(json);
  };

  @Override
  protected String buildKey(String channel, Map<String, Object> params) {
    return this.getSubscritpionKeys().get(channel);
  }

  // PUBLIC METHODS
  @Override
  public void subscribeToTrades(
      Callback<Map<String, List<WSPublicTrade>>> callback,
      List<String> symbols,
      Integer limit,
      Callback<List<String>> resultCallback) {
    ParamsBuilder params = new ParamsBuilder().symbolList(symbols).limit(limit);
    Interceptor interceptor = InterceptorFactory.newMapStringListOfChanneledWSResponseObject(callback,
        WSPublicTrade.class);
    Interceptor resultInterceptor = (resultCallback == null)
        ? null
        : InterceptorFactory.ofSubscriptionResponse(resultCallback);
    subscriptionByChannel("trades", params.buildObjectMap(), interceptor, resultInterceptor);
  }

  @Override
  public void subscribeToCandles(
      Callback<Map<String, List<WSCandle>>> callback,
      Period period,
      List<String> symbols,
      Integer limit,
      Callback<List<String>> resultCallback) {
    ParamsBuilder params = new ParamsBuilder().symbolList(symbols);
    Interceptor interceptor = InterceptorFactory.newMapStringListOfChanneledWSResponseObject(callback, WSCandle.class);
    Interceptor resultInterceptor = (resultCallback == null)
        ? null
        : InterceptorFactory.ofSubscriptionResponse(resultCallback);
    subscriptionByChannel(String.format("candles/%s", period), params.buildObjectMap(), interceptor, resultInterceptor);
  }

  @Override
  public void subscribeToMiniTicker(
      Callback<Map<String, WSCandle>> callback,
      TickerSpeed speed,
      List<String> symbols,
      Callback<List<String>> resultCallback) {
    ParamsBuilder params = new ParamsBuilder().symbolList(symbols);
    Interceptor interceptor = InterceptorFactory.newOfChanneledWSResponse(callback, WSCandle.class);
    Interceptor resultInterceptor = (resultCallback == null)
        ? null
        : InterceptorFactory.ofSubscriptionResponse(resultCallback);
    subscriptionByChannel(String.format("ticker/price/%s", speed), params.buildObjectMap(), interceptor,
        resultInterceptor);
  }

  @Override
  public void subscribeToMiniTickerInBatches(
      Callback<Map<String, WSCandle>> callback,
      TickerSpeed speed,
      List<String> symbols,
      Callback<List<String>> resultCallback) {
    ParamsBuilder params = new ParamsBuilder().symbolList(symbols);
    Interceptor interceptor = InterceptorFactory.newOfChanneledWSResponse(callback, WSCandle.class);
    Interceptor resultInterceptor = (resultCallback == null)
        ? null
        : InterceptorFactory.ofSubscriptionResponse(resultCallback);
    subscriptionByChannel(String.format("ticker/price/%s/batch", speed), params.buildObjectMap(), interceptor,
        resultInterceptor);
  }

  @Override
  public void subscribeToTicker(
      Callback<Map<String, WSTicker>> callback,
      TickerSpeed speed,
      List<String> symbols,
      Callback<List<String>> resultCallback) {
    ParamsBuilder params = new ParamsBuilder().symbolList(symbols);
    Interceptor interceptor = InterceptorFactory.newOfChanneledWSResponse(callback, WSTicker.class);
    Interceptor resultInterceptor = (resultCallback == null)
        ? null
        : InterceptorFactory.ofSubscriptionResponse(resultCallback);
    subscriptionByChannel(String.format("ticker/%s", speed), params.buildObjectMap(), interceptor, resultInterceptor);

  }

  @Override
  public void subscribeToTickerInBatches(
      Callback<Map<String, WSTicker>> callback,
      TickerSpeed speed,
      List<String> symbols,
      Callback<List<String>> resultCallback) {
    ParamsBuilder params = new ParamsBuilder().symbolList(symbols);
    Interceptor interceptor = InterceptorFactory.newOfChanneledWSResponse(callback, WSTicker.class);
    Interceptor resultInterceptor = (resultCallback == null)
        ? null
        : InterceptorFactory.ofSubscriptionResponse(resultCallback);
    subscriptionByChannel(String.format("ticker/%s/batch", speed), params.buildObjectMap(), interceptor,
        resultInterceptor);
  }

  @Override
  public void subscribeToFullOrderBook(
      Callback<Map<String, WSOrderBook>> callback,
      List<String> symbols,
      Callback<List<String>> resultCallback) {
    ParamsBuilder params = new ParamsBuilder().symbolList(symbols);
    Interceptor interceptor = InterceptorFactory.newOfChanneledWSResponse(callback, WSOrderBook.class);
    Interceptor resultInterceptor = (resultCallback == null)
        ? null
        : InterceptorFactory.ofSubscriptionResponse(resultCallback);
    subscriptionByChannel("orderbook/full", params.buildObjectMap(), interceptor, resultInterceptor);

  }

  @Override
  public void subscribeToPartialOrderBook(
      Callback<Map<String, WSOrderBook>> callback,
      Depth depth,
      OBSpeed speed,
      List<String> symbols,
      Callback<List<String>> resultCallback) {
    ParamsBuilder params = new ParamsBuilder().symbolList(symbols);
    Interceptor interceptor = InterceptorFactory.newOfChanneledWSResponse(callback, WSOrderBook.class);
    Interceptor resultInterceptor = (resultCallback == null)
        ? null
        : InterceptorFactory.ofSubscriptionResponse(resultCallback);
    subscriptionByChannel(String.format("orderbook/%s/%s", depth, speed), params.buildObjectMap(), interceptor,
        resultInterceptor);
  }

  @Override
  public void subscribeToPartialOrderBookInBatches(
      Callback<Map<String, WSOrderBook>> callback,
      Depth depth,
      OBSpeed speed,
      List<String> symbols,
      Callback<List<String>> resultCallback) {
    ParamsBuilder params = new ParamsBuilder().symbolList(symbols);
    Interceptor interceptor = InterceptorFactory.newOfChanneledWSResponse(callback, WSOrderBook.class);
    Interceptor resultInterceptor = (resultCallback == null)
        ? null
        : InterceptorFactory.ofSubscriptionResponse(resultCallback);
    subscriptionByChannel(String.format("orderbook/%s/%s/batch", depth, speed), params.buildObjectMap(), interceptor,
        resultInterceptor);
  }

  @Override
  public void subscribeToTopOfOrderBook(
      Callback<Map<String, WSOrderBookTop>> callback,
      OBSpeed speed,
      List<String> symbols,
      Callback<List<String>> resultCallback) {
    ParamsBuilder params = new ParamsBuilder().symbolList(symbols);
    Interceptor interceptor = InterceptorFactory.newOfChanneledWSResponse(callback, WSOrderBookTop.class);
    Interceptor resultInterceptor = (resultCallback == null)
        ? null
        : InterceptorFactory.ofSubscriptionResponse(resultCallback);
    subscriptionByChannel(String.format("orderbook/top/%s", speed), params.buildObjectMap(), interceptor,
        resultInterceptor);
  }

  @Override
  public void subscribeToTopOfOrderBookInBatches(
      Callback<Map<String, WSOrderBookTop>> callback,
      OBSpeed speed,
      List<String> symbols,
      Callback<List<String>> resultCallback) {
    ParamsBuilder params = new ParamsBuilder().symbolList(symbols);
    Interceptor interceptor = InterceptorFactory.newOfChanneledWSResponse(callback, WSOrderBookTop.class);
    Interceptor resultInterceptor = (resultCallback == null)
        ? null
        : InterceptorFactory.ofSubscriptionResponse(resultCallback);
    subscriptionByChannel(String.format("orderbook/top/%s/batch", speed), params.buildObjectMap(), interceptor,
        resultInterceptor);
  }
}
