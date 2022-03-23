package com.cryptomarket.params;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cryptomarket.sdk.exceptions.CryptomarketArgumentException;

import org.jetbrains.annotations.Nullable;

public class ParamsBuilder {
  private Map<String, String> params;

  public ParamsBuilder() {
    params = new HashMap<String, String>();
  }

  public String remove(String argName) {
    return params.remove(argName);
  }

  static private String fromSnakeCaseToCamelCase(String arg) {
    String[] parts = arg.split("_", 0);
    StringBuilder builder = new StringBuilder();
    for (int i = 0; i < parts.length; i++) {
      if (i == 0) {
        builder.append(parts[i]);
        continue;
      }
      builder.append(parts[i].substring(0, 1).toUpperCase());
      builder.append(parts[i].substring(1));
    }
    return builder.toString();
  }

  public ParamsBuilder checkRequired(List<String> requiredParams) throws CryptomarketArgumentException {
    List<String> missing = new ArrayList<String>();
    requiredParams.forEach(required -> {
      if (!params.containsKey(required)) {
        missing.add(required);
      }
    });
    if (missing.size() > 0) {
      missing.replaceAll(ParamsBuilder::fromSnakeCaseToCamelCase);
      throw new CryptomarketArgumentException("Missing required parameters: "+String.join(", ", missing));
    }
    return this;
  }

  public Map<String, String> build() {
    return this.params;
  }

  private ParamsBuilder addList(String key, @Nullable List<String> list) {
    if (list != null && list.size() > 0)
      params.put(key, String.join(",", list));
    return this;
  }

  private ParamsBuilder addEnumList(String key, @Nullable List<? extends Enum> list) {
    List<String> strList = new ArrayList<>();
    if (list != null && list.size() > 0) {
      list.forEach(elem -> strList.add(elem.name()));
    }
    return this.addList(key, strList);
  }

  private ParamsBuilder addArg(String key, @Nullable Boolean arg) {
    if (arg != null)
      params.put(key, arg.toString());
    return this;
  }

  private ParamsBuilder addArg(String key, @Nullable String arg) {
    if (arg != null)
      params.put(key, arg);
    return this;
  }

  private ParamsBuilder addArg(String key, @Nullable Integer arg) {
    if (arg != null)
      params.put(key, arg.toString());
    return this;
  }

  private ParamsBuilder addArg(String key, @Nullable Enum arg) {
    if (arg != null)
      params.put(key, arg.toString());
    return this;
  }

  public ParamsBuilder currencies(@Nullable List<String> arg) {
    return this.addList(ArgNames.CURRENCIES, arg);
  }

  public ParamsBuilder symbols(@Nullable List<String> arg) {
    return this.addList(ArgNames.SYMBOLS, arg);
  }

  public ParamsBuilder from(@Nullable String arg) {
    return this.addArg(ArgNames.FROM, arg);
  }

  public ParamsBuilder till(@Nullable String arg) {
    return this.addArg(ArgNames.TILL, arg);
  }

  //TODO leave only one limit. string or integer, not both.
  public ParamsBuilder limit(@Nullable Integer arg) {
    return this.addArg(ArgNames.LIMIT, arg);
  }

  public ParamsBuilder limit(@Nullable String arg) {
    return this.addArg(ArgNames.LIMIT, arg);
  }

  public ParamsBuilder offset(@Nullable Integer arg) {
    return this.addArg(ArgNames.OFFSET, arg);
  }

  public ParamsBuilder sort(@Nullable Sort arg) {
    return this.addArg(ArgNames.SORT, arg);
  }

  public ParamsBuilder by(@Nullable SortBy arg) {
    return this.addArg(ArgNames.BY, arg);
  }

  public ParamsBuilder to(@Nullable String arg) {
    return this.addArg(ArgNames.TO, arg);
  }

  public ParamsBuilder since(@Nullable String arg) {
    return this.addArg(ArgNames.SINCE, arg);
  }

  public ParamsBuilder until(@Nullable String arg) {
    return this.addArg(ArgNames.UNTIL, arg);
  }

  public ParamsBuilder period(@Nullable Period arg) {
    return this.addArg(ArgNames.PERIOD, arg);
  }

  public ParamsBuilder symbol(String arg) {
    return this.addArg(ArgNames.SYMBOL, arg);
  }

  public ParamsBuilder side(Side arg) {
    return this.addArg(ArgNames.SIDE, arg);
  }

  public ParamsBuilder quantity(String arg) {
    return this.addArg(ArgNames.QUANTITY, arg);
  }

  public ParamsBuilder clientOrderID(String arg) {
    return this.addArg(ArgNames.CLIENT_ORDER_ID, arg);
  }

  public ParamsBuilder orderType(OrderType arg) {
    return this.addArg(ArgNames.ORDER_TYPE, arg);
  }

