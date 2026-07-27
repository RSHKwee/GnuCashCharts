package zandbak_reform;

import java.io.File;
import java.time.LocalDate;
import java.util.*;

import kwee.gnucashcharts.library.AccountDetails;
import kwee.gnucashcharts.library.gnuCashDb.ReadGnuCashDB;

public class AccountHierarchyPrinter2 {

  public static void main(String[] args) {
    // Voorbeeld data
    Map<String, AccountDetails> Accounts = new HashMap<>();
    String gcshFileName = "D:\\Users\\René\\SynologyDrive\\Documenten\\Administraties\\Prive_RK.gnucash";

    LocalDate toDay = LocalDate.now();

    ReadGnuCashDB rGnuCsh = new ReadGnuCashDB(new File(gcshFileName));
    ArrayList<AccountDetails> accDets = rGnuCsh.getAccDets(toDay);

    accDets.forEach(acc -> {
      Accounts.put(acc.get_AccountName(), new AccountDetails(acc));
    });

    // Print de hiërarchie
    printHierarchy(Accounts);
  }

  public static void printHierarchy(Map<String, AccountDetails> accounts) {
    // Vind alle top-level accounts (parent is leeg)
    List<AccountDetails> topLevelAccounts = new ArrayList<>();
    for (AccountDetails acc : accounts.values()) {
      if (acc.get_RootAccount() == null || acc.get_RootAccount().isEmpty()) {
        topLevelAccounts.add(acc);
      }
    }

    // Sorteer top-level accounts (optioneel)
    // topLevelAccounts.sort(Comparator.comparing(a -> accounts. ));

    // Print elk top-level account en zijn children
    for (AccountDetails top : topLevelAccounts) {
      printAccount(accounts, top, 0);
    }
  }

  private static void printAccount(Map<String, AccountDetails> accounts, AccountDetails account, int indentLevel) {
    // Print indentatie
    StringBuilder indent = new StringBuilder();
    for (int i = 0; i < indentLevel; i++) {
      indent.append("| ");
    }
    System.out.println(
        indent + "|- " + account.get_AccountName() + " " + account.get_Saldo().toPlainString().replace(".", ","));

    // Vind alle children van dit account
    List<AccountDetails> children = new ArrayList<>();
    for (AccountDetails acc : accounts.values()) {
      if (acc.get_RootAccount() != null && acc.get_RootAccount().equals(account.get_AccountName())
          && !acc.get_AccountName().equals(account.get_AccountName())) { // Voorkom oneindige loops
        children.add(acc);
      }
    }

    // Sorteer children (optioneel)
    children.sort(Comparator.comparing(a -> account.get_AccountName()));

    // Print alle children met meer indentatie
    for (AccountDetails child : children) {
      printAccount(accounts, child, indentLevel + 1);
    }
  }
}
