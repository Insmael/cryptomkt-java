package com.cryptomarket.sdk.models.adapters;

import com.cryptomarket.sdk.Utils;
import com.cryptomarket.sdk.models.OrderType;
import com.squareup.moshi.FromJson;

public class OrderTypeAdapter {
  @FromJson
  OrderType fromJson(String str)  {
    return OrderType.valueOf(Utils.fromCamelCaseToCapSnakeCase(str));
  }
}
