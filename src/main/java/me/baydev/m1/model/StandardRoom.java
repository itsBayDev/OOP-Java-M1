package me.baydev.m1.model;

import java.math.BigDecimal;

public final class StandardRoom extends Room {
  public StandardRoom(int number, BigDecimal nightlyRate) {
    super(number, nightlyRate);
  }

  @Override
  public String getType() {
    return "Standard";
  }

  @Override
  public BigDecimal calculateNightlyPrice() {
    return getNightlyRate();
  }
}
