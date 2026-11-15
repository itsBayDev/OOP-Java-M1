package me.baydev.m1.exception;

public final class RoomNotAvailableException extends ReservationException {
  public RoomNotAvailableException(int roomNumber) {
    super("Room " + roomNumber + " is not available for those dates");
  }
}
