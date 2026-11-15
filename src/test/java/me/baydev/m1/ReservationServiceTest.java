package me.baydev.m1;

import me.baydev.m1.exception.RoomNotAvailableException;
import me.baydev.m1.model.Room;
import me.baydev.m1.model.StandardRoom;
import me.baydev.m1.model.Suite;
import me.baydev.m1.service.ReservationService;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ReservationServiceTest {
  private static final LocalDate JUNE_1 = LocalDate.of(2026, 6, 1);
  private final Room standard = new StandardRoom(101, new BigDecimal("100.00"));
  private final ReservationService service = new ReservationService(List.of(standard));

  @Test
  void booksAndCalculatesTotalForSeveralNights() throws Exception {
    var reservation = service.book("Ada", 101, JUNE_1, JUNE_1.plusDays(3));
    assertEquals(new BigDecimal("300.00"), reservation.totalPrice());
  }

  @Test
  void rejectsOverlappingReservationButAllowsAdjacentStay() throws Exception {
    service.book("Ada", 101, JUNE_1, JUNE_1.plusDays(2));
    assertThrows(RoomNotAvailableException.class,
        () -> service.book("Bob", 101, JUNE_1.plusDays(1), JUNE_1.plusDays(3)));
    service.book("Bob", 101, JUNE_1.plusDays(2), JUNE_1.plusDays(4));
  }

  @Test
  void rejectsInvalidDateRange() {
    assertThrows(IllegalArgumentException.class,
        () -> service.book("Ada", 101, JUNE_1, JUNE_1));
  }

  @Test
  void suiteUsesPolymorphicPremiumPricing() throws Exception {
    var suiteService = new ReservationService(List.of(new Suite(201, new BigDecimal("100.00"))));
    var reservation = suiteService.book("Ada", 201, JUNE_1, JUNE_1.plusDays(2));
    assertEquals(new BigDecimal("250.0000"), reservation.totalPrice());
  }
}