  public ParamsBuilder price(String arg) {
    return this.addArg(ArgNames.PRICE, arg);
  }

  public ParamsBuilder stopPrice(String arg) {
    return this.addArg(ArgNames.STOP_PRICE, arg);
  }

  public ParamsBuilder timeInForce(TimeInForce arg) {
    return this.addArg(ArgNames.TIME_IN_FORCE, arg);
  }

  public ParamsBuilder expireTime(String arg) {
    return this.addArg(ArgNames.EXPIRE_TIME, arg);
  }

  public ParamsBuilder strictValidate(Boolean arg) {
    return this.addArg(ArgNames.STRICT_VALIDATE, arg);
  }

  public ParamsBuilder postOnly(Boolean arg) {
    return this.addArg(ArgNames.POST_ONLY, arg);
  }

  public ParamsBuilder takeRate(String arg) {
    return this.addArg(ArgNames.TAKE_RATE, arg);
  }

  public ParamsBuilder makeRate(String arg) {
    return this.addArg(ArgNames.MAKE_RATE, arg);
  }

  public ParamsBuilder newClientOrderID(String arg) {
    return this.addArg(ArgNames.NEW_CLIENT_ORDER_ID, arg);
  }

  public ParamsBuilder currency(String arg) {
    return this.addArg(ArgNames.CURRENCY, arg);
  }

  public ParamsBuilder address(String arg) {
    return this.addArg(ArgNames.ADDRESS, arg);
  }

  public ParamsBuilder amount(String arg) {
    return this.addArg(ArgNames.AMOUNT, arg);
  }

  public ParamsBuilder paymentID(String arg) {
    return this.addArg(ArgNames.PAYMENT_ID, arg);
  }

  public ParamsBuilder includeFee(Boolean arg) {
    return this.addArg(ArgNames.INCLUDE_FEE, arg);
  }

  public ParamsBuilder autoCommit(Boolean arg) {
    return this.addArg(ArgNames.AUTO_COMMIT, arg);
  }

  public ParamsBuilder useOffchain(UseOffchain arg) {
    return this.addArg(ArgNames.USE_OFFCHAIN, arg);
  }

  public ParamsBuilder publicComment(String arg) {
    return this.addArg(ArgNames.PUBLIC_COMMENT, arg);
  }

  public ParamsBuilder source(AccountType arg) {
    return this.addArg(ArgNames.SOURCE, arg);
  }

  public ParamsBuilder destination(AccountType arg) {
    return this.addArg(ArgNames.DESTINATION, arg);
  }

  public ParamsBuilder types(List<TransactionType> arg) {
    return this.addEnumList(ArgNames.TYPES, arg);
  }

  public ParamsBuilder subtypes(List<TransactionSubtype> arg) {
    return this.addEnumList(ArgNames.SUBTYPES, arg);
  }

  public ParamsBuilder statuses(List<? extends Enum> arg) {
    return this.addEnumList(ArgNames.STATUSES, arg);
  }

  public ParamsBuilder transactionIDs(List<String> arg) {
    return this.addList(ArgNames.TRANSACTION_IDS, arg);
  }

  public ParamsBuilder IDFrom(Integer arg) {
    return this.addArg(ArgNames.ID_FROM, arg);
  }

  public ParamsBuilder IDTill(Integer arg) {
    return this.addArg(ArgNames.ID_TILL, arg);
  }

  public ParamsBuilder showSenders(Boolean arg) {
    return this.addArg(ArgNames.SHOW_SENDERS, arg);
  }

  public ParamsBuilder baseCurrency(String arg) {
    return this.addArg(ArgNames.BASE_CURRENCY, arg);
  }

  public ParamsBuilder activeAt(String arg) {
    return this.addArg(ArgNames.ACTIVE_AT, arg);
  }

  public ParamsBuilder transactionID(String arg) {
    return this.addArg(ArgNames.TRANSACTION_ID, arg);
  }

  public ParamsBuilder active(Boolean arg) {
    return this.addArg(ArgNames.ACTIVE, arg);
  }

  public ParamsBuilder volume(Integer arg) {
    return this.addArg(ArgNames.VOLUME, arg);
  }

  public ParamsBuilder fromCurrency(String arg) {
    return this.addArg(ArgNames.FROM_CURRENCY, arg);
  }

  public ParamsBuilder toCurrency(String arg) {
    return this.addArg(ArgNames.TO_CURRENCY, arg);
  }

  public ParamsBuilder transferBy(IdentifyBy arg) {
    return this.addArg(ArgNames.TRANSFER_BY, arg);
  }

  public ParamsBuilder identifier(String arg) {
    return this.addArg(ArgNames.IDENTIFIER, arg);
  }

}
