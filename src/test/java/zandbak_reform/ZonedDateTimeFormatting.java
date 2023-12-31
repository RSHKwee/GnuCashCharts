package zandbak_reform;

import java.time.ZonedDateTime;
import java.time.chrono.ChronoZonedDateTime;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class ZonedDateTimeFormatting {
  public static void main(String[] args) {
    ZonedDateTime zonedDateTime = ZonedDateTime.now(ZoneId.of("Europe/Amsterdam"));
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    String formattedDateTime = zonedDateTime.format(formatter);
    System.out.println("Formatted date and time1: " + formattedDateTime);
    LocalDate date = LocalDate.parse("2023-08-20");
    // LocalDate date = LocalDate.now();
    DateTimeFormatter lformatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    String formattedLocalDate = date.format(lformatter);
    System.out.println("Formatted date and time local: " + formattedLocalDate);

    lformatter = DateTimeFormatter.ofPattern("MMMyy");
    formattedLocalDate = date.format(lformatter);
    System.out.println("Formatted date month year: " + formattedLocalDate);

    try {
      boolean tmp = zonedDateTime.isAfter(ChronoZonedDateTime.from(date.atStartOfDay()));
      String formattedDateTime1 = zonedDateTime.format(formatter);
      System.out.println("Formatted date and time2: " + formattedDateTime1 + " " + tmp);
    } catch (DateTimeException e) {
      zonedDateTime = date.atStartOfDay(ZoneId.systemDefault());
      boolean tmp = zonedDateTime.isAfter(ChronoZonedDateTime.from(zonedDateTime));
      String formattedDateTime1 = zonedDateTime.format(formatter);
      System.out.println("Formatted date and time3: " + formattedDateTime1 + " " + tmp);
    }
  }
}
