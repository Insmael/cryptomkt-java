package com.cryptomarket.sdk.websocket;

import java.util.List;

import com.cryptomarket.params.ContingencyType;
import com.cryptomarket.params.OrderType;
import com.cryptomarket.params.ParamsBuilder;
import com.cryptomarket.params.Side;
import com.cryptomarket.params.TimeInForce;
import com.cryptomarket.sdk.Callback;
import com.cryptomarket.sdk.models.Balance;
import com.cryptomarket.sdk.models.Commission;
import com.cryptomarket.sdk.models.Order;
import com.cryptomarket.sdk.models.Report;

import org.jetbrains.annotations.Nullable;

/**
 * TradingClient connects via websocket to cryptomarket to enable the user to
 * manage orders. uses SHA256 as auth method and authenticates on connection.
 */
public interface CryptomarketWSTradingClient extends CryptomarketWS {

  public void subscribeToReports(Callback<Report> callback, @Nullable Callback<Boolean> resultCallback);

  public void unsubscribeToReports(Callback<Boolean> callback);

  public void getActiveOrders(Callback<List<Report>> callback);

  public void createSpotOrder(
      String symbol,
      Side side,
      String quantity,
      @Nullable String clientOrderID,
      @Nullable OrderType orderType,
      @Nullable String price,
      @Nullable String stopPrice,
      @Nullable TimeInForce timeInForce,
      @Nullable String expireTime,
      @Nullable Boolean strictValidate,
      @Nullable Boolean postOnly,
      @Nullable String takeRate,
      @Nullable String makeRate,
      @Nullable Callback<Report> callback);

  public void createSpotOrder(
      ParamsBuilder paramsBuilder,
      @Nullable Callback<Report> callback);

  public void createSpotOrderList(
      @Nullable String orderListID,
      ContingencyType contingencyType,
      List<Order> orders,
      Callback<List<Report>> callback); // TODO check the order type class for building the query

  public void cancelOrder(String clientOrderID, Callback<Report> callback);

  public void replaceOrder(
      String clientOrderID,
      String newClientOrderID,
      @Nullable String quantity,
      @Nullable String price,
      @Nullable Boolean strictValidate,
      Callback<Report> callback);

  public void cancelAllSpotOrders(
      Callback<List<Report>> callback);

  public void getSpotTradingBalances(Callback<List<Balance>> callback);

  public void getSpotTradingBalance(String symbol, Callback<Balance> callback);

  public void getSpotCommissions(Callback<List<Commission>> callback);

  public void getSpotCommission(String symbol, Callback<Commission> callback);

}
