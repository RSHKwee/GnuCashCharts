package kwee.gnucashcharts.library.gnuCashDb;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.gnucash.numbers.FixedPointNumber;
import org.gnucash.read.GnucashAccount;
import org.gnucash.read.impl.GnucashFileImpl;
import org.gnucash.read.impl.GnucashPriceDBImpl;

import kwee.gnucashcharts.library.AccountDetails;
import kwee.logger.MyLogger;

public class ReadGnuCashDB {
  private static final Logger lOGGER = MyLogger.getLogger();
  // private ArrayList<String> m_Regels = new ArrayList<String>();
  private GnucashFileImpl m_gnucashFile;
  private Collection<GnucashAccount> m_accounts;
  private GnucashPriceDBImpl m_pricedb;
  private File m_FileName;

  public record Result(FixedPointNumber amount, FixedPointNumber balance) {
  }

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
    m_FileName = a_SelectedFile;
    readGnuCashFile(a_SelectedFile);
  }
  
  public File getFile() {
    return m_FileName;
  }
  
  /**
   * Get content GnuCashFile in CSV format:

   * @param a_Date Sadi on given date
   * @return Result in csv format
   */
  public ArrayList<String> getRegels(LocalDate a_Date) {
    ArrayList<String> l_Regels = new ArrayList<String>();
    l_Regels = filterGnuCash(a_Date);    
    return l_Regels;
  } 
  
  public ArrayList<AccountDetails> getAccDets(LocalDate a_Date) {
    ArrayList<AccountDetails> l_Accdets = new ArrayList<AccountDetails>();
    l_Accdets = filterAccDetsGnuCash(a_Date);    
    return l_Accdets;
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

    try {
      for (GnucashAccount account : m_accounts) {
        AccountDetails l_accdet = Account2AccountDetails(a_Date, account);      
        String sAmnt = l_accdet.get_Amount().toPlainString().replace(".", ",");
        String sSaldo = l_accdet.get_Saldo().toPlainString().replace(".", ",");
        String sRootAccount = "";
        try {
          sRootAccount = l_accdet.get_RootAccount().getName();
        } catch (Exception e) {
          sRootAccount = "";
        }
        String l_regel = String.join(";",l_accdet.getLocalDateStr(), l_accdet.get_AccountNr(), l_accdet.get_AccountName(), sSaldo, sAmnt, l_accdet.get_Remark(), sRootAccount, l_accdet.get_ChildAccounts());
        l_Regels.add(l_regel);
      }
    } catch (Exception e) {
    	e.printStackTrace();
      lOGGER.log(Level.INFO, e.getMessage());
    }
    return l_Regels;
  }
  
  /**
   * Filter transaction for given date
   * 
   * @param a_Date Date
   * @return 
   */
  ArrayList<AccountDetails> l_AccDets;
  private ArrayList<AccountDetails> filterAccDetsGnuCash (LocalDate a_Date) {
    lOGGER.log(Level.FINE, "filterAccDetsGnuCash Date: " + a_Date);
    l_AccDets = new ArrayList<AccountDetails>();
    try {
      m_accounts.forEach(account -> {
        AccountDetails l_accdet = Account2AccountDetails(a_Date, account);
        l_AccDets.add(l_accdet);
      });
      l_AccDets = updateAccDetTotals(l_AccDets);
      l_AccDets = updateAccDetTotals(l_AccDets);
    } catch (Exception e) {
      e.printStackTrace();
      lOGGER.log(Level.INFO, e.getMessage());
    }
    return l_AccDets;
  }

  /**
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
   * @param a_Date
   * @param account
   * @return AccountDetails
   */
  FixedPointNumber totSaldo = new FixedPointNumber("0.0");

  private AccountDetails Account2AccountDetails (LocalDate a_Date, GnucashAccount account) {
    String l_notes = "";
    FixedPointNumber fBalance = account.getBalance(a_Date);
    FixedPointNumber fAmount = new FixedPointNumber("0.0");
    
    if (account.getUserDefinedAttribute("notes") != null) {
      l_notes = account.getUserDefinedAttribute("notes");
      lOGGER.log(Level.FINE, "notes : " + l_notes);
    }
    
    Result r = getStockBalance(a_Date, account);
    fAmount = r.amount;
    fBalance = r.balance;
    
    // Stock convert number shares to amount
    /*
    String atype = account.getType();
    if (atype.equals(GnucashAccount.TYPE_STOCK) || atype.equals(GnucashAccount.TYPE_MUTUAL) ) {
      FixedPointNumber cmdPrice = m_pricedb.getPrice(account.getCurrencyID(), a_Date);
      fAmount = account.getBalance(a_Date);
      fBalance = fBalance.multiply(cmdPrice);
      lOGGER.log(Level.FINE, "Account currence ID: " + account.getCurrencyID());  
    }
   */
    GnucashAccount rootAcc = null;
    Collection<GnucashAccount> accs = account.getChildren();
    String accsString = accs.toString();
    
    try {
      totSaldo = new FixedPointNumber("0.0");
      accs.forEach(acc->{
        Result r2 = getStockBalance(a_Date, acc);
        FixedPointNumber fBalance2 = r2.balance;
        totSaldo.add(fBalance2);
      });
      if (totSaldo.equals(new FixedPointNumber("0.0"))) {
        totSaldo = fBalance;
      }
    } catch (Exception e){
      // Do nothing
      lOGGER.log(Level.FINE, e.getMessage());
      if (totSaldo.equals(new FixedPointNumber("0.0"))) {
        totSaldo = fBalance;
      }
    }
    
    try {
      rootAcc = account.getParentAccount();
    } catch (Exception e){
      // Do nothing
      lOGGER.log(Level.FINE, e.getMessage());
    }
    
    AccountDetails l_accdet = new AccountDetails.Builder()
        .localDate(a_Date)
        .accountName(account.getName())
        .accountNr(account.getDescription())
        .saldo(fBalance)
        .amount(fAmount)
        .remark(l_notes)
        .rootAccount(rootAcc)
        .childAccounts(accsString)
        .totSaldo(totSaldo)
        .build();

    return l_accdet;
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
  
  private ArrayList<AccountDetails> updateAccDetTotals (ArrayList<AccountDetails> a_accdets) {
    ArrayList<AccountDetails> l_AccDets = new ArrayList<AccountDetails>();
    a_accdets.forEach(accdet -> {
      AccountDetails l_accdet = new AccountDetails(accdet);
      l_accdet = updateTotSaldo(accdet);
      if (l_accdet.get_Saldo().equals(new FixedPointNumber("0.0"))) {
        l_accdet.set_Saldo(l_accdet.get_TotSaldo());
      }
      l_AccDets.add(l_accdet);
    });
    return l_AccDets;
  }
  
  
  private AccountDetails updateTotSaldo (AccountDetails a_accdet) {
    AccountDetails l_Accdet = new AccountDetails(a_accdet);
    if (a_accdet.get_RootAccount() != null) {
      Collection<GnucashAccount> accs = a_accdet.get_RootAccount().getChildren();
      GnucashAccount account = a_accdet.get_RootAccount();
    
      try {
        totSaldo = new FixedPointNumber("0.0");
        accs.forEach(acc->{
          totSaldo.add(acc.getBalance());
        });
        if (totSaldo.equals(new FixedPointNumber("0.0"))) {
          totSaldo = account.getBalance();
        }
      } catch (Exception e){
        // Do nothing
        lOGGER.log(Level.INFO, e.getMessage());
      }
    }
    return l_Accdet;
  }
  
  private Result getStockBalance (LocalDate a_Date, GnucashAccount account) {
    FixedPointNumber fAmount = new FixedPointNumber("0.0");
    FixedPointNumber fBalance = account.getBalance(a_Date);

    // Stock convert number shares to amount
    String atype = account.getType();
    if (atype.equals(GnucashAccount.TYPE_STOCK) || atype.equals(GnucashAccount.TYPE_MUTUAL) ) {
      FixedPointNumber cmdPrice = m_pricedb.getPrice(account.getCurrencyID(), a_Date);
      fAmount = account.getBalance(a_Date);
      fBalance = fBalance.multiply(cmdPrice);
      lOGGER.log(Level.FINE, "Account currence ID: " + account.getCurrencyID());  
    }  
    return new Result(fAmount, fBalance);
  }
}
