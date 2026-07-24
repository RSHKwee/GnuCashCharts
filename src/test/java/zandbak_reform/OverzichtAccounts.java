package zandbak_reform;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.gnucash.generated.Price;
import org.gnucash.numbers.FixedPointNumber;
import org.gnucash.read.GnucashAccount;
import org.gnucash.read.GnucashFile;
import org.gnucash.read.impl.GnucashFileImpl;
import org.gnucash.read.impl.GnucashPriceDBImpl;

import kwee.gnucashcharts.library.AccountDetails;
import kwee.gnucashcharts.library.gnuCashDb.ReadGnuCashDB;

public class OverzichtAccounts {
  // BEGIN Example data -- adapt to your needs
//  private static String gcshFileName = "G:\\Users\\René\\SynologyDrive\\Administraties\\Prive_RK.gnucash";
  private static String gcshFileName = "D:\\Users\\René\\SynologyDrive\\Documenten\\Administraties\\Prive_RK.gnucash";
  // END Example data

  public static void main(String[] args) {
    LocalDate toDay = LocalDate.now();

    ReadGnuCashDB rGnuCsh = new ReadGnuCashDB(new File(gcshFileName));
    ArrayList<AccountDetails> accDets = rGnuCsh.getAccDets(toDay);

    accDets.forEach(acc -> {
      System.out.println(acc.print());
    });

    System.out.println("==============");
    ArrayList<String> Regels = rGnuCsh.getRegels(toDay);
    Regels.forEach(regel -> {
      System.out.println(regel);
    });

    if (false) {
      GnucashFileImpl gcshFile = null;
      try {
        gcshFile = new GnucashFileImpl(new File(gcshFileName));

        GnucashPriceDBImpl pdb = new GnucashPriceDBImpl(gcshFile);

        GnucashFile acc = pdb.getFile();
        Collection<GnucashAccount> listAcc = acc.getAccounts();
        // Collection<GnucashAccount> listAcc = acc.getAccountsByParentID("Activa");
        listAcc.forEach(Acc -> {

          FixedPointNumber Bal = Acc.getBalance(toDay);
          String Bal2 = Acc.getBalanceFormatted();
          String acname = Acc.getName();
          String qualAccName = Acc.getQualifiedName();
          System.out.println("QualName: " + qualAccName + "/ Account: " + acname + ", Bal: " + Bal);
          System.out.println("Bal2: " + Bal2);
        });

        if (false) {
          List<String> l_commodities = pdb.getCommodities();

          l_commodities.forEach(cmd -> {
            List<Price> lst_prices = pdb.getPrices(cmd);

            System.out.println("Fonds " + cmd);
            char euroChar = '\u20AC';
            lst_prices.forEach(prc -> {
              FixedPointNumber p = new FixedPointNumber(prc.getPriceValue());
              System.out.println(
                  prc.getPriceTime().getTsDate() + "| " + euroChar + " " + p.toString().replace('.', ',') + "|");
            });
            System.out.println("------------------------");
          });
        }
      } catch (Exception e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }
  }
}
