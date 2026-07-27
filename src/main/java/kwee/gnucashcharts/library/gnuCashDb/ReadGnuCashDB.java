package kwee.gnucashcharts.library.gnuCashDb;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
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
  private GnucashFileImpl m_gnucashFile;
  private Collection<GnucashAccount> m_accounts;
  private GnucashPriceDBImpl m_pricedb;
  private File m_FileName;
  private AccountDetails m_rootHierarchy;

  // Decimal formatting
  private DecimalFormat m_decimalFormat;
  private Locale m_locale;

  /**
   * Constructor met standaard systeem locale
   */
  public ReadGnuCashDB(File a_SelectedFile) {
    this(a_SelectedFile, Locale.getDefault());
  }

  /**
   * Constructor met specifieke locale
   */
  public ReadGnuCashDB(File a_SelectedFile, Locale locale) {
    m_FileName = a_SelectedFile;
    m_locale = locale;
    readGnuCashFile(a_SelectedFile);
    buildAccountHierarchy();
    initializeDecimalFormat();
  }

  public File getFile() {
    return m_FileName;
  }

  /**
   * Get content GnuCashFile in CSV format
   */
  public ArrayList<String> getRegels(LocalDate a_Date) {
    ArrayList<String> l_Regels = new ArrayList<String>();
    l_Regels = filterGnuCash(a_Date);
    return l_Regels;
  }

  /**
   * Get AccountDetails als platte lijst
   */
  public ArrayList<AccountDetails> getAccDets(LocalDate a_Date) {
    ArrayList<AccountDetails> l_Accdets = new ArrayList<AccountDetails>();
    l_Accdets = filterAccDetsGnuCash(a_Date);
    return l_Accdets;
  }

  /**
   * Get AccountDetails als hiërarchische structuur
   */
  public AccountDetails getAccountHierarchy(LocalDate a_Date) {
    return buildHierarchyForDate(a_Date);
  }

  /**
   * Get hiërarchie als geformatteerde string
   */
  public String getHierarchyString(LocalDate a_Date) {
    AccountDetails hierarchy = getAccountHierarchy(a_Date);
    if (hierarchy != null) {
      return hierarchy.getHierarchyString(AccountDetails.OutputStyle.TREE);
    }
    return "Geen accounts gevonden";
  }

  /**
   * Set locale voor decimal formatting
   */
  public void setLocale(Locale locale) {
    this.m_locale = locale;
    initializeDecimalFormat();
  }

  /**
   * Get huidige locale
   */
  public Locale getLocale() {
    return m_locale;
  }

  /**
   * Get decimal separator
   */
  public String getDecimalSeparator() {
    return String.valueOf(m_decimalFormat.getDecimalFormatSymbols().getDecimalSeparator());
  }

  /**
   * Detecteer en set locale op basis van data
   */
  public void detectAndSetLocaleFromData() {
    try {
      for (GnucashAccount account : m_accounts) {
        FixedPointNumber balance = account.getBalance();
        if (balance != null) {
          String balanceStr = balance.toPlainString();

          if (balanceStr.contains(",")) {
            setLocale(new Locale("nl", "NL"));
            lOGGER.log(Level.INFO, "Locale gedetecteerd: Nederlands (komma als decimaal scheidingsteken)");
            return;
          } else if (balanceStr.contains(".")) {
            setLocale(Locale.US);
            lOGGER.log(Level.INFO, "Locale gedetecteerd: US/Engels (punt als decimaal scheidingsteken)");
            return;
          }
        }
      }
    } catch (Exception e) {
      lOGGER.log(Level.WARNING, "Kon decimal separator niet detecteren uit data: " + e.getMessage());
    }
  }

  // Private functions

  /**
   * Initialize decimal format
   */
  private void initializeDecimalFormat() {
    DecimalFormatSymbols symbols = new DecimalFormatSymbols(m_locale);
    m_decimalFormat = new DecimalFormat("#,##0.00", symbols);
    m_decimalFormat.setMinimumFractionDigits(2);
    m_decimalFormat.setMaximumFractionDigits(2);

    lOGGER.log(Level.FINE, "Decimal format geïnitialiseerd met locale: " + m_locale.getDisplayName());
    lOGGER.log(Level.FINE, "Decimal separator: " + symbols.getDecimalSeparator());
    lOGGER.log(Level.FINE, "Grouping separator: " + symbols.getGroupingSeparator());
  }

  /**
   * Formatteer FixedPointNumber met huidige locale
   */
  private String formatNumber(FixedPointNumber number) {
    if (number == null) {
      return "0,00";
    }
    return m_decimalFormat.format(number.doubleValue());
  }

  /**
   * Bouw de complete account hiërarchie
   */
  private void buildAccountHierarchy() {
    try {
      // Maak een root node
      m_rootHierarchy = new AccountDetails.Builder().rootAccount("GnuCash").build();

      // Map om accounts op te slaan op naam voor snelle lookup
      Map<String, AccountDetails> accountMap = new HashMap<>();

      // Eerste pas: maak AccountDetails objecten voor alle accounts
      for (GnucashAccount account : m_accounts) {
        AccountDetails accDet = createAccountDetails(account);
        accountMap.put(account.getName(), accDet);
      }

      // Tweede pas: bouw de hiërarchie
      for (GnucashAccount account : m_accounts) {
        AccountDetails current = accountMap.get(account.getName());
        GnucashAccount parent = account.getParentAccount();

        if (parent == null) {
          // Root account - voeg toe aan hoofdroot
          current.setLevel(m_rootHierarchy.get_Level() + 1);
          m_rootHierarchy.add_Child(current);
        } else {
          // Voeg toe aan parent
          AccountDetails parentDet = accountMap.get(parent.getName());
          current.setLevel(parentDet.get_Level() + 1);
          if (parentDet != null) {
            parentDet.add_Child(current);
          }
        }
      }

      lOGGER.log(Level.INFO, "Hiërarchie opgebouwd met " + accountMap.size() + " accounts");

    } catch (Exception e) {
      e.printStackTrace();
      lOGGER.log(Level.SEVERE, "Fout bij opbouwen hiërarchie: " + e.getMessage());
    }
  }

  /**
   * Bouw hiërarchie voor een specifieke datum
   */
  private AccountDetails buildHierarchyForDate(LocalDate a_Date) {
    try {
      // Maak een kopie van de hiërarchie met de actuele saldi
      AccountDetails dateHierarchy = new AccountDetails.Builder().rootAccount("GnuCash " + a_Date.toString()).build();

      // Voor elk account in de hiërarchie, maak een nieuwe met de juiste datum
      Map<String, AccountDetails> dateAccountMap = new HashMap<>();

      // Eerste pas: maak AccountDetails voor alle accounts met de datum
      for (GnucashAccount account : m_accounts) {
        AccountDetails accDet = Account2AccountDetails(a_Date, account);
        dateAccountMap.put(account.getName(), accDet);
      }

      // Tweede pas: bouw de hiërarchie
      for (GnucashAccount account : m_accounts) {
        AccountDetails current = dateAccountMap.get(account.getName());
        GnucashAccount parent = account.getParentAccount();

        if (parent == null) {
          dateHierarchy.add_Child(current);
        } else {
          AccountDetails parentDet = dateAccountMap.get(parent.getName());
          if (parentDet != null) {
            parentDet.add_Child(current);
          }
        }
      }

      return dateHierarchy;

    } catch (Exception e) {
      e.printStackTrace();
      lOGGER.log(Level.SEVERE, "Fout bij bouwen datum hiërarchie: " + e.getMessage());
      return null;
    }
  }

  /**
   * Zoek een account in de hiërarchie op naam
   */
  private AccountDetails findAccountInHierarchy(AccountDetails root, String accountName) {
    if (root.get_RootAccount().equals(accountName)) {
      return root;
    }

    for (AccountDetails child : root.get_Children()) {
      AccountDetails found = findAccountInHierarchy(child, accountName);
      if (found != null) {
        return found;
      }
    }

    return null;
  }

  /**
   * Filter transactions for given date (CSV output)
   */
  private ArrayList<String> filterGnuCash(LocalDate a_Date) {
    ArrayList<String> l_Regels = new ArrayList<String>();
    lOGGER.log(Level.FINE, "filterGnuCash Date: " + a_Date);

    try {
      for (GnucashAccount account : m_accounts) {
        AccountDetails l_accdet = Account2AccountDetails(a_Date, account);

        // Gebruik de locale-aware formatting
        String sAmnt = formatNumber(l_accdet.get_Amount());
        String sSaldo = formatNumber(l_accdet.get_Saldo());

        String l_regel = String.join(";", l_accdet.getLocalDateStr(), l_accdet.get_AccountNr(),
            l_accdet.get_AccountName(), sSaldo, sAmnt, l_accdet.get_Remark(), l_accdet.get_RootAccount(),
            l_accdet.get_ChildAccounts());
        l_Regels.add(l_regel);
      }
    } catch (Exception e) {
      e.printStackTrace();
      lOGGER.log(Level.INFO, e.getMessage());
    }
    return l_Regels;
  }

  /**
   * Filter AccountDetails for given date
   */
  private ArrayList<AccountDetails> filterAccDetsGnuCash(LocalDate a_Date) {
    ArrayList<AccountDetails> l_AccDets = new ArrayList<AccountDetails>();
    lOGGER.log(Level.FINE, "filterAccDetsGnuCash Date: " + a_Date);

    try {
      for (GnucashAccount account : m_accounts) {
        AccountDetails l_accdet = Account2AccountDetails(a_Date, account);
        l_AccDets.add(l_accdet);
      }
    } catch (Exception e) {
      e.printStackTrace();
      lOGGER.log(Level.INFO, e.getMessage());
    }
    return l_AccDets;
  }

  /**
   * Convert GnuCashAccount to AccountDetails met datum
   */
  private AccountDetails Account2AccountDetails(LocalDate a_Date, GnucashAccount account) {
    String l_notes = "";
    FixedPointNumber fBalance = account.getBalance(a_Date);
    FixedPointNumber fAmount = new FixedPointNumber("0.0");

    if (account.getUserDefinedAttribute("notes") != null) {
      l_notes = account.getUserDefinedAttribute("notes");
      lOGGER.log(Level.FINE, "notes : " + l_notes);
    }

    // Stock convert number shares to amount
    String atype = account.getType();
    if (atype.equals(GnucashAccount.TYPE_STOCK) || atype.equals(GnucashAccount.TYPE_MUTUAL)) {
      FixedPointNumber cmdPrice = m_pricedb.getPrice(account.getCurrencyID(), a_Date);
      fAmount = account.getBalance(a_Date);
      fBalance = fBalance.multiply(cmdPrice);
      lOGGER.log(Level.FINE, "Account currency ID: " + account.getCurrencyID());
    }

    // Bepaal root account
    String rootAcc = "";
    String childAccs = "";
    try {
      GnucashAccount parent = account.getParentAccount();
      if (parent != null) {
        rootAcc = parent.getName();
        if (!account.getChildren().isEmpty()) {
          childAccs = "Has children";
        }
      }
    } catch (Exception e) {
      // Root account - do nothing
      lOGGER.log(Level.FINE, "Root account: " + account.getName());
    }

    // Bepaal of dit een leaf node is (geen kinderen)
    boolean isLeaf = account.getChildren().isEmpty();

    AccountDetails l_accdet = new AccountDetails.Builder().localDate(a_Date).rootAccount(rootAcc)
        .accountName(account.getName()).accountNr(account.getDescription()).saldo(fBalance).amount(fAmount)
        .remark(l_notes).childAccounts(childAccs).isLeaf(isLeaf).build();

    return l_accdet;
  }

  /**
   * Create AccountDetails zonder datum voor hiërarchie bouw
   */
  private AccountDetails createAccountDetails(GnucashAccount account) {
    String l_notes = "";
    if (account.getUserDefinedAttribute("notes") != null) {
      l_notes = account.getUserDefinedAttribute("notes");
    }

    // Bepaal root account
    String rootAcc = "";
    String childAccs = "";
    try {
      GnucashAccount parent = account.getParentAccount();
      if (parent != null) {
        rootAcc = parent.getName();
        if (!account.getChildren().isEmpty()) {
          childAccs = "Has children";
        }
      }
    } catch (Exception e) {
      // Root account
      lOGGER.log(Level.FINE, "Root account: " + account.getName());
    }

    boolean isLeaf = account.getChildren().isEmpty();

    return new AccountDetails.Builder().rootAccount(rootAcc).accountName(account.getName())
        .accountNr(account.getDescription()).remark(l_notes).childAccounts(childAccs).isLeaf(isLeaf).build();
  }

  /**
   * Read GnuCash file
   */
  private void readGnuCashFile(File a_SelectedFile) {
    try {
      m_gnucashFile = new GnucashFileImpl(a_SelectedFile);
      m_accounts = m_gnucashFile.getAccounts();
      m_pricedb = new GnucashPriceDBImpl(m_gnucashFile);

      lOGGER.log(Level.INFO, "GnuCash bestand geladen: " + m_accounts.size() + " accounts");

    } catch (IOException e) {
      e.printStackTrace();
      lOGGER.log(Level.SEVERE, "Fout bij lezen GnuCash bestand: " + e.getMessage());
    }
  }
}