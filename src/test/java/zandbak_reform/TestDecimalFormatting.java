package zandbak_reform;

import java.io.File;
import java.time.LocalDate;
import java.util.Locale;

import kwee.gnucashcharts.library.gnuCashDb.ReadGnuCashDB;

public class TestDecimalFormatting {
  public static void main(String[] args) {
    String gcshFileName = "D:\\Users\\René\\SynologyDrive\\Documenten\\Administraties\\Prive_RK.gnucash";
    File gnucashFile = new File(gcshFileName);

    // Optie 1: Gebruik standaard systeem locale
    ReadGnuCashDB reader1 = new ReadGnuCashDB(gnucashFile);

    // Optie 2: Specificeer Nederlandse locale
    ReadGnuCashDB reader2 = new ReadGnuCashDB(gnucashFile, new Locale("nl", "NL"));

    // Optie 3: Specificeer Amerikaanse locale
    ReadGnuCashDB reader3 = new ReadGnuCashDB(gnucashFile, Locale.US);

    // Optie 4: Detecteer automatisch op basis van data
    ReadGnuCashDB reader4 = new ReadGnuCashDB(gnucashFile);
    reader4.detectAndSetLocaleFromData();

    // Output voor alle readers
    LocalDate today = LocalDate.now();
    System.out.println("=== Reader 1 (Systeem locale) ===");
    for (String regel : reader1.getRegels(today)) {
      System.out.println(regel);
    }

    System.out.println("\n=== Reader 4 (Gedetecteerde locale) ===");
    System.out.println("Decimal separator: " + reader4.getDecimalSeparator());
    for (String regel : reader4.getRegels(today)) {
      System.out.println(regel);
    }
  }
}