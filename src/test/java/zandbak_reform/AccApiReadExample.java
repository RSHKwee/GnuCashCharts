package zandbak_reform;

import com.druvu.acc.api.AccStore;
import com.druvu.acc.api.entity.Transaction;
import com.druvu.acc.api.service.AccountService;

import kwee.logger.MyLogger;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Example usage of Acc API.
 *
 * @author Deniss Larka <br>
 *         on 11 jan 2026
 */

public class AccApiReadExample {
  private static final Logger lOGGER = MyLogger.getLogger();
  private static String gcshFileName = "D:\\Users\\René\\SynologyDrive\\Documenten\\Administraties\\Prive_RK.gnucash";

  static void main(String[] args) {
    /* @formatter:off
    if (args.length < 1) {
      System.err.println("Usage: AccApiExample <gnucash-file>");
      System.exit(1);
    }
    @formatter:on
    */
    Path filePath = Paths.get(gcshFileName);
    new AccApiReadExample().run(filePath);
  }

  void run(Path filePath) {

    final AccStore store = AccStore.load(filePath);
    final AccountService accountService = AccountService.create(store, "Root Account");
    store.prices();

    for (var account : store.accounts()) {
      // 'balance' is the account's own splits, 'total' rolls up its sub-accounts - the two
      // columns of the GnuCash account tree. Both carry their commodity: a balance is always
      // one, a total can span several and is printed per commodity rather than collapsed.
      lOGGER.log(Level.INFO, "{} balance: {} total: {}" + account + accountService.balance(account.id())
          + accountService.totalBalance(account.id()));
      lOGGER.log(Level.INFO, "");
    }

    for (Transaction tx : store.transactions()) {
      lOGGER.log(Level.INFO, "{}" + tx.toString());
      lOGGER.log(Level.INFO, "");
    }
  }
}
