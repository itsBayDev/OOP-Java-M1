package me.baydev.m1.cli;

import me.baydev.m1.exception.ReservationException;
import me.baydev.m1.model.Reservation;
import me.baydev.m1.service.ReservationService;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public final class HotelCli {
  private final ReservationService service;
  private final Scanner scanner;

  public HotelCli(ReservationService service) {
    this(service, new Scanner(System.in));
  }

  HotelCli(ReservationService service, Scanner scanner) {
    this.service = service;
    this.scanner = scanner;
  }

  public void run() {
    System.out.println("Welcome to Baydev Hotel");
    boolean running = true;
    while (running) {
      printMenu();
      String choice = scanner.nextLine().trim();
      try {
        running = switch (choice) {
          case "1" -> {
            listRooms();
            yield true;
          }
          case "2" -> {
            book();
            yield true;
          }
          case "3" -> {
            cancel();
            yield true;
          }
          case "4" -> {
            listReservations();
            yield true;
          }
          case "0" -> false;
          default -> {
            System.out.println("Unknown option.");
            yield true;
          }
        };
      } catch (ReservationException | IllegalArgumentException exception) {
        System.out.println("Error: " + exception.getMessage());
      }
    }
    System.out.println("Goodbye.");
  }

  private void printMenu() {
    System.out.println("\n1. List rooms\n2. Book a room\n3. Cancel reservation\n4. List reservations\n0. Exit");
    System.out.print("Choose an option: ");
  }

  private void listRooms() {
    service.listRooms().forEach(System.out::println);
  }

  private void book() throws ReservationException {
    System.out.print("Guest name: ");
    String guest = scanner.nextLine();
    System.out.print("Room number: ");
    int room = Integer.parseInt(scanner.nextLine());
    LocalDate checkIn = readDate("Check-in (YYYY-MM-DD): ");
    LocalDate checkOut = readDate("Check-out (YYYY-MM-DD): ");
    Reservation reservation = service.book(guest, room, checkIn, checkOut);
    System.out.println("Booked successfully: " + reservation);
  }

  private void cancel() throws ReservationException {
    System.out.print("Reservation number: ");
    service.cancel(Integer.parseInt(scanner.nextLine()));
    System.out.println("Reservation cancelled.");
  }

  private void listReservations() {
    if (service.listReservations().isEmpty()) {
      System.out.println("No reservations.");
    } else {
      service.listReservations().forEach(System.out::println);
    }
  }

  private LocalDate readDate(String prompt) {
    System.out.print(prompt);
    try {
      return LocalDate.parse(scanner.nextLine());
    } catch (DateTimeParseException exception) {
      throw new IllegalArgumentException("Dates must use YYYY-MM-DD format");
    }
  }
}
