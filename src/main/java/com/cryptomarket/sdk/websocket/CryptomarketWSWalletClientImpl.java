package com.cryptomarket.sdk.websocket;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.cryptomarket.params.ParamsBuilder;
import com.cryptomarket.params.Sort;
import com.cryptomarket.params.SortBy;
import com.cryptomarket.params.TransactionStatus;
import com.cryptomarket.params.TransactionSubtype;
import com.cryptomarket.params.TransactionType;
import com.cryptomarket.sdk.Callback;
import com.cryptomarket.sdk.exceptions.CryptomarketAPIException;
import com.cryptomarket.sdk.exceptions.CryptomarketSDKException;
import com.cryptomarket.sdk.models.Balance;
import com.cryptomarket.sdk.models.ErrorBody;
import com.cryptomarket.sdk.models.Transaction;
import com.cryptomarket.sdk.models.WSJsonResponse;
import com.cryptomarket.sdk.websocket.interceptor.Interceptor;
import com.cryptomarket.sdk.websocket.interceptor.InterceptorFactory;

public class CryptomarketWSWalletClientImpl extends AuthClient implements CryptomarketWSWalletClient {

  public CryptomarketWSWalletClientImpl(String apiKey, String apiSecret) throws IOException {
    super("wss://api.exchange.cryptomkt.com/api/3/ws/wallet", apiKey, apiSecret);
    Map<String, String> subsKeys = this.getSubscritpionKeys();
    // transactions
    subsKeys.put("subscribe_transactions", "transactions");
    subsKeys.put("unsubscribe_transactions", "transactions");
    subsKeys.put("transaction_update", "transactions");
    // balance
    subsKeys.put("subscribe_wallet_balances", "balance");
    subsKeys.put("unsubscribe_wallet_balances", "balance");
    subsKeys.put("wallet_balances", "balance");
    subsKeys.put("wallet_balance_update", "balance");
  }

  @Override
  public void subscribeToTransactions(Callback<Transaction> callback, Callback<Boolean> resultCallback) {
    Interceptor interceptor = new Interceptor() {
      @Override
      public void makeCall(WSJsonResponse response) {
        ErrorBody error = response.getError();
        if (error != null) {
          callback.reject(new CryptomarketAPIException(error));
        } else {
          Transaction transaction = adapter.objectFromValue(response.getParams(), Transaction.class);
          callback.resolve(transaction);
        }
      }
    };
    Interceptor resultInterceptor = (resultCallback == null) ? null
        : InterceptorFactory.newOfWSResponseObject(resultCallback, Boolean.class);
    sendSubscription("subscribe_transactions", null, interceptor, resultInterceptor);
  }

  @Override
  public void unsubscribeToTransactions(Callback<Boolean> callback) {
    Interceptor interceptor = (callback == null) ? null
        : InterceptorFactory.newOfWSResponseObject(callback, Boolean.class);
    sendUnsubscription("unsubscribe_transactions", null, interceptor);
  }

  @Override
  public void subscribeToWalletBalances(Callback<List<Balance>> callback, Callback<Boolean> resultCallback) {
    Interceptor interceptor = new Interceptor() {
      @Override
      public void makeCall(WSJsonResponse response) {
        ErrorBody error = response.getError();
        if (error != null) {
          callback.reject(new CryptomarketAPIException(error));
        } else {
          if (response.getMethod().equals("wallet_balances")) {
            List<Balance> balances = adapter.listFromValue(response.getParams(), Balance.class);
            callback.resolve(balances);
          } else if (response.getMethod().equals("wallet_balance_update")) {
            Balance balance = adapter.objectFromValue(response.getParams(), Balance.class);
            callback.resolve(Arrays.asList(balance));
          } else {
            callback.reject(new CryptomarketSDKException("unkown data format"));
          }
        }
      }
    };
    Interceptor resultInterceptor = (resultCallback == null) ? null
        : InterceptorFactory.newOfWSResponseObject(resultCallback, Boolean.class);
    sendSubscription("subscribe_wallet_balances", null, interceptor, resultInterceptor);
  }

  @Override
  public void unsubscribeToWalletBalances(Callback<Boolean> callback) {
    Interceptor interceptor = (callback == null) ? null
        : InterceptorFactory.newOfWSResponseObject(callback, Boolean.class);
    sendUnsubscription("unsubscribe_wallet_balances", null, interceptor);
  }

  @Override
  public void getWalletBalances(Callback<List<Balance>> callback) {
    Interceptor interceptor = (callback == null) ? null
        : InterceptorFactory.newOfWSResponseList(callback, Balance.class);
    sendById("wallet_balances", null, interceptor);
  }

  @Override
  public void getWalletBalance(String currency, Callback<Balance> callback) {
    ParamsBuilder paramsBuilder = new ParamsBuilder().currency(currency);
    Interceptor interceptor = (callback == null) ? null
        : InterceptorFactory.newOfWSResponseObject(callback, Balance.class);
    sendById("wallet_balance", paramsBuilder.buildObjectMap(), interceptor);
  }

  @Override
  public void getTransactions(
      Callback<List<Transaction>> callback,
      List<TransactionType> types,
      List<TransactionSubtype> subtypes,
      List<TransactionStatus> statuses,
      List<String> currencies,
      List<String> transactionIDs,
      Sort sort,
      SortBy by,
      String from,
      String till,
      Integer IDFrom,
      Integer IDTill,
      Integer limit,
      Integer offset) {
    getTransactions(
        new ParamsBuilder()
            .types(types)
            .subtypes(subtypes)
            .statuses(statuses)
            .currencies(currencies)
            .transactionIDs(transactionIDs)
            .sort(sort)
            .by(by)
            .from(from)
            .till(till)
            .IDFrom(IDFrom)
            .IDTill(IDTill)
            .limit(limit)
            .offset(offset),
        callback);
  }

  @Override
  public void getTransactions(ParamsBuilder paramsBuilder, Callback<List<Transaction>> callback) {
    Interceptor interceptor = (callback == null) ? null
        : InterceptorFactory.newOfWSResponseList(callback, Transaction.class);
    sendById("get_transactions", (paramsBuilder == null) ? null : paramsBuilder.buildObjectMap(), interceptor);

  }
}
