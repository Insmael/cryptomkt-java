package com.cryptomarket.sdk.models;

import com.squareup.moshi.Json;

public class Airdrop {
  @Json(name = "id")
  private long ID;
  @Json(name = "created_at")
  private String createdAt;
  @Json(name = "updated_at")
  private String updatedAt;
  private String currency;
  @Json(name = "base_currency")
  private String baseCurrency;
  private String description;
  @Json(name = "start_time")
  private String startTime;
  @Json(name = "end_time")
  private String endTime;
  private String amount;
  private String status;
  @Json(name = "transaction_id")
  private String transactionID;

  public long getID() {
    return ID;
  }

  public void setID(long iD) {
    ID = iD;
  }

  public String getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(String createdAt) {
    this.createdAt = createdAt;
  }

  public String getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(String updatedAt) {
    this.updatedAt = updatedAt;
  }

  public String getCurrency() {
    return currency;
  }

  public void setCurrency(String currency) {
    this.currency = currency;
  }

  public String getBaseCurrency() {
    return baseCurrency;
  }

  public void setBaseCurrency(String baseCurrency) {
    this.baseCurrency = baseCurrency;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getStartTime() {
    return startTime;
  }

  public void setStartTime(String startTime) {
    this.startTime = startTime;
  }

  public String getEndTime() {
    return endTime;
  }

  public void setEndTime(String endTime) {
    this.endTime = endTime;
  }

  public String getAmount() {
    return amount;
  }

  public void setAmount(String amount) {
    this.amount = amount;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getTransactionID() {
    return transactionID;
  }

  public void setTransactionID(String transactionID) {
    this.transactionID = transactionID;
  }

  @Override
  public String toString() {
    return "Airdrop [ID=" + ID + ", amount=" + amount + ", baseCurrency=" + baseCurrency + ", createdAt=" + createdAt
        + ", currency=" + currency + ", description=" + description + ", endTime=" + endTime + ", startTime="
        + startTime + ", status=" + status + ", transactionID=" + transactionID + ", updatedAt=" + updatedAt + "]";
  }
}
