package com.cryptomarket.sdk.models;

public enum TimeInForce {
  GTC,
  IOC,
  FOK,
  DAY {@Override public String toString() {return "Day";}},
  GTD,
}
