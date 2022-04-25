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
 * <p>
 * Requires API keys to make socket calls
 */
public interface CryptomarketWSWalletClient extends CryptomarketWS {

  /**
   * A transaction notification occurs each time a transaction has been changed,
   * such as creating a transaction, updating the pending state (e.g., the hash
   * assigned) or completing a transaction
   * <p>
   * https://api.exchange.cryptomkt.com/#subscribe-to-transactions
   *
   * @param callback       recieves a feed of transactions
   * @param resultCallback Optional. recieves true if the subscription is
   *                       successfull
   *
   */
  public void subscribeToTransactions(Callback<Transaction> callback, @Nullable Callback<Boolean> resultCallback);

  /**
   * stop recieving the feed of transactions
   * <p>
   * https://api.exchange.cryptomkt.com/#subscribe-to-transactions
   *
   * @param callback recieves true if the unsubscription is successfull.
   *
   */
  public void unsubscribeToTransactions(@Nullable Callback<Boolean> callback);

  /**
   * subscribe to a feed of the user's wallet balances
   * <p>
   * only non-zero values are present
   * <p>
   * https://api.exchange.cryptomkt.com/#subscribe-to-wallet-balance
   *
   * @param callback       recieves a feed of a list of balances
   * @param resultCallback Optional. recieves true if the subscription is
   *                       successfull.
   */
  public void subscribeToWalletBalances(Callback<List<Balance>> callback, @Nullable Callback<Boolean> resultCallback);

  /**
   * stop recieving the feed of balances changes
   * <p>
   * https://api.exchange.cryptomkt.com/#subscribe-to-wallet-balance
   *
   * @param callback recieves true is the unsubcription is successfull.
   */
  public void unsubscribeToWalletBalances(@Nullable Callback<Boolean> callback);

  /**
   * Get the user's wallet balance for all currencies with balance
   * <p>
   * https://api.exchange.cryptomkt.com/#wallet-balance
   *
   * @param callback recieves a list of balances
   */
  public void getWalletBalances(Callback<List<Balance>> callback);

  /**
   * Get the user's wallet balance of a currency
   * <p>
   * https://api.exchange.cryptomkt.com/#wallet-balance
   *
   * @param currency The currency code to query the balance
   * @param callback recieves a balance
   */
  public void getWalletBalanceOfCurrency(String currency, Callback<Balance> callback);

  /**
   * Get the transaction history of the account
   * <p>
   * Important:
   * <p>
   * - The list of supported transaction types may be expanded in future versions
   * <p>
   * - Some transaction subtypes are reserved for future use and do not purport to
   * provide any functionality on the platform
   * <p>
   * - The list of supported transaction subtypes may be expanded in future
   * versions
   * <p>
   * https://api.exchange.cryptomkt.com/#get-transactions-history
   *
   * @param callback            recieves a list of transactions
   * @param transactionIDs      Optional. List of transaction identifiers to query
   * @param transactionTypes    Optional. List of types to query. valid types are:
   *                            'DEPOSIT', 'WITHDRAW', 'TRANSFER' and 'SWAP'
   * @param transactionSubtyes  Optional. List of subtypes to query. valid
   *                            subtypes are: 'UNCLASSIFIED', 'BLOCKCHAIN',
   *                            'AIRDROP', 'AFFILIATE', 'STAKING', 'BUY_CRYPTO',
   *                            'OFFCHAIN', 'FIAT', 'SUB_ACCOUNT',
   *                            'WALLET_TO_SPOT', 'SPOT_TO_WALLET',
   *                            'WALLET_TO_DERIVATIVES', 'DERIVATIVES_TO_WALLET',
   *                            'CHAIN_SWITCH_FROM', 'CHAIN_SWITCH_TO' and
   *                            'INSTANT_EXCHANGE'
   * @param transactionStatuses Optional. List of statuses to query. valid
   *                            subtypes are: 'CREATED', 'PENDING', 'FAILED',
   *                            'SUCCESS' and 'ROLLED_BACK'
   * @param orderBy             Optional. sorting parameter.'created_at' or 'id'.
   *                            Default is 'created_at'
   * @param from                Optional. Interval initial value when ordering by
   *                            'created_at'. As Datetime
   * @param till                Optional. Interval end value when ordering by
   *                            'created_at'. As Datetime
   * @param idFrom              Optional. Interval initial value when ordering by
   *                            id. Min is 0
   * @param idTill              Optional. Interval end value when ordering by id.
   *                            Min is 0
   * @param sort                Optional. Sort direction. 'ASC' or 'DESC'. Default
   *                            is 'DESC'
   * @param limit               Optional. Transactions per query. Defaul is 100.
   *                            Max is 1000
   * @param offset              Optional. Default is 0. Max is 100000
   */
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

  /**
   * Get the transaction history of the account
   * <p>
   * Important:
   * <p>
   * - The list of supported transaction types may be expanded in future versions
   * <p>
   * - Some transaction subtypes are reserved for future use and do not purport to
   * provide any functionality on the platform
   * <p>
   * - The list of supported transaction subtypes may be expanded in future
   * versions
   * <p>
   * https://api.exchange.cryptomkt.com/#get-transactions-history
   *
   * @param callback            recieves a list of transactions
   * @param paramsBuilder       takes all other arguments except callback as chain
   *                            methods
   * @param transactionIDs      Optional. List of transaction identifiers to query
   * @param transactionTypes    Optional. List of types to query. valid types are:
   *                            'DEPOSIT', 'WITHDRAW', 'TRANSFER' and 'SWAP'
   * @param transactionSubtyes  Optional. List of subtypes to query. valid
   *                            subtypes are: 'UNCLASSIFIED', 'BLOCKCHAIN',
   *                            'AIRDROP', 'AFFILIATE', 'STAKING', 'BUY_CRYPTO',
   *                            'OFFCHAIN', 'FIAT', 'SUB_ACCOUNT',
   *                            'WALLET_TO_SPOT', 'SPOT_TO_WALLET',
   *                            'WALLET_TO_DERIVATIVES', 'DERIVATIVES_TO_WALLET',
   *                            'CHAIN_SWITCH_FROM', 'CHAIN_SWITCH_TO' and
   *                            'INSTANT_EXCHANGE'
   * @param transactionStatuses Optional. List of statuses to query. valid
   *                            subtypes are: 'CREATED', 'PENDING', 'FAILED',
   *                            'SUCCESS' and 'ROLLED_BACK'
   * @param orderBy             Optional. sorting parameter.'created_at' or 'id'.
   *                            Default is 'created_at'
   * @param from                Optional. Interval initial value when ordering by
   *                            'created_at'. As Datetime
   * @param till                Optional. Interval end value when ordering by
   *                            'created_at'. As Datetime
   * @param idFrom              Optional. Interval initial value when ordering by
   *                            id. Min is 0
   * @param idTill              Optional. Interval end value when ordering by id.
   *                            Min is 0
   * @param sort                Optional. Sort direction. 'ASC' or 'DESC'. Default
   *                            is 'DESC'
   * @param limit               Optional. Transactions per query. Defaul is 100.
   *                            Max is 1000
   * @param offset              Optional. Default is 0. Max is 100000
   */
  public void getTransactions(
    Callback<List<Transaction>> callback,
      ParamsBuilder paramsBuilder);
}
