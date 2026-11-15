# Hotel Reservation System

A small Java CLI for managing hotel room reservations. It loads the initial room inventory from
`src/main/resources/rooms.csv`, supports booking and cancellation, prevents overlapping stays,
and calculates the total price for each reservation.

## Run

With Maven installed:

```bash
mvn test
mvn package
java -cp target/classes me.baydev.m1.Main
```

An alternate CSV path can be supplied as the first argument:

```bash
java -cp target/classes me.baydev.m1.Main path/to/rooms.csv
```

## Features

- List standard rooms and suites loaded from CSV.
- Book a room for a guest and a date range.
- Reject invalid date ranges, unknown rooms, and conflicting reservations.
- Cancel reservations and list current reservations.
- Calculate totals by number of nights.

## Main Classes

- `Main`: loads the room data and starts the CLI.
- `HotelCli`: presents the menu and reads user input.
- `ReservationService`: enforces availability, booking, and cancellation rules.
- `RoomCsvRepository`: parses the CSV room inventory.
- `Room`: abstract base class containing shared room state and pricing behavior.
- `StandardRoom` and `Suite`: room categories with different polymorphic pricing.
- `Reservation`: represents a guest stay and calculates its total price.
- `ReservationException` and `RoomNotAvailableException`: report business errors explicitly.

## Technical Decisions

- Stays use half-open `LocalDate` ranges: check-in is included and check-out is excluded,
  allowing a new guest to check in on the same day another guest checks out.
- `List` stores rooms and reservations because display order matters and reservations are sorted
  by check-in date when displayed.
- Checked reservation exceptions force the CLI to handle booking and cancellation failures
  explicitly instead of silently ignoring them.
- `Room` is abstract because all room categories share state such as room number and base price,
  while each category provides its own nightly pricing behavior.

## Design and SOLID Principles

- **Single Responsibility (SRP):** `HotelCli` handles I/O, `RoomCsvRepository` handles CSV
  parsing, and `ReservationService` enforces booking rules.
- **Open/Closed (OCP):** New room categories can be added by subclassing `Room` without changing
  reservation or total-price calculation logic.
- **Liskov Substitution (LSP):** `StandardRoom` and `Suite` can be used wherever a `Room` is
  expected without breaking pricing or availability checks.
- `StandardRoom` and `Suite` demonstrate inheritance and polymorphism through their distinct
  `calculateNightlyPrice` implementations.
- `Reservation` validates its invariants.
- `ReservationException` is the custom checked exception for business failures, with
  `RoomNotAvailableException` as a specific case.

The application starts by reading the CSV file rather than relying on hard-coded room objects.
JUnit tests cover normal bookings, totals, polymorphic suite pricing, adjacent stays, conflicts,
and invalid dates.
