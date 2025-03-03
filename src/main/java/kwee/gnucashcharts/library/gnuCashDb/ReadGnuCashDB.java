package kwee.gnucashcharts.library.gnuCashDb;

import java.io.File;
import java.io.IOException;
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
  // private ArrayList<String> m_Regels = new ArrayList<String>();
  private GnucashFileImpl m_gnucashFile;
  private Collection<GnucashAccount> m_accounts;
  private GnucashPriceDBImpl m_pricedb;

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
    readGnuCashFile(a_SelectedFile);
  }
  
  /**
   * Get content GnuCashFile in CSV format.
   * 
   * @param a_Date Sadi on given date
   * @return Result in csv format
   */
  public ArrayList<String> getRegels(LocalDate a_Date) {
    ArrayList<String> l_Regels = new ArrayList<String>();
    l_Regels = filterGnuCash(a_Date);    
    return l_Regels;
  } 
  
  // Private functions
  /**
   * Filter transaction for given date
   * 
   * @param a_Date Date
   * @return 
   */
  private ArrayList<String> filterGnuCash (LocalDate a_Date) {
    ArrayList<String> l_Regels = new ArrayList<String>();
    lOGGER.log(Level.FINE, "filterGnuCash Date: " + a_Date);
    
    DateTimeFormatter lformatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    String formattedLocalDate = a_Date.format(lformatter);

    try {
      for (GnucashAccount account : m_accounts) {
        String l_notes = "";
        FixedPointNumber fBalance = account.getBalance(a_Date);
        
        if (account.getUserDefinedAttribute("notes") != null) {
          l_notes = account.getUserDefinedAttribute("notes");
          lOGGER.log(Level.FINE, "notes : " + l_notes);
        }
        
        // Stock convert number shares to amount
        String atype = account.getType();
        if (atype.equals(GnucashAccount.TYPE_STOCK) || atype.equals(GnucashAccount.TYPE_MUTUAL) ) {
        	FixedPointNumber cmdPrice = m_pricedb.getPrice(account.getCurrencyID(), a_Date);
          fBalance = fBalance.multiply(cmdPrice);
          lOGGER.log(Level.FINE, "Account currence ID: " + account.getCurrencyID());  
        }
        
        String sBalance = fBalance.toString().replace(".", ",");
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
        l_Regels.add(l_regel);
      }
    } catch (Exception e) {
    	e.printStackTrace();
      lOGGER.log(Level.INFO, e.getMessage());
    }
    return l_Regels;
  }
  
  /**
   * Read GnuCash file
   * 
   * @param a_SelectedFile GnuCash File
   */ 
  private void readGnuCashFile (File a_SelectedFile) {
    try {
      m_gnucashFile = new GnucashFileImpl(a_SelectedFile);
      m_accounts = m_gnucashFile.getAccounts();

      m_pricedb = new GnucashPriceDBImpl(m_gnucashFile);
    } catch (IOException e) {
      e.printStackTrace();
      lOGGER.log(Level.INFO, e.getMessage());
    }    
  }  
  
}
