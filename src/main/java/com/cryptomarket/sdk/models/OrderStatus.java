package com.cryptomarket.sdk.models;

public enum OrderStatus {

  NEW("new"),
  SUSPENDED("suspended"),
  PARTIALLY_FILLED("partiallyFilled"),
  FILLED("filled"),
  CANCELED("canceled"),
  EXPIRED("expired");

  private final String label;

  private OrderStatus(String label) {
    this.label = label;
  }

  public String toString() {
    return label;
  }
}
