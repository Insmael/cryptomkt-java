package com.cryptomarket.sdk.websocket;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.cryptomarket.params.ContingencyType;
import com.cryptomarket.params.OrderType;
import com.cryptomarket.params.ParamsBuilder;
import com.cryptomarket.params.Side;
import com.cryptomarket.params.TimeInForce;
import com.cryptomarket.sdk.Callback;
import com.cryptomarket.sdk.exceptions.CryptomarketAPIException;
import com.cryptomarket.sdk.exceptions.CryptomarketSDKException;
import com.cryptomarket.sdk.models.Balance;
import com.cryptomarket.sdk.models.Commission;
import com.cryptomarket.sdk.models.ErrorBody;
import com.cryptomarket.sdk.models.Order;
import com.cryptomarket.sdk.models.Report;
import com.cryptomarket.sdk.models.WSJsonResponse;
import com.cryptomarket.sdk.websocket.interceptor.Interceptor;
import com.cryptomarket.sdk.websocket.interceptor.InterceptorFactory;
import com.squareup.moshi.JsonDataException;

public class CryptomarketWSTradingClientImpl extends AuthClient implements CryptomarketWSTradingClient {

  public CryptomarketWSTradingClientImpl(String apiKey, String apiSecret) throws IOException {
    super("wss://api.exchange.cryptomkt.com/api/3/ws/trading", apiKey, apiSecret);
    Map<String, String> subsKeys = this.getSubscritpionKeys();
    // reports
    subsKeys.put("spot_subscribe", "reports");
    subsKeys.put("spot_unsubscribe", "reports");
    subsKeys.put("spot_orders", "reports");
    subsKeys.put("spot_order", "reports");
  }

  @Override
  public void subscribeToReports(Callback<Report> callback, Callback<Boolean> resultCallback) {
    Interceptor feedInterceptor = new Interceptor() {
      @Override
      public void makeCall(WSJsonResponse response) {
        ErrorBody error = response.getError();
        if (error != null) {
          callback.reject(new CryptomarketAPIException(error));
        } else if (response.getMethod().equals("spot_orders")) {
          try {
            List<Report> reports = adapter.listFromValue(response.getParams(), Report.class);
            if (reports != null) {
              reports.forEach(report -> callback.resolve(report));
            }
          } catch (JsonDataException e) {
            callback.reject(new CryptomarketSDKException("unkown data format"));
          }
        } else if (response.getMethod().equals("spot_order")) {
          try {
            Report report = adapter.objectFromValue(response.getParams(), Report.class);
            callback.resolve(report);
          } catch (JsonDataException e) {
            callback.reject(new CryptomarketSDKException("unkown data format"));
          }
        }
      }
    };
    Interceptor resultInterceptor = (resultCallback == null)
        ? null
        : InterceptorFactory.newOfWSResponseObject(resultCallback, Boolean.class);
    sendSubscription("spot_subscribe", null, feedInterceptor, resultInterceptor);
  }

  @Override
  public void unsubscribeToReports(Callback<Boolean> callback) {
    // TODO test the result type (suposedly boolean), as is not said in the docs
    Interceptor interceptor = (callback == null)
        ? null
        : InterceptorFactory.newOfWSResponseObject(callback, Boolean.class);
    sendUnsubscription("spot_unsubscribe", null, interceptor);
  }

  @Override
  public void getAllActiveOrders(Callback<List<Report>> callback) {
    Interceptor interceptor = InterceptorFactory.newOfWSResponseList(callback, Report.class);
    sendById("spot_get_orders", null, interceptor);

  }

