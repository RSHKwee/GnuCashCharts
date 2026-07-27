package zandbak_reform;

import java.io.File;
import java.time.LocalDate;

import kwee.gnucashcharts.library.AccountDetails;
import kwee.gnucashcharts.library.gnuCashDb.ReadGnuCashDB;

public class DebugHierarchy {
  public static void main(String[] args) {
    String gcshFileName = "D:\\Users\\René\\SynologyDrive\\Documenten\\Administraties\\Prive_RK.gnucash";
    File gnucashFile = new File(gcshFileName);
    ReadGnuCashDB reader = new ReadGnuCashDB(gnucashFile);

    LocalDate today = LocalDate.now();
    AccountDetails root = reader.getAccountHierarchy(today);

    // Debug: Bekijk de structuur
    System.out.println("=== DEBUG INFORMATIE ===");
    System.out.println("Root account: '" + root.get_RootAccount() + "'");
    System.out.println("Aantal kinderen: " + root.get_Children().size());
    System.out.println();

    // Print eerste paar kinderen voor debug
    int count = 0;
    for (AccountDetails child : root.get_Children()) {
      System.out.println("Kind " + (count + 1) + ": '" + child.get_RootAccount() + "'");
      System.out.println("  Is leaf: " + child.is_isLeaf());
      System.out.println("  Aantal kinderen: " + child.get_Children().size());
      count++;
      if (count >= 5)
        break;
    }
    System.out.println();

    // Gebruik de ingebouwde methode
    System.out.println("=== GET HIERARCHY STRING ===");
    System.out.println(reader.getHierarchyString(today));
  }
}
