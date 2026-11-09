package zandbak_reform;

import java.io.File;
import java.util.List;

import org.gnucash.generated.Price;
import org.gnucash.numbers.FixedPointNumber;
import org.gnucash.read.impl.GnucashFileImpl;
import org.gnucash.read.impl.GnucashPriceDBImpl;

public class OverzichtPrices {
  // BEGIN Example data -- adapt to your needs
//  private static String gcshFileName = "G:\\Users\\René\\SynologyDrive\\Administraties\\Prive_RK.gnucash";
  private static String gcshFileName = "D:\\Users\\René\\SynologyDrive\\Documenten\\Administraties\\Prive_RK.gnucash";
  // END Example data

  public static void main(String[] args) {
    GnucashFileImpl gcshFile = null;
    try {
      gcshFile = new GnucashFileImpl(new File(gcshFileName));

      GnucashPriceDBImpl pdb = new GnucashPriceDBImpl(gcshFile);
      List<String> l_commodities = pdb.getCommodities();

      l_commodities.forEach(cmd -> {
        List<Price> lst_prices = pdb.getPrices(cmd);

        System.out.println("Fonds " + cmd);
        char euroChar = '\u20AC';
        lst_prices.forEach(prc -> {
          FixedPointNumber p = new FixedPointNumber(prc.getPriceValue());
          System.out
              .println(prc.getPriceTime().getTsDate() + "| " + euroChar + " " + p.toString().replace('.', ',') + "|");
        });
        System.out.println("------------------------");
      });

    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

  }
}
