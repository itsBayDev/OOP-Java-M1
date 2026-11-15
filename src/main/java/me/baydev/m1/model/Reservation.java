package me.baydev.m1.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public final class Reservation {
  private final int id;
  private final String guestName;
  private final Room room;
  private final LocalDate checkIn;
  private final LocalDate checkOut;

  public Reservation(int id, String guestName, Room room, LocalDate checkIn, LocalDate checkOut) {
    if (id <= 0 || guestName == null || guestName.isBlank() || room == null
        || checkIn == null || checkOut == null || !checkIn.isBefore(checkOut)) {
      throw new IllegalArgumentException("Invalid reservation details");
    }
    this.id = id;
    this.guestName = guestName.trim();
    this.room = room;
    this.checkIn = checkIn;
    this.checkOut = checkOut;
  }

  public int getId() {
    return id;
  }

  public String getGuestName() {
    return guestName;
  }

  public Room getRoom() {
    return room;
  }

  public LocalDate getCheckIn() {
    return checkIn;
  }

  public LocalDate getCheckOut() {
    return checkOut;
  }

  public long nights() {
    return ChronoUnit.DAYS.between(checkIn, checkOut);
  }

  public BigDecimal totalPrice() {
    return room.calculateNightlyPrice().multiply(BigDecimal.valueOf(nights()));
  }

  public boolean overlaps(LocalDate requestedCheckIn, LocalDate requestedCheckOut) {
    return checkIn.isBefore(requestedCheckOut) && requestedCheckIn.isBefore(checkOut);
  }

  @Override
  public String toString() {
    return "#" + id + " - " + guestName + ", room " + room.getNumber() + ", "
        + checkIn + " to " + checkOut + ", " + totalPrice() + " EUR";
  }
}
