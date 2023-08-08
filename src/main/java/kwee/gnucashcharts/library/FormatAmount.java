package kwee.gnucashcharts.library;

import java.text.NumberFormat;
import java.util.Locale;

public class FormatAmount {

  public static String formatAmount(double amt) {
    Locale locale = new Locale("nl", "NL"); // Dutch locale for Euro format
    NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(locale);
    String formattedNumber = currencyFormat.format(amt);
    return formattedNumber;
  }

}
