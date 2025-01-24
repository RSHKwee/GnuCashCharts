package kwee.gnucashcharts.library.gnuCashDb;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.gnucash.numbers.FixedPointNumber;
import org.gnucash.read.GnucashAccount;
import org.gnucash.read.impl.GnucashFileImpl;
import org.gnucash.read.impl.GnucashPriceDBImpl;

import kwee.logger.MyLogger;

public class ReadGnuCashDB {
  private static final Logger lOGGER = MyLogger.getLogger();
  private ArrayList<String> m_Regels = new ArrayList<String>();

  /**
   * Read GnuCash file and return content as CSV-format
   * 
   * @formatter:on
   *   String LocalDate
   *   String m_AccountNr = ""; account.getName()
   *   String m_AccountName = ""; account.getDescription()
   *   double m_Amount = 0.0; account.getBalance().doubleValue()
   *   double m_Saldo = 0.0;
   *   String m_Remark = ""; account.getUserDefinedAttribute("notes")    
   * @formatter:off
   *
   * @param a_SelectedFile GnuCash file
   */
  public ReadGnuCashDB(File a_SelectedFile) {
    LocalDate l_Date = LocalDate.now();
    readGnuCash(a_SelectedFile, l_Date);
  }
  
  /**
   * Read GnuCash file and return content as CSV-format
   * 
   * @param a_SelectedFile GnuCash file
   * @param a_Date Sadi on given date
   */
  public ReadGnuCashDB(File a_SelectedFile, LocalDate a_Date) {
    readGnuCash(a_SelectedFile, a_Date);
  }
  
  /**
   * Get content GnuCashFile in CSV format.
   * 
   * @return Result in csv format
   */
  public ArrayList<String> getRegels() {
    return m_Regels;
  }
  
  // Private functions
  /**
   * Read GnuCash file
   * 
   * @param a_SelectedFile GnuCash File
   * @param a_Date Date
   */
  private void readGnuCash (File a_SelectedFile, LocalDate a_Date) {
    GnucashFileImpl gnucashFile;
    lOGGER.log(Level.FINE, "readGnuCash Date: " + a_Date);
    
    DateTimeFormatter lformatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    String formattedLocalDate = a_Date.format(lformatter);

    try {
      gnucashFile = new GnucashFileImpl(a_SelectedFile);

      Collection<GnucashAccount> accounts = gnucashFile.getAccounts();
      GnucashPriceDBImpl pricedb = new GnucashPriceDBImpl(gnucashFile);
      
      for (GnucashAccount account : accounts) {
        String l_notes = "";
        FixedPointNumber fBalance = account.getBalance(a_Date);
        
        if (account.getUserDefinedAttribute("notes") != null) {
          l_notes = account.getUserDefinedAttribute("notes");
          lOGGER.log(Level.FINE, "notes : " + l_notes);
        }
        String atype = account.getType();
        if (atype.equals(GnucashAccount.TYPE_STOCK)) {
        	FixedPointNumber cmdPrice = pricedb.getPrice(account.getCurrencyID(), a_Date);
          fBalance = fBalance.multiply(cmdPrice);
          lOGGER.log(Level.FINE, "Account currence ID: " + account.getCurrencyID());  
        }
        
        String sBalance = fBalance.toString().replace(".", ",");
        //String tmp = account.getBalanceFormated(); // tbv debug
        lOGGER.log(Level.FINE, " Account: " + account.getName() + " / Balance: " + sBalance + " / type: " + account.getType() );
       
        String rootAcc = "";
        Collection<GnucashAccount> accs = account.getChildren();
        String accsString = accs.toString();
        try {
          rootAcc = account.getParentAccount().getName();
        } catch (Exception e){
          // Do nothing
          lOGGER.log(Level.FINE, e.getMessage());
        }
        String l_regel = String.join(";",formattedLocalDate, account.getName(), account.getDescription(), sBalance, "", l_notes, rootAcc, accsString);
        m_Regels.add(l_regel);
      }
    } catch (Exception e) {
    	e.printStackTrace();
      lOGGER.log(Level.INFO, e.getMessage());
    }
  }
}
