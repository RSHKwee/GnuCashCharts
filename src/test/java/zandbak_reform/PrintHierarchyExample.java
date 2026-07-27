package zandbak_reform;

import java.io.File;
import java.time.LocalDate;

import kwee.gnucashcharts.library.AccountDetails;
import kwee.gnucashcharts.library.gnuCashDb.ReadGnuCashDB;

public class PrintHierarchyExample {
  public static void main(String[] args) {
    String gcshFileName = "D:\\Users\\René\\SynologyDrive\\Documenten\\Administraties\\Prive_RK.gnucash";
    File gnucashFile = new File(gcshFileName);
    // Lees GnuCash bestand

    ReadGnuCashDB reader = new ReadGnuCashDB(gnucashFile);

    // Krijg hiërarchie voor vandaag
    LocalDate today = LocalDate.now();
    AccountDetails root = reader.getAccountHierarchy(today);

    // Print de hiërarchie
    System.out.println("=== Account Hiërarchie ===");
    printHierarchy(root, 0);
  }

  /**
   * Print de hiërarchie met inspringing
   */
  private static void printHierarchy(AccountDetails node, int level) {
    // Maak inspringing
    String indent = "  ".repeat(level);

    // Print de huidige node (alleen als het geen root is)
    if (!node.get_RootAccount().equals("GnuCash")
        && !node.get_RootAccount().equals("GnuCash " + LocalDate.now().toString())) {

      System.out.print(indent + "├─ " + node.get_RootAccount());

      // Toon extra details voor leaf nodes
      if (node.is_isLeaf()) {
        if (node.get_AccountNr() != null && !node.get_AccountNr().isEmpty()) {
          System.out.print(" (" + node.get_AccountNr() + ")");
        }
        if (node.get_Saldo() != null) {
          System.out.print(" - € " + node.get_Saldo().toPlainString().replace(".", ","));
        }
      }
      System.out.println();
    }

    // Print alle kinderen
    if (node.get_Children() != null) {
      for (AccountDetails child : node.get_Children()) {
        printHierarchy(child, level + 1);
      }
    }

  }
}
