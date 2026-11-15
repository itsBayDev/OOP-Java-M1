package me.baydev.m1.service;

import me.baydev.m1.exception.ReservationException;
import me.baydev.m1.exception.RoomNotAvailableException;
import me.baydev.m1.model.Reservation;
import me.baydev.m1.model.Room;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public final class ReservationService {
  private final List<Room> rooms;
  private final List<Reservation> reservations = new ArrayList<>();
  private int nextReservationId = 1;

  public ReservationService(List<Room> rooms) {
    if (rooms == null || rooms.isEmpty()) {
      throw new IllegalArgumentException("At least one room is required");
    }
    this.rooms = List.copyOf(rooms);
  }

  public List<Room> listRooms() {
    return rooms;
  }

  public List<Room> availableRooms(LocalDate checkIn, LocalDate checkOut) {
    validateDates(checkIn, checkOut);
    return rooms.stream()
        .filter(room -> reservations.stream().noneMatch(reservation ->
            reservation.getRoom().getNumber() == room.getNumber()
                && reservation.overlaps(checkIn, checkOut)))
        .toList();
  }

  public Reservation book(String guestName, int roomNumber, LocalDate checkIn, LocalDate checkOut)
      throws ReservationException {
    validateDates(checkIn, checkOut);
    Room room = rooms.stream().filter(candidate -> candidate.getNumber() == roomNumber)
        .findFirst().orElseThrow(() -> new ReservationException("Room " + roomNumber + " does not exist"));
    boolean occupied = reservations.stream().anyMatch(reservation ->
        reservation.getRoom().getNumber() == roomNumber && reservation.overlaps(checkIn, checkOut));
    if (occupied) {
      throw new RoomNotAvailableException(roomNumber);
    }
    Reservation reservation = new Reservation(nextReservationId++, guestName, room, checkIn, checkOut);
    reservations.add(reservation);
    return reservation;
  }

  public void cancel(int reservationId) throws ReservationException {
    boolean removed = reservations.removeIf(reservation -> reservation.getId() == reservationId);
    if (!removed) {
      throw new ReservationException("Reservation #" + reservationId + " does not exist");
    }
  }

  public List<Reservation> listReservations() {
    return reservations.stream().sorted(Comparator.comparing(Reservation::getCheckIn)).toList();
  }

  private void validateDates(LocalDate checkIn, LocalDate checkOut) {
    if (checkIn == null || checkOut == null || !checkIn.isBefore(checkOut)) {
      throw new IllegalArgumentException("Check-out must be after check-in");
    }
  }
}
