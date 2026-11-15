package me.baydev.m1.repository;

import me.baydev.m1.model.Room;
import me.baydev.m1.model.StandardRoom;
import me.baydev.m1.model.Suite;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public final class RoomCsvRepository {
  public List<Room> load(Path path) throws IOException {
    if (!Files.exists(path)) {
      throw new IOException("Data file not found: " + path);
    }
    try (var lines = Files.lines(path)) {
      return lines.filter(line -> !line.isBlank() && !line.trim().startsWith("#"))
          .skip(1)
          .map(this::parse)
          .toList();
    }
  }

  private Room parse(String line) {
    String[] fields = line.split(",");
    if (fields.length != 3) {
      throw new IllegalArgumentException("Invalid room row: " + line);
    }
    int number = Integer.parseInt(fields[0].trim());
    BigDecimal rate = new BigDecimal(fields[2].trim());
    return switch (fields[1].trim().toUpperCase()) {
      case "STANDARD" -> new StandardRoom(number, rate);
      case "SUITE" -> new Suite(number, rate);
      default -> throw new IllegalArgumentException("Unknown room type: " + fields[1]);
    };
  }
}
