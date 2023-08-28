package kwee.gnucashcharts.library.gnuCashDb;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.gnucash.read.GnucashAccount;
import org.gnucash.write.impl.GnucashFileWritingImpl;

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
    GnucashFileWritingImpl gnucashFile;
    lOGGER.log(Level.FINE, "readGnuCash Date: " + a_Date);
    
    DateTimeFormatter lformatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    String formattedLocalDate = a_Date.format(lformatter);

    try {
      gnucashFile = new GnucashFileWritingImpl(a_SelectedFile);

      Collection<GnucashAccount> accounts = gnucashFile.getAccounts();
      for (GnucashAccount account : accounts) {
        String l_notes = "";
        if (account.getUserDefinedAttribute("notes") != null) {
          l_notes = account.getUserDefinedAttribute("notes");
        }
        String sBalance = account.getBalance(a_Date).toString().replace(".", ",");
        //String tmp = account.getBalanceFormated(); // tbv debug
        String l_regel = String.join(";",formattedLocalDate, account.getName(), account.getDescription(), sBalance, "", l_notes);
        m_Regels.add(l_regel);
      }
    } catch (Exception e) {
      lOGGER.log(Level.INFO, e.getMessage());
    }
  }
}
