package me.baydev.m1.model;

import java.math.BigDecimal;

public final class Suite extends Room {
  private static final BigDecimal PREMIUM = new BigDecimal("1.25");

  public Suite(int number, BigDecimal nightlyRate) {
    super(number, nightlyRate);
  }

  @Override
  public String getType() {
    return "Suite";
  }

  @Override
  public BigDecimal calculateNightlyPrice() {
    return getNightlyRate().multiply(PREMIUM);
  }
}
