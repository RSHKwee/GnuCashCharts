package kwee.gnucashcharts.library.html;

import java.util.logging.Level;
import java.util.logging.Logger;

import kwee.logger.MyLogger;

public class DataRij {
  private static final Logger lOGGER = MyLogger.getLogger();

  String m_AccountNr = "";
  String m_AccountName = "";
  double m_Amount = 0.0;
  double m_Saldo = 0.0;
  String m_Remark = "";

  public DataRij(String a_Regel) {
    String[] l_elems = a_Regel.split(";");
    if (l_elems.length >= 6) {
      m_AccountNr = l_elems[0];
      m_AccountName = l_elems[1];

      if (!l_elems[3].isBlank()) {
        m_Amount = convertToDouble(l_elems[3]);
      }

      if (!l_elems[4].isBlank()) {
        m_Amount = convertToDouble(l_elems[4]);
      }

      if (!l_elems[5].isBlank()) {
        m_Amount = convertToDouble(l_elems[5]);
      }

      if (l_elems.length >= 7) {
        if (!l_elems[6].isBlank()) {
          m_Amount = convertToDouble(l_elems[6]);
        }
      }
      if (l_elems.length >= 8) {
        m_Saldo = convertToDouble(l_elems[7]);
      }
      if (l_elems.length >= 9) {
        m_Remark = l_elems[8];
      }
    }
  }

  public String getAccountNr() {
    return m_AccountNr;
  }

  public String getAccountName() {
    return m_AccountName;
  }

  public double getAmount() {
    return m_Amount;
  }

  public double getSaldo() {
    return m_Saldo;
  }

  public String getRemark() {
    return m_Remark;
  }

  public String printHeader() {
    String head = "Accountnr; Accountname; Amount; Saldo; Remark";
    return head;
  }

  public String print() {
    String l_Amount = Double.toString(m_Amount);
    String l_Saldo = Double.toString(m_Saldo);
    return String.join(";", m_AccountNr, m_AccountName, l_Amount, l_Saldo, m_Remark);
  }

  private double convertToDouble(String input) {
    lOGGER.log(Level.FINE, "input: " + input);
    // Remove the Euro currency symbol and the thousand separator
    input = input.trim();
    String[] selems = input.split(" ");
    if (selems.length > 1) {
      input = selems[1];
    }
    String cleanedInput = input.replace("€", "").replace(".", "").replace(",", ".");
    lOGGER.log(Level.FINE, "cleanedInput: " + cleanedInput);

    try {
      // Parse the cleaned string as a double
      double result = Double.parseDouble(cleanedInput);
      return result;
    } catch (Exception e) {
      if (!cleanedInput.isBlank()) {
        lOGGER.log(Level.INFO, "Error converting to double: " + e.getMessage() + " " + cleanedInput);
        // Handle any parsing exceptions
        // System.err.println("Error converting to double: " + e.getMessage());
      }
      return 0.0; // Return a default value or handle the error accordingly
    }
  }
}
