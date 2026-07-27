package kwee.gnucashcharts.library;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.gnucash.numbers.FixedPointNumber;

public class AccountDetails {
  private String m_RootAccount;
  private String m_ChildAccounts;
  private LocalDate m_LocalDate;
  private String m_AccountNr;
  private String m_AccountName;
  private FixedPointNumber m_Amount;
  private FixedPointNumber m_Saldo;
  private String m_Remark;

  // Nieuwe attributen voor hiërarchie
  private List<AccountDetails> m_Children;
  private boolean m_IsLeaf;
  private int m_Level;

  // Output style enum
  public enum OutputStyle {
    TREE, INDENT, NUMBERED
  }

  // Private constructor voor Builder
  private AccountDetails(Builder builder) {
    this.m_RootAccount = builder.rootAccount != null ? builder.rootAccount : "";
    this.m_ChildAccounts = builder.childAccounts != null ? builder.childAccounts : "";
    this.m_LocalDate = builder.localDate;
    this.m_AccountNr = builder.accountNr != null ? builder.accountNr : "";
    this.m_AccountName = builder.accountName != null ? builder.accountName : "";
    this.m_Amount = builder.amount;
    this.m_Saldo = builder.saldo;
    this.m_Remark = builder.remark != null ? builder.remark : "";
    this.m_Children = new ArrayList<>(); // ALTIJD initialiseren!
    this.m_IsLeaf = builder.isLeaf;
    this.m_Level = builder.level;
  }

  /**
   * Default constructor
   */
  public AccountDetails() {
    this.m_RootAccount = "";
    this.m_ChildAccounts = "";
    this.m_LocalDate = null;
    this.m_AccountNr = "";
    this.m_AccountName = "";
    this.m_Amount = null;
    this.m_Saldo = null;
    this.m_Remark = "";
    this.m_Children = new ArrayList<>(); // ALTIJD initialiseren!
    this.m_IsLeaf = true;
    this.m_Level = 0;
  }

  /**
   * Constructor (behouden voor backward compatibility)
   */
  public AccountDetails(LocalDate a_LocalDate, String a_AccountNr, String a_AccountName, FixedPointNumber a_Amount,
      FixedPointNumber a_Saldo, String a_Remark) {
    this.m_LocalDate = a_LocalDate;
    this.m_AccountNr = a_AccountNr != null ? a_AccountNr : "";
    this.m_AccountName = a_AccountName != null ? a_AccountName : "";
    this.m_Amount = a_Amount;
    this.m_Saldo = a_Saldo;
    this.m_Remark = a_Remark != null ? a_Remark : "";
    this.m_RootAccount = "";
    this.m_ChildAccounts = "";
    this.m_Children = new ArrayList<>(); // ALTIJD initialiseren!
    this.m_IsLeaf = true;
    this.m_Level = 0;
  }

  // Getters
  public LocalDate getLocalDate() {
    return m_LocalDate;
  }

  public String getLocalDateStr() {
    if (m_LocalDate == null)
      return "";
    DateTimeFormatter lformatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    return m_LocalDate.format(lformatter);
  }

  public String get_AccountNr() {
    return m_AccountNr;
  }

  public String get_AccountName() {
    return m_AccountName;
  }

  public FixedPointNumber get_Amount() {
    return m_Amount;
  }

  public FixedPointNumber get_Saldo() {
    return m_Saldo;
  }

  public String get_Remark() {
    return m_Remark;
  }

  public String get_RootAccount() {
    return m_RootAccount;
  }

  public String get_ChildAccounts() {
    return m_ChildAccounts;
  }

  // Nieuwe getters voor hiërarchie
  public List<AccountDetails> get_Children() {
    if (m_Children == null) {
      m_Children = new ArrayList<>();
    }
    return m_Children;
  }

  public boolean is_isLeaf() {
    return m_IsLeaf;
  }

  public int get_Level() {
    return m_Level;
  }

  // Setters
  public void setLocalDate(LocalDate a_localDate) {
    m_LocalDate = a_localDate;
  }

  public void set_AccountNr(String m_AccountNr) {
    this.m_AccountNr = m_AccountNr;
  }

  public void set_AccountName(String m_AccountName) {
    this.m_AccountName = m_AccountName;
  }

  public void set_Amount(FixedPointNumber m_Amount) {
    this.m_Amount = m_Amount;
  }

  public void set_Saldo(FixedPointNumber m_Saldo) {
    this.m_Saldo = m_Saldo;
  }

  public void set_Remark(String m_Remark) {
    this.m_Remark = m_Remark;
  }

  public void set_RootAccount(String m_RootAccount) {
    this.m_RootAccount = m_RootAccount;
  }

