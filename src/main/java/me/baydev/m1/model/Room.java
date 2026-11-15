package me.baydev.m1.model;

import java.math.BigDecimal;

public abstract class Room {
  private final int number;
  private final BigDecimal nightlyRate;

  protected Room(int number, BigDecimal nightlyRate) {
    if (number <= 0 || nightlyRate == null || nightlyRate.signum() <= 0) {
      throw new IllegalArgumentException("Room number and nightly rate must be positive");
    }
    this.number = number;
    this.nightlyRate = nightlyRate;
  }

  public int getNumber() {
    return number;
  }

  public BigDecimal getNightlyRate() {
    return nightlyRate;
  }

  public abstract String getType();

  public abstract BigDecimal calculateNightlyPrice();

  @Override
  public String toString() {
    return "Room " + number + " (" + getType() + ") - " + calculateNightlyPrice() + " EUR/night";
  }
}
