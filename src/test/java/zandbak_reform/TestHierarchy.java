package zandbak_reform;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;

import kwee.gnucashcharts.library.AccountDetails;
import kwee.gnucashcharts.library.gnuCashDb.ReadGnuCashDB;

public class TestHierarchy {
  public static void main(String[] args) {
    String gcshFileName = "D:\\Users\\René\\SynologyDrive\\Documenten\\Administraties\\Prive_RK.gnucash";
    File gnucashFile = new File(gcshFileName);
    ReadGnuCashDB reader = new ReadGnuCashDB(gnucashFile);

    LocalDate today = LocalDate.now();

    // Optie 1: Print hiërarchie
    System.out.println("=== Account Hiërarchie ===");
    System.out.println(reader.getHierarchyString(today));

    // Optie 2: Krijg root node voor verdere verwerking
    AccountDetails root = reader.getAccountHierarchy(today);

    // Optie 3: Platte lijst (backward compatibility)
    ArrayList<AccountDetails> flatList = reader.getAccDets(today);

    // Optie 4: CSV output
    ArrayList<String> csvLines = reader.getRegels(today);
  }
}
