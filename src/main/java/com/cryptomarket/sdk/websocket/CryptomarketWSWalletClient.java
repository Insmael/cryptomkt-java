package com.cryptomarket.sdk.websocket;

import java.util.List;

import com.cryptomarket.params.ParamsBuilder;
import com.cryptomarket.params.Sort;
import com.cryptomarket.params.SortBy;
import com.cryptomarket.params.TransactionStatus;
import com.cryptomarket.params.TransactionSubtype;
import com.cryptomarket.params.TransactionType;
import com.cryptomarket.sdk.Callback;
import com.cryptomarket.sdk.models.Balance;
import com.cryptomarket.sdk.models.Transaction;

import org.jetbrains.annotations.Nullable;

/**
 * CryptomarketWSAccountClient connects via websocket to cryptomarket to get
 * account information of the user. uses SHA256 as auth method and authenticates
 * on connection.
 */
public interface CryptomarketWSWalletClient extends CryptomarketWS {
  public void subscribeToTransactions(Callback<Transaction> callback, @Nullable Callback<Boolean> resultCallback);

  public void unsubscribeToTransactions(@Nullable Callback<Boolean> callback);

  public void subscribeToWalletBalances(Callback<List<Balance>> callback, @Nullable Callback<Boolean> resultCallback);

  public void unsubscribeToWalletBalances(@Nullable Callback<Boolean> callback);

  public void getWalletBalances(Callback<List<Balance>> callback);

  public void getWalletBalance(String currency, Callback<Balance> callback);

  public void getTransactions(
      Callback<List<Transaction>> callback,
      @Nullable List<TransactionType> types,
      @Nullable List<TransactionSubtype> subtypes,
      @Nullable List<TransactionStatus> statuses,
      @Nullable List<String> currencies,
      @Nullable List<String> transactionIDs,
      @Nullable Sort sort,
      @Nullable SortBy by,
      @Nullable String from,
      @Nullable String till,
      @Nullable Integer IDFrom,
      @Nullable Integer IDTill,
      @Nullable Integer limit,
      @Nullable Integer offset);

  public void getTransactions(
      ParamsBuilder paramsBuilder,
      Callback<List<Transaction>> callback);
}
