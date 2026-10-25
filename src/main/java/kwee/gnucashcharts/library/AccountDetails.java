package kwee.gnucashcharts.library;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import com.druvu.acc.api.entity.Account;
import com.druvu.acc.api.entity.Amount;

public class AccountDetails {
  private Account m_RootAccount;
  private String m_ChildAccounts;
  private LocalDate m_LocalDate;
  private String m_AccountNr;
  private String m_AccountName;
  private Amount m_Amount;
  private Amount m_Saldo;
  private Optional<String> m_Remark;
  private Amount m_TotSaldo;

  // Private constructor voor de Builder
  private AccountDetails(Builder builder) {
    this.m_RootAccount = builder.rootAccount;
    this.m_ChildAccounts = builder.childAccounts;
    this.m_LocalDate = builder.localDate;
    this.m_AccountNr = builder.accountNr;
    this.m_AccountName = builder.accountName;
    this.m_Amount = builder.amount;
    this.m_Saldo = builder.saldo;
    this.m_Remark = builder.remark;
    this.m_TotSaldo = builder.totSaldo;
  }

  /**
   * Default constructor
   */
  public AccountDetails() {
    this.m_RootAccount = null;
    this.m_ChildAccounts = "";
    this.m_LocalDate = null;
    this.m_AccountNr = "";
    this.m_AccountName = "";
  }

  public AccountDetails(AccountDetails accdet) {
    this.m_RootAccount = accdet.get_RootAccount();
    this.m_ChildAccounts = accdet.get_ChildAccounts();
    this.m_LocalDate = accdet.getLocalDate();
    this.m_AccountNr = accdet.get_AccountNr();
    this.m_AccountName = accdet.get_AccountName();
    this.m_Amount = accdet.get_Amount();
    this.m_Saldo = accdet.get_Saldo();
    this.m_Remark = accdet.get_Remark();
    this.m_TotSaldo = accdet.get_TotSaldo();
  }

  // Getters
  public LocalDate getLocalDate() {
    return m_LocalDate;
  }

  public String getLocalDateStr() {
    DateTimeFormatter lformatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    String formattedLocalDate = m_LocalDate.format(lformatter);
    return formattedLocalDate;
  }

  public String get_AccountNr() {
    return m_AccountNr;
  }

  public String get_AccountName() {
    return m_AccountName;
  }

  public Amount get_Amount() {
    return m_Amount;
  }

  public Amount get_Saldo() {
    return m_Saldo;
  }

  public Optional<String> get_Remark() {
    return m_Remark;
  }

  public Amount get_TotSaldo() {
    return m_TotSaldo;
  }

  public Account get_RootAccount() {
    return m_RootAccount;
  }

  public String get_ChildAccounts() {
    return m_ChildAccounts;
  }

  // Setters
  public void set_LocalDate(LocalDate a_localDate) {
    m_LocalDate = a_localDate;
  }

  /**
   * Set LocalDate
   * 
   * @param a_localDate bv. "2026-07-24"
   */
  public void set_LocalDate(String a_localDate) {
    m_LocalDate = LocalDate.parse(a_localDate);
  }

  public void set_AccountNr(String m_AccountNr) {
    this.m_AccountNr = m_AccountNr;
  }

  public void set_AccountName(String m_AccountName) {
    this.m_AccountName = m_AccountName;
  }

  public void set_Amount(Amount m_Amount) {
    this.m_Amount = m_Amount;
  }

  public void set_Saldo(Amount m_Saldo) {
    this.m_Saldo = m_Saldo;
  }

  public void set_TotSaldo(Amount m_totSaldo) {
    this.m_TotSaldo = m_totSaldo;
  }

  public void set_Remark(String m_Remark) {
    this.m_Remark = Optional.ofNullable(m_Remark);
  }

  public void set_RootAccount(Account m_RootAccount) {
    this.m_RootAccount = m_RootAccount;
  }

  public void set_ChildAccounts(String m_ChildAccounts) {
    this.m_ChildAccounts = m_ChildAccounts;
  }

  public String print() {
    String regel = "";
    regel = regel + "Date: " + m_LocalDate.toString();
    regel = regel + " | Root: " + m_RootAccount;
    regel = regel + " | Childs: " + m_ChildAccounts;
    regel = regel + " | AccNr: " + m_AccountNr;
    regel = regel + " | AccName: " + m_AccountName;
    regel = regel + " | Amount: " + m_Amount.toString().replace(".", ",");
    regel = regel + " | Saldo: " + m_Saldo.toString().replace(".", ",");
    regel = regel + " | Remark: " + m_Remark;
    regel = regel + " | TotSaldo: " + m_TotSaldo.toString().replace(".", ",");
    return regel;
  }

  // Builder Class
  public static class Builder {
    private Account rootAccount;
    private String childAccounts = "";
    private LocalDate localDate;
    private String accountNr = "";
    private String accountName = "";
    private Amount amount;
    private Amount saldo;
    private Optional<String> remark;
    private Amount totSaldo;

    public Builder() {
      // Default constructor
    }

    public Builder rootAccount(Account rootAccount) {
      this.rootAccount = rootAccount;
      return this;
    }

    public Builder childAccounts(String childAccounts) {
      this.childAccounts = notNull(childAccounts);
      return this;
    }

    public Builder localDate(LocalDate localDate) {
      this.localDate = localDate;
      return this;
    }

    public Builder accountNr(String accountNr) {
      this.accountNr = notNull(accountNr);
      return this;
    }

    public Builder accountName(String accountName) {
      this.accountName = notNull(accountName);
      return this;
    }

    public Builder amount(Amount amount) {
      this.amount = amount;
      return this;
    }

    public Builder saldo(Amount saldo) {
      this.saldo = saldo;
      return this;
    }

    public Builder remark(Optional<String> a_notes) {
      this.remark = a_notes;
      return this;
    }

    public Builder totSaldo(Amount totSaldo) {
      this.totSaldo = totSaldo;
      return this;
    }

    public AccountDetails build() {
      return new AccountDetails(this);
    }

    private String notNull(String a_param) {
      if (a_param == null) {
        return "";
      }
      return a_param;
    }
  }
}