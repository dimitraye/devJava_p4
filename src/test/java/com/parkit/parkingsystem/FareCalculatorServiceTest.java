package com.parkit.parkingsystem;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.FareCalculatorService;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

public class FareCalculatorServiceTest {

  private static FareCalculatorService fareCalculatorService;
  private Ticket ticket;

  @BeforeAll
  private static void setUp() {
    fareCalculatorService = new FareCalculatorService();
  }

  @BeforeEach
  private void setUpPerTest() {
    ticket = new Ticket();
  }

  @Test
  public void calculateFareCar() {
    LocalDateTime inTime = LocalDateTime.now().minusHours(1);
    LocalDateTime outTime = LocalDateTime.now();
    ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);

    ticket.setInTime(inTime);
    ticket.setOutTime(outTime);
    ticket.setParkingSpot(parkingSpot);
    fareCalculatorService.calculateFare(ticket);

    long durationInMinutes = ChronoUnit.MINUTES.between(ticket.getInTime(), ticket.getOutTime());
    double expectedFare = Fare.CAR_RATE_PER_HOUR / FareCalculatorService.MINUTES_IN_HOUR * durationInMinutes;
    double actualFare = ticket.getPrice();

    assertEquals(expectedFare, actualFare);
  }

  @Test
  public void calculateFareBike() {
    LocalDateTime inTime = LocalDateTime.now().minusHours(1);
    LocalDateTime outTime = LocalDateTime.now();
    ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);

    ticket.setInTime(inTime);
    ticket.setOutTime(outTime);
    ticket.setParkingSpot(parkingSpot);
    fareCalculatorService.calculateFare(ticket);

    long durationInMinutes = ChronoUnit.MINUTES.between(ticket.getInTime(), ticket.getOutTime());
    double expectedFare = Fare.BIKE_RATE_PER_HOUR / FareCalculatorService.MINUTES_IN_HOUR * durationInMinutes;
    double actualFare = ticket.getPrice();

    assertEquals(expectedFare, actualFare);
  }

  @Test
  public void calculateFareUnkownType() {
    LocalDateTime inTime = LocalDateTime.now().minusHours(1);
    LocalDateTime outTime = LocalDateTime.now();
    ParkingSpot parkingSpot = new ParkingSpot(1, null, false);

    ticket.setInTime(inTime);
    ticket.setOutTime(outTime);
    ticket.setParkingSpot(parkingSpot);
    assertThrows(NullPointerException.class, () -> fareCalculatorService.calculateFare(ticket));
  }

  @Test
  public void calculateFareBikeWithFutureInTime() {
    LocalDateTime inTime = LocalDateTime.now().plusHours(1);
    LocalDateTime outTime = LocalDateTime.now();
    ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);

    ticket.setInTime(inTime);
    ticket.setOutTime(outTime);
    ticket.setParkingSpot(parkingSpot);
    assertThrows(IllegalArgumentException.class, () -> fareCalculatorService.calculateFare(ticket));
  }

  @Test
  public void calculateFareBikeWithLessThanOneHourParkingTime() {
    LocalDateTime inTime = LocalDateTime.now().minusMinutes(45);
    LocalDateTime outTime = LocalDateTime.now();
    ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);

    ticket.setInTime(inTime);
    ticket.setOutTime(outTime);
    ticket.setParkingSpot(parkingSpot);
    fareCalculatorService.calculateFare(ticket);
    assertEquals((0.75 * Fare.BIKE_RATE_PER_HOUR), ticket.getPrice());
  }

  @Test
  public void calculateFareCarWithLessThanOneHourParkingTime() {
    LocalDateTime inTime = LocalDateTime.now().minusMinutes(45);
    LocalDateTime outTime = LocalDateTime.now();
    ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);

    ticket.setInTime(inTime);
    ticket.setOutTime(outTime);
    ticket.setParkingSpot(parkingSpot);
    fareCalculatorService.calculateFare(ticket);
    assertEquals((0.75 * Fare.CAR_RATE_PER_HOUR), ticket.getPrice());
  }

  @Test
  public void calculateFareCarWithMoreThanADayParkingTime() {
    LocalDateTime inTime = LocalDateTime.now().minusHours(24);
    LocalDateTime outTime = LocalDateTime.now();
    ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);

    ticket.setInTime(inTime);
    ticket.setOutTime(outTime);
    ticket.setParkingSpot(parkingSpot);
    fareCalculatorService.calculateFare(ticket);
    assertEquals((24 * Fare.CAR_RATE_PER_HOUR), ticket.getPrice());
  }

  @Test
  public void calculateFareWithLessThanThirtyMinutesParkingTime() {
    LocalDateTime inTime = LocalDateTime.now().minusMinutes(25);
    LocalDateTime outTime = LocalDateTime.now();
    ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);

    ticket.setInTime(inTime);
    ticket.setOutTime(outTime);
    ticket.setParkingSpot(parkingSpot);
    fareCalculatorService.calculateFare(ticket);
    assertEquals(Fare.RATE_UNDER_HALF_HOUR, ticket.getPrice());
  }

  @Test
  public void calculateFareWithMoreThanADayParkingTimeForRecurrentClient() {
    LocalDateTime inTime = LocalDateTime.now().minusHours(24);
    LocalDateTime outTime = LocalDateTime.now();
    ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
    ticket.setClient(true);
    ticket.setInTime(inTime);
    ticket.setOutTime(outTime);
    ticket.setParkingSpot(parkingSpot);
    fareCalculatorService.calculateFare(ticket);

    assertEquals((24 * Fare.CAR_RATE_PER_HOUR * fareCalculatorService.REDUCTION_CLIENT),
        ticket.getPrice());
  }
}
