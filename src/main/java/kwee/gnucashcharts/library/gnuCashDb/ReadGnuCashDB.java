package kwee.gnucashcharts.library.gnuCashDb;

import java.io.File;
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

  /*
   * @formatter:on
   *   String m_AccountNr = ""; account.getName()
   *   String m_AccountName = ""; account.getDescription()
   *   double m_Amount = 0.0; account.getBalance().doubleValue()
   *   double m_Saldo = 0.0;
   *   String m_Remark = ""; account.getUserDefinedAttribute("notes")    
   * @formatter:off
   */ 
  public ReadGnuCashDB(File a_SelectedFile) {
    GnucashFileWritingImpl gnucashFile;
    try {
      gnucashFile = new GnucashFileWritingImpl(a_SelectedFile);

      Collection<GnucashAccount> accounts = gnucashFile.getAccounts();
      for (GnucashAccount account : accounts) {
        String l_notes = "";
        if (account.getUserDefinedAttribute("notes") != null) {
          l_notes = account.getUserDefinedAttribute("notes");
        }        
        String l_regel = String.join(";", account.getName(), account.getDescription(), account.getBalanceFormated(), "", l_notes);
        m_Regels.add(l_regel);
      }
    } catch (Exception e) {
      lOGGER.log(Level.INFO, e.getMessage());
    }
  }
  
  public ArrayList<String> getRegels() {
    return m_Regels;
  }
   
}
