package com.cryptomarket.params;

public enum AirdropStatus {

  AVAILABLE("available"),
  CLAIMED("claimed"),
  PENDING("pending"),
  COMMITTED("committed");

  private final String label;

  private AirdropStatus(String label) {
      this.label = label;
  }

  public String toString() {
      return label;
  }

}
