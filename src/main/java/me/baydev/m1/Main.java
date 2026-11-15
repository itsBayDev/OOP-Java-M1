package me.baydev.m1;

import me.baydev.m1.cli.HotelCli;
import me.baydev.m1.repository.RoomCsvRepository;
import me.baydev.m1.service.ReservationService;

import java.io.IOException;
import java.nio.file.Path;

public final class Main {
  private Main() {
  }

  public static void main(String[] args) {
    Path dataFile = args.length > 0 ? Path.of(args[0]) : Path.of("src/main/resources/rooms.csv");
    try {
      var rooms = new RoomCsvRepository().load(dataFile);
      new HotelCli(new ReservationService(rooms)).run();
    } catch (IOException | IllegalArgumentException exception) {
      System.err.println("Unable to start the hotel reservation system: " + exception.getMessage());
    }
  }
}