  public void set_ChildAccounts(String m_ChildAccounts) {
    this.m_ChildAccounts = m_ChildAccounts;
  }

  // Nieuwe setters voor hiërarchie
  public void add_Child(AccountDetails child) {
    if (m_Children == null) {
      m_Children = new ArrayList<>();
    }
    child.setLevel(m_Level + 1);
    m_Children.add(child);
  }

  public void setLeaf(boolean isLeaf) {
    this.m_IsLeaf = isLeaf;
  }

  public void setLevel(int level) {
    this.m_Level = level;
  }

  public String print() {
    String regel = "";
    regel = "Root: " + m_RootAccount;
    regel = regel + " | Childs: " + m_ChildAccounts;
    regel = regel + " | Date: " + (m_LocalDate != null ? m_LocalDate.toString() : "null");
    regel = regel + " | AccNr: " + m_AccountNr;
    regel = regel + " | AccName: " + m_AccountName;
    regel = regel + " | Amount: " + (m_Amount != null ? m_Amount.toPlainString().replace(".", ",") : "null");
    regel = regel + " | Saldo: " + (m_Saldo != null ? m_Saldo.toPlainString().replace(".", ",") : "null");
    regel = regel + " | Remark: " + m_Remark;
    regel = regel + " | Level: " + m_Level;
    regel = regel + " | isLeaf: " + m_IsLeaf;
    regel = regel + " | childern: " + m_Children.toString();
    return regel;
  }

  // Methode voor hiërarchie string
  public String getHierarchyString(OutputStyle style) {
    StringBuilder sb = new StringBuilder();
    buildHierarchyString(sb, 0, style);
    return sb.toString();
  }

  private void buildHierarchyString(StringBuilder sb, int depth, OutputStyle style) {
    // Print alleen als er een naam is en niet de root is
    if (m_RootAccount != null && !m_RootAccount.isEmpty() && !m_RootAccount.equals("GnuCash")
        && !m_RootAccount.startsWith("GnuCash ")) {

      switch (style) {
      case TREE:
        sb.append(getTreePrefix(depth));
        break;
      case INDENT:
        sb.append(" ".repeat(Math.max(0, depth * 4)));
        break;
      case NUMBERED:
        sb.append(depth + ". ");
        break;
      }
      sb.append(m_RootAccount);

      // Details toevoegen voor leaf nodes
      if (m_IsLeaf && m_AccountNr != null && !m_AccountNr.isEmpty()) {
        sb.append(" (").append(m_AccountNr).append(")");
        if (m_Saldo != null) {
          sb.append(" - ").append(m_Saldo.toPlainString());
        }
      }
      sb.append("\n");
    }

    // Kinderen verwerken
    if (m_Children != null) {
      for (AccountDetails child : m_Children) {
        child.buildHierarchyString(sb, depth + 1, style);
      }
    }
  }

  private String getTreePrefix(int depth) {
    if (depth == 0)
      return "";
    StringBuilder sb = new StringBuilder();
    for (int i = 1; i < depth; i++) {
      sb.append("│   ");
    }
    sb.append("├── ");
    return sb.toString();
  }

  // Builder Class
  public static class Builder {
    private String rootAccount = "";
    private String childAccounts = "";
    private LocalDate localDate;
    private String accountNr = "";
    private String accountName = "";
    private FixedPointNumber amount;
    private FixedPointNumber saldo;
    private String remark = "";
    private boolean isLeaf = true;
    private int level = 0;

    public Builder() {
      // Default constructor
    }

    public Builder rootAccount(String rootAccount) {
      this.rootAccount = rootAccount != null ? rootAccount : "";
      return this;
    }

    public Builder childAccounts(String childAccounts) {
      this.childAccounts = childAccounts != null ? childAccounts : "";
      return this;
    }

    public Builder localDate(LocalDate localDate) {
      this.localDate = localDate;
      return this;
    }

    public Builder accountNr(String accountNr) {
      this.accountNr = accountNr != null ? accountNr : "";
      return this;
    }

    public Builder accountName(String accountName) {
      this.accountName = accountName != null ? accountName : "";
      return this;
    }

    public Builder amount(FixedPointNumber amount) {
      this.amount = amount;
      return this;
    }

    public Builder saldo(FixedPointNumber saldo) {
      this.saldo = saldo;
      return this;
    }

    public Builder remark(String remark) {
      this.remark = remark != null ? remark : "";
      return this;
    }

    public Builder isLeaf(boolean isLeaf) {
      this.isLeaf = isLeaf;
      return this;
    }

    public Builder level(int level) {
      this.level = level;
      return this;
    }

    public AccountDetails build() {
      return new AccountDetails(this);
    }
  }
}