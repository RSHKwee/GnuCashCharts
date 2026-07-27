package zandbak_reform;

import java.io.File;
import java.time.LocalDate;
import java.util.*;

import kwee.gnucashcharts.library.AccountDetails;
import kwee.gnucashcharts.library.gnuCashDb.ReadGnuCashDB;

public class AccountHierarchyPrinter {

  // Jouw SubAcc klasse
  static class SubAcc {
    String AccountNaam;
    String ParentAccountNaam;

    public SubAcc(String accountNaam, String parentAccountNaam) {
      this.AccountNaam = accountNaam;
      this.ParentAccountNaam = parentAccountNaam;
      // this.Level = level;
    }
  }

  public static void main(String[] args) {
    // Voorbeeld data
    Map<String, SubAcc> Accounts = new HashMap<>();
    String gcshFileName = "D:\\Users\\René\\SynologyDrive\\Documenten\\Administraties\\Prive_RK.gnucash";

    LocalDate toDay = LocalDate.now();

    ReadGnuCashDB rGnuCsh = new ReadGnuCashDB(new File(gcshFileName));
    ArrayList<AccountDetails> accDets = rGnuCsh.getAccDets(toDay);

    accDets.forEach(acc -> {
      Accounts.put(acc.get_AccountName(), new SubAcc(acc.get_AccountName(), acc.get_RootAccount()));
    });

    // Print de hiërarchie
    printHierarchy(Accounts);
  }

  public static void printHierarchy(Map<String, SubAcc> accounts) {
    // Vind alle top-level accounts (parent is leeg)
    List<SubAcc> topLevelAccounts = new ArrayList<>();
    for (SubAcc acc : accounts.values()) {
      if (acc.ParentAccountNaam == null || acc.ParentAccountNaam.isEmpty()) {
        topLevelAccounts.add(acc);
      }
    }

    // Sorteer top-level accounts (optioneel)
    topLevelAccounts.sort(Comparator.comparing(a -> a.AccountNaam));

    // Print elk top-level account en zijn children
    for (SubAcc top : topLevelAccounts) {
      printAccount(accounts, top, 0);
    }
  }

  private static void printAccount(Map<String, SubAcc> accounts, SubAcc account, int indentLevel) {
    // Print indentatie
    StringBuilder indent = new StringBuilder();
    for (int i = 0; i < indentLevel; i++) {
      indent.append("| ");
    }
    System.out.println(indent + "|- " + account.AccountNaam);

    // Vind alle children van dit account
    List<SubAcc> children = new ArrayList<>();
    for (SubAcc acc : accounts.values()) {
      if (acc.ParentAccountNaam != null && acc.ParentAccountNaam.equals(account.AccountNaam)
          && !acc.AccountNaam.equals(account.AccountNaam)) { // Voorkom oneindige loops
        children.add(acc);
      }
    }

    // Sorteer children (optioneel)
    children.sort(Comparator.comparing(a -> a.AccountNaam));

    // Print alle children met meer indentatie
    for (SubAcc child : children) {
      printAccount(accounts, child, indentLevel + 1);
    }
  }
}