  @Override
  public void createSpotOrder(
      String symbol,
      Side side,
      String quantity,
      String clientOrderID,
      OrderType orderType,
      String price,
      String stopPrice,
      TimeInForce timeInForce,
      String expireTime,
      Boolean strictValidate,
      Boolean postOnly,
      String takeRate,
      String makeRate,
      Callback<Report> callback) {
    ParamsBuilder paramsBuilder = new ParamsBuilder()
        .symbol(symbol)
        .side(side)
        .quantity(quantity)
        .clientOrderID(clientOrderID)
        .orderType(orderType)
        .price(price)
        .stopPrice(stopPrice)
        .timeInForce(timeInForce)
        .expireTime(expireTime)
        .strictValidate(strictValidate)
        .postOnly(postOnly)
        .takeRate(takeRate)
        .makeRate(makeRate);
    createSpotOrder(paramsBuilder, callback);
  }

  @Override
  public void createSpotOrder(
      ParamsBuilder paramsBuilder,
      Callback<Report> callback) {
    Interceptor interceptor = (callback == null)
        ? null
        : InterceptorFactory.newOfWSResponseObject(callback, Report.class);
    sendById("spot_new_order", paramsBuilder.buildObjectMap(), interceptor);
  }

  @Override
  public void createSpotOrderList(
      ContingencyType contingencyType,
      List<Order> orders,
      String orderListID,
      Callback<List<Report>> callback) {
    ParamsBuilder params = new ParamsBuilder()
        .orderListID(orderListID)
        .contingencyType(contingencyType)
        .orders(orders);
    Interceptor interceptor = (callback == null)
        ? null
        : InterceptorFactory.newOfWSResponseList(callback, Report.class);
    sendById("cancel_spot_order", params.buildObjectMap(), interceptor);

  }

  @Override
  public void cancelSpotOrder(String clientOrderID, Callback<Report> callback) {
    ParamsBuilder params = new ParamsBuilder().clientOrderID(clientOrderID);
    Interceptor interceptor = (callback == null)
        ? null
        : InterceptorFactory.newOfWSResponseObject(callback, Report.class);
    sendById("spot_cancel_order", params.buildObjectMap(), interceptor);
  }

  @Override
  public void replaceSpotOrder(String clientOrderID, String newClientOrderID, String quantity, String price,
      Boolean strictValidate, Callback<Report> callback) {
    ParamsBuilder paramsBuilder = new ParamsBuilder()
        .clientOrderID(clientOrderID)
        .newClientOrderID(newClientOrderID)
        .quantity(quantity)
        .price(price)
        .strictValidate(strictValidate);
    Interceptor interceptor = (callback == null)
        ? null
        : InterceptorFactory.newOfWSResponseObject(callback, Report.class);
    sendById("spot_replace_order", paramsBuilder.buildObjectMap(), interceptor);
  }

  @Override
  public void cancelAllSpotOrders(Callback<List<Report>> callback) {
    Interceptor interceptor = (callback == null)
        ? null
        : InterceptorFactory.newOfWSResponseList(callback, Report.class);
    sendById("spot_cancel_orders", null, interceptor);
  }

  @Override
  public void getSpotTradingBalances(Callback<List<Balance>> callback) {
    Interceptor interceptor = (callback == null)
        ? null
        : InterceptorFactory.newOfWSResponseList(callback, Balance.class);
    sendById("spot_balances", null, interceptor);
  }

  @Override
  public void getSpotTradingBalanceOfSymbol(String symbol, Callback<Balance> callback) {
    Interceptor interceptor = (callback == null)
        ? null
        : InterceptorFactory.newOfWSResponseObject(callback, Balance.class);
    sendById("spot_balance", null, interceptor);
  }

  @Override
  public void getSpotCommissions(Callback<List<Commission>> callback) {
    Interceptor interceptor = (callback == null)
        ? null
        : InterceptorFactory.newOfWSResponseList(callback, Commission.class);
    sendById("spot_fees", null, interceptor);
  }

  @Override
  public void getSpotCommissionOfSymbol(String symbol, Callback<Commission> callback) {
    ParamsBuilder paramsBuilder = new ParamsBuilder().symbol(symbol);
    Interceptor interceptor = (callback == null)
        ? null
        : InterceptorFactory.newOfWSResponseObject(callback, Commission.class);
    sendById("spot_fee", paramsBuilder.buildObjectMap(), interceptor);
  }

}
