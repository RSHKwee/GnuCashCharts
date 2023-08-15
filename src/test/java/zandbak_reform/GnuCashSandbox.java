package zandbak_reform;

import org.gnucash.numbers.FixedPointNumber;
import org.gnucash.read.GnucashAccount;
import org.gnucash.read.GnucashTransaction;
import org.gnucash.read.GnucashTransactionSplit;
import org.gnucash.read.impl.GnucashFileImpl;
import org.gnucash.write.GnucashWritableTransaction;
import org.gnucash.write.GnucashWritableTransactionSplit;
import org.gnucash.write.impl.GnucashFileWritingImpl;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

/**
 * Created by Deniss Larka on 19.Apr.2017
 */
public class GnuCashSandbox {
//  private static final Logger LOGGER = Logger.getLogger(Class.class.getName());

  public static void main(String[] args) throws IOException {

    GnuCashSandbox sandbox = new GnuCashSandbox();
    sandbox.process();
  }

  private void process() throws IOException {
    String l_filename = "D:\\temp\\KHKwee.gnucash";
    // String l_filename = "D:\\temp\\Bewindvoering.gnucash";

    GnucashFileWritingImpl gnucashFile = new GnucashFileWritingImpl(new File(l_filename));
    Collection<GnucashAccount> accounts = gnucashFile.getAccounts();
    for (GnucashAccount account : accounts) {
      System.out.print(account.getQualifiedName() + "\t € " + account.getBalanceFormated());
      // System.out.print(" accd: " + account..getAccountCode());
      System.out.print(" dsc: " + account.getDescription() + " nam: " + account.getName());
      System.out.println(" note: " + account.getUserDefinedAttribute("notes"));
    }
    /*
 * @formatter:off
 * KHKwee.gnucash
    GnucashWritableTransaction writableTransaction = gnucashFile.createWritableTransaction();
    writableTransaction.setDescription("check");
    writableTransaction.setCurrencyID("EUR");
    writableTransaction.setDateEntered(LocalDateTime.now());
    GnucashWritableTransactionSplit writingSplit = writableTransaction
        .createWritingSplit(gnucashFile.getAccountByName("Root Account::Income::Bonus"));
    writingSplit.setValue(new FixedPointNumber(100));
    writingSplit.setDescription("descr");

    Collection<? extends GnucashTransaction> transactions = gnucashFile.getTransactions();
    for (GnucashTransaction transaction : transactions) {
      System.out.println(transaction.getDatePosted());
      List<GnucashTransactionSplit> splits = transaction.getSplits();
      for (GnucashTransactionSplit split : splits) {
        System.out.println("\t" + split.getQuantity());
      }
    }
   @formatter:on
*/
  }
}
