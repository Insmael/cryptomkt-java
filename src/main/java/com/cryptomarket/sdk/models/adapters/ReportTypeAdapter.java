package com.cryptomarket.sdk.models.adapters;

import com.cryptomarket.sdk.models.ReportType;
import com.squareup.moshi.FromJson;

public class ReportTypeAdapter {
  @FromJson
  ReportType fromJson(String str) {
    return ReportType.valueOf(str.toUpperCase());
  }

}
