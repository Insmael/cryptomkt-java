package com.cryptomarket.sdk.models.adapters;

import com.cryptomarket.sdk.Utils;
import com.cryptomarket.sdk.models.OrderStatus;
import com.squareup.moshi.FromJson;

public class OrderStatusAdapter {
  @FromJson
  OrderStatus fromJson(String str) {
    return OrderStatus.valueOf(Utils.fromCamelCaseToCapSnakeCase(str));
  }
}
